package com.babel.cdm.components.security

import com.babel.cdm.components.common.CDMComponentsError

class SecurityError(
    id: Int,
    description: String): CDMComponentsError(id,description) {

    enum class AndroidCode(val code: Int){
        SECURITY_DEFAULT_ANDROID_ERROR(2001),
        KEYSTORE_EXCEPTION(2002),
        KEY_EXCEPTION(2003),
        NULL_POINTER(2004)
    }

    enum class IOSCode(val code: Int){
        SECURITY_DEFAULT_IOS_ERROR(2001)
    }

}