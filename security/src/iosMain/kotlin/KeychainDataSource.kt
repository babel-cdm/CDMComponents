package com.babel.cdm.components.security

import com.babel.cdm.components.common.Either
import platform.Security.SecKeyRef

interface KeychainDataSource {

    fun generateAppKey(): Either<SecurityError, SecKeyRef>

    fun getAppKey(private: Boolean): Either<SecurityError, SecKeyRef>

}