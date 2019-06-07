package com.babel.cdm.components.security

import com.babel.cdm.components.common.Either
import javax.crypto.SecretKey

interface KeystoreDataSource {

    companion object {
        const val ALIAS = "CDMComponentSecurity"
    }

    fun generateAppKey(): Either<SecurityError, SecretKey?>

}