package com.core.exceptions

import com.google.gson.annotations.SerializedName


data class AppError(val message: String?,
                    val exception: Exception? = null, val body: String? = null) {

    val isNoConnectionException get() = exception is NoConnectionException
}

data class LoginError(val username: String?, val password: String?)

data class AuthError(val username: String?, val password: String?, val email: String?, val code: String?)

data class VerifyError(val code: String)

data class GlobalError(val status: String, val message: String?)

data class ServiceError(val status: String, val message: String)

data class ImageError(val files: String)

data class UpdateProfileError(val email: String, val code: String)

data class NewPassError(val password: String, @SerializedName("password_new") val passwordNew: String,
                        @SerializedName("password_new_confirmation") val passwordNewConfirmation: String)

data class ChangePassError(
        val password: String, @SerializedName("password_confirmation")
        val passwordConfirmation: String)

data class ReplenishError(val amount: String)

data class RequestFSWalletError(@SerializedName("phone_number") val phoneNumber: String,
                                @SerializedName("fastshift_id") val fastShiftID: String)

data class TransferPayError(
        val amount: String, @SerializedName("payment_method") val paymentMethod: String,
        @SerializedName("payment_card_id") val paymentCardId: Int
)

data class HistoryFilterErrors(@SerializedName("filters_date_from") val filtersDateFrom: String?, val code: String?)


data class SearchItemErrors(
        @SerializedName("credit_code") val creditCode: String,
        val code: String,
        val passport: String,
)



