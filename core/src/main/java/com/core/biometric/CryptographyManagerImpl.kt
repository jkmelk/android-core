package com.core.biometric

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import com.core.clean.EncryptedData
import com.core.logger.log
import com.core.prefrences.AppPreferences
import com.core.prefrences.PreferenceKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.nio.charset.Charset
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStore
import java.security.UnrecoverableKeyException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptographyManagerImpl : CryptographyManager, KoinComponent {
    private val ANDROID_KEYSTORE = "AndroidKeyStore"

    private val preferences by inject<AppPreferences>()
    private val KEY_SIZE: Int = 256
    private val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    private val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

    private val _bioEncodeFlow = MutableSharedFlow<EncryptedData>()
    private val _bioDecodeFlow = MutableSharedFlow<String>()
    override val bioEncodeFlow = _bioEncodeFlow.asSharedFlow()
    override val bioDecodeFlow = _bioDecodeFlow.asSharedFlow()
    private val _errorFlow = MutableSharedFlow<Exception>()
    override val errorFlow = _errorFlow.asSharedFlow()

    override suspend fun getInitializedCipherForEncryption(keyName: String): Cipher? = try {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        if (keyStore.containsAlias(keyName)) {
            keyStore.deleteEntry(keyName)
        }
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keyName)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        cipher
    } catch (e: KeyPermanentlyInvalidatedException) {
        _errorFlow.emit(e)
        null
    } catch (error: InvalidAlgorithmParameterException) {
        _errorFlow.emit(error)
        null
    } catch (e: UnrecoverableKeyException) {
        getInitializedCipherForEncryption(keyName)
        log { e.message }
        null
    }

    override suspend fun deleteKey(keyName: String) {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        keyStore.deleteEntry(keyName)
    }

    override suspend fun getInitializedCipherForDecryption(keyName: String) = try {
        val cipher = getCipher()
        try {
            val secretKey = getOrCreateSecretKey(keyName)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, preferences.getByteArray(PreferenceKey.USER_IV)))
            cipher
        } catch (error: InvalidAlgorithmParameterException) {
            _errorFlow.emit(error)
            null
        }
    } catch (e: KeyPermanentlyInvalidatedException) {
        _errorFlow.emit(e)
        null
    }

    override suspend fun encryptData(plaintext: String, cipher: Cipher?) = cipher?.let {
        val cipherText = cipher.doFinal(plaintext.toByteArray(Charset.forName("UTF-8")))
        preferences.putByteArray(PreferenceKey.USER_IV, cipher.iv)
        preferences.putByteArray(PreferenceKey.USER_BIO, cipherText)
        preferences.putBoolean(PreferenceKey.BIOMETRIC_ATTACHED_KEY, true)
        _bioEncodeFlow.emit(EncryptedData(cipherText, cipher.iv))
    } ?: kotlin.run {
        throw RuntimeException("keystore cant be null")
    }

    override suspend fun decryptData(cipher: Cipher?) = cipher?.let {
        val plaintext = cipher.doFinal(preferences.getByteArray(PreferenceKey.USER_BIO))
        _bioDecodeFlow.emit(String(plaintext, Charset.forName("UTF-8")))
    } ?: kotlin.run {
        throw RuntimeException("keystore cant be null")
    }

    override suspend fun deleteBiometric() {
        preferences.putByteArray(PreferenceKey.USER_IV, null)
        preferences.putByteArray(PreferenceKey.USER_BIO, null)
        preferences.putBoolean(PreferenceKey.BIOMETRIC_ATTACHED_KEY, false)
    }

    override fun isBiometricEnabled(): Boolean {
        return preferences.getByteArray(PreferenceKey.USER_IV).isNotEmpty() &&
                preferences.getByteArray(PreferenceKey.USER_BIO).isNotEmpty() &&
                preferences.getBoolean(PreferenceKey.BIOMETRIC_ATTACHED_KEY)
    }

    private fun getCipher(): Cipher {
        val transformation = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"
        return Cipher.getInstance(transformation)
    }

    private fun getOrCreateSecretKey(keyName: String): SecretKey? = try {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        keyStore.getKey(keyName, null)?.let { return it as SecretKey }
        val paramsBuilder = KeyGenParameterSpec.Builder(keyName, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
        paramsBuilder.apply {
            setBlockModes(ENCRYPTION_BLOCK_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING)
            setKeySize(KEY_SIZE)
            setUserAuthenticationRequired(true)
        }
        val keyGenParams = paramsBuilder.build()
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        keyGenerator.init(keyGenParams)
        keyGenerator.generateKey()
    } catch (e: IllegalStateException) {
        log { "Manager" + e.message }
        null
    }
}