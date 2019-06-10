package com.babel.cdm.components.security

const val ALIAS = "CDMComponentSecurity"
const val ANDROID_KEYSTORE = "AndroidKeyStore"
const val CIPHER_ALGORITHM = "AES/GCM/NoPadding"
const val tLen = 128
const val SP_NAME = "CDMComponentSecurity"
const val IV_SUFIX = "IV"

enum class AndroidCode(val code: Int){
    SECURITY_DEFAULT_ANDROID_ERROR(2101),
    KEYSTORE_EXCEPTION(2102),
    KEY_EXCEPTION(2103),
    NULL_POINTER(2104),
    APP_KEY_DOES_NOT_EXIST(2105)
}