package com.babel.cdm.components.security

import com.babel.cdm.components.common.CDMComponentsError
import com.babel.cdm.components.common.Either

expect class SecurityUtils() {
    fun storeSecure(key: String, value: String): Either<CDMComponentsError, String>
    fun retrieveFromSecureStorage(key: String): Either<CDMComponentsError, String>
}

