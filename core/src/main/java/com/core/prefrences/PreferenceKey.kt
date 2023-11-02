package com.core.prefrences

enum class PreferenceKey(val keyName: String) {
    TOKEN("token"),
    REGISTRATION_KEY("Bearer "),
    DISPLAY_MODE("display mode"),
    PIN_CODE_ATTACHED("pin code attached"),
    USER_IV("user iv"),
    PIN_IV("pin iv"),
    USER_BIO("user bio"),
    USER_PIN("user pin"),
    BIOMETRIC_ATTACHED_KEY("biometric attached")
}