package com.babel.cdm.components.security

import com.babel.cdm.components.common.CDMComponentsError
import com.babel.cdm.components.common.Either
import com.babel.cdm.components.common.fold
import com.babel.cdm.components.common.map
import com.babel.cdm.components.security.IOSCode.*
import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.*
import platform.Security.*
import platform.darwin.OSStatus

const val ENCRYPT_ERROR = "Encrypt error"

actual class SecurityUtils actual constructor() {

    private val defaults: NSUserDefaults = NSUserDefaults.standardUserDefaults()

    actual fun storeSecure(key: String, value: String): Either<CDMComponentsError, String> {

        val keychainDataSource = KeychainDataSourceImp()

        val data = (value as NSString).dataUsingEncoding(NSUTF8StringEncoding)

        return if (data != null) {

            val secKey: Either<SecurityError, SecKeyRef> = keychainDataSource.getAppKey().fold(
                {
                    if (it.id == KEY_NOT_FOUND.code) {
                        keychainDataSource.generateAppKey()
                    } else {
                        Either.Left(it)
                    }
                },
                {
                    Either.Right(it)
                })


            val encryptedValue = secKey.map { encrypt(data, it) }

            defaults.setObject(encryptedValue, key)

            Either.Right(key)

        } else {

            Either.Left(SecurityError(WRONG_VALUE_PARAM.code, WRONG_VALUE))

        }

    }

    private fun encrypt(data: NSData, secKey: SecKeyRef): Either<SecurityError, NSData> {

        var error: CValuesRef<CFErrorRefVar>? = null

        val result = SecKeyCreateEncryptedData(
            key = secKey,
            algorithm = kSecKeyAlgorithmRSAEncryptionPKCS1,
            plaintext = data as CFDataRef,
            error = error
        )

        return result?.let {
            Either.Right(it as NSData)
        } ?: Either.Left(SecurityError(UNABLE_TO_ENCRYPT.code, ENCRYPT_ERROR))

    }

    actual fun retrieveFromSecureStorage(key: String): Either<CDMComponentsError, String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}