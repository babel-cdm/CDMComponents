package com.babel.cdm.components.security

import com.babel.cdm.components.common.CDMComponentsError

class SecurityError(
    id: Int,
    description: String): CDMComponentsError(id,description) {

    enum class CommonCode(val code:Int){
        SECURITY_DEFAULT_ANDROID_ERROR(2001)
    }





}