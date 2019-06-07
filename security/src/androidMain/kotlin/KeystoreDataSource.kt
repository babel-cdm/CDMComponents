package com.babel.cdm.components.security

import com.babel.cdm.components.common.Either
import javax.crypto.SecretKey

interface KeystoreDataSource {

    fun generateAppKey(): Either<SecurityError, SecretKey?>

}