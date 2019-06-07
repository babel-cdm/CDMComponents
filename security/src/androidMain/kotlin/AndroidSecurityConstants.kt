package com.babel.cdm.components.security

const val ALIAS = "CDMComponentSecurity"

enum class AndroidCode(val code: Int){
    SECURITY_DEFAULT_ANDROID_ERROR(2101),
    KEYSTORE_EXCEPTION(2102),
    KEY_EXCEPTION(2103),
    NULL_POINTER(2104)
}