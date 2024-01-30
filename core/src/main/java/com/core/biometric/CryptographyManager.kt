package com.core.biometric

import com.core.clean.EncryptedData
import kotlinx.coroutines.flow.SharedFlow
import javax.crypto.Cipher

interface CryptographyManager {

    val bioEncodeFlow: SharedFlow<EncryptedData>
    val bioDecodeFlow: SharedFlow<String>
    val errorFlow: SharedFlow<Exception>

    suspend fun getInitializedCipherForEncryption(keyName: String): Cipher?

    suspend fun getInitializedCipherForDecryption(keyName: String): Cipher?

    suspend fun encryptData(plaintext: String, cipher: Cipher?)

    suspend fun decryptData(cipher: Cipher?)

    fun isBiometricEnabled(): Boolean

    suspend fun deleteBiometric()

    suspend fun deleteKey(keyName: String)

}