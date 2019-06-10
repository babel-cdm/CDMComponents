package com.babel.cdm.components.security

const val KEY_ALIAS = "CDMComponentSecurity"
const val APP_ALIAS = "CDMComponentSecurity_App"
const val KEY_SIZE = 1024

enum class IOSCode(val code: Int){
    SECURITY_DEFAULT_IOS_ERROR(2201),
    KEY_NOT_FOUND(2202),
    UNABLE_TO_ENCRYPT(2203),
    WRONG_VALUE_PARAM(2204),
    SAVE_KEY_ERROR(2205),
    RECOVER_ERROR(2206),
    UNABLE_TO_DECRYPT(2207),
    ENCODING_ERROR(2208)
}