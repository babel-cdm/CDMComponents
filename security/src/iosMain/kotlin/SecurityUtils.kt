package com.babel.cdm.components.security

import com.babel.cdm.components.common.*
import com.babel.cdm.components.security.IOSCode.*
import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.*
import platform.Security.*
import platform.darwin.UInt8Var

const val ENCRYPT_ERROR = "Encrypt error"
const val DECRYPT_ERROR = "Decrypt error"
const val EMPTY_RECOVER = "Empty recovered value"
const val ENCODING = "Encoding error"

actual class SecurityUtils actual constructor() {

    private val defaults: NSUserDefaults = NSUserDefaults.standardUserDefaults()

    actual fun storeSecure(key: String, value: String): Either<CDMComponentsError, String> {

        val keychainDataSource = KeychainDataSourceImp()

        val data = (value as NSString).dataUsingEncoding(NSUTF8StringEncoding)

        return if (data != null) {

            val secKey: Either<SecurityError, SecKeyRef> = keychainDataSource.getAppKey(false).fold(
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

            val encryptedValue = secKey.flatMap { encrypt(data, it) }

            encryptedValue.flatMap {
                storeEncryptedValue(it, key)
                return@flatMap Either.Right(key)
            }

        } else {
            Either.Left(SecurityError(WRONG_VALUE_PARAM.code, WRONG_VALUE))
        }

    }

    actual fun retrieveFromSecureStorage(key: String): Either<CDMComponentsError, String> {

        val encryptedValue = retrieveEncryptedValue(key)

        val keychainDataSource = KeychainDataSourceImp()

        val secretKey = keychainDataSource.getAppKey(true)

        val decryptedData = secretKey.flatMap {
            if (encryptedValue != null) {
                decrypt(encryptedValue, it)
            } else {
                Either.Left(SecurityError(RECOVER_ERROR.code, EMPTY_RECOVER))
            }
        }

        return decryptedData.flatMap { data ->
            NSString.stringWithUTF8String(data.bytes() as CPointer<ByteVar>)?.let { decryptedValue ->
                Either.Right(decryptedValue)
            } ?: Either.Left(SecurityError(ENCODING_ERROR.code, ENCODING))
        }

    }



    private fun encrypt(data: NSData, secKey: SecKeyRef): Either<SecurityError, NSData> {
        memScoped {

            val error = alloc<CFErrorRefVar>()

            val result = SecKeyCreateEncryptedData(
                key = secKey,
                algorithm = kSecKeyAlgorithmRSAEncryptionPKCS1,
                plaintext = dataToCFData(data),
                error = error.ptr
            )

            return result?.let {
                Either.Right(CFBridgingRelease(it) as NSData)
            } ?: Either.Left(SecurityError(UNABLE_TO_ENCRYPT.code, ENCRYPT_ERROR))
        }

    }

    private fun decrypt(encryptedValue: NSData, secKey: SecKeyRef): Either<SecurityError, NSData> {

        memScoped {

            val error = alloc<CFErrorRefVar>()

            val result = SecKeyCreateDecryptedData(
                key = secKey,
                algorithm = kSecKeyAlgorithmRSAEncryptionPKCS1,
                ciphertext = dataToCFData(data = encryptedValue),
                error = error.ptr
            )

            return result?.let {
                Either.Right(CFBridgingRelease(it) as NSData)
            } ?: Either.Left(SecurityError(UNABLE_TO_DECRYPT.code, DECRYPT_ERROR))
        }

    }

    private fun storeEncryptedValue(encryptedValue: NSData, key: String) {
        defaults.setObject(encryptedValue, key)
        defaults.synchronize()
    }

    private fun retrieveEncryptedValue(key: String): NSData? = defaults.dataForKey(key)

    private fun dataToCFData(data: NSData): CFDataRef? {
        return CFDataCreate(
            kCFAllocatorDefault,
            data.bytes as CValuesRef<UInt8Var>,
            NSNumber(unsignedLongLong = data.length) as CFIndex)
    }
}