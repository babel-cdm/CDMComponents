package com.babel.cdm.components.security

import com.babel.cdm.components.common.*
import com.babel.cdm.components.security.IOSCode.*
import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.*
import platform.Security.SecKeyCreateDecryptedData
import platform.Security.SecKeyCreateEncryptedData
import platform.Security.SecKeyRef
import platform.Security.kSecKeyAlgorithmRSAEncryptionPKCS1
import platform.darwin.UInt8Var

const val ENCRYPT_ERROR = "Encrypt error"
const val DECRYPT_ERROR = "Decrypt error"
const val EMPTY_RECOVER = "Empty recovered value"
const val ENCODING = "Encoding error"

actual class SecurityUtils actual constructor() {

    private val LOG_TAG = "SecurityUtils"

    private val defaults: NSUserDefaults = NSUserDefaults.standardUserDefaults()

    actual fun storeSecure(key: String, value: String): Either<CDMComponentsError, String> {

        val keychainDataSource = KeychainDataSourceImp()

        val data = (value as NSString).dataUsingEncoding(NSUTF8StringEncoding)

        LoggerUtils.logD(LOG_TAG,"Value encoded")

        return if (data != null) {

            LoggerUtils.logD(LOG_TAG,"Recovering key...")

            val secKey: Either<SecurityError, SecKeyRef> = keychainDataSource.getAppKey(false).fold(
                {
                    if (it.id == KEY_NOT_FOUND.code) {
                        LoggerUtils.logD(LOG_TAG,"Key not found")
                        LoggerUtils.logD(LOG_TAG,"Generating key")
                        return@fold keychainDataSource.generateAppKey()
                    } else {
                        LoggerUtils.logE(LOG_TAG,"Error recovering key: ${it.id}")
                        return@fold Either.Left(it)
                    }
                },
                {
                    LoggerUtils.logD(LOG_TAG,"Key found")
                    return@fold Either.Right(it)
                })


            val encryptedValue = secKey.flatMap {
                LoggerUtils.logD(LOG_TAG,"Encrypting value...")
                return@flatMap encrypt(data, it)
            }

            encryptedValue.flatMap {
                LoggerUtils.logD(LOG_TAG,"Storing encrypted value...")
                storeEncryptedValue(it, key)
                return@flatMap Either.Right(key)
            }

        } else {
            LoggerUtils.logE(LOG_TAG,"Error storeSecure: ${WRONG_VALUE_PARAM.code}")
            Either.Left(SecurityError(WRONG_VALUE_PARAM.code, WRONG_VALUE))
        }

    }

    actual fun retrieveFromSecureStorage(key: String): Either<CDMComponentsError, String> {

        LoggerUtils.logD(LOG_TAG,"Recovering encrypted value...")
        val encryptedValue = retrieveEncryptedValue(key)

        val keychainDataSource = KeychainDataSourceImp()

        LoggerUtils.logD(LOG_TAG,"Recovering key...")
        val secretKey = keychainDataSource.getAppKey(true)

        val decryptedData = secretKey.flatMap {
            if (encryptedValue != null) {
                LoggerUtils.logD(LOG_TAG,"Decrypting value...")
                return@flatMap decrypt(encryptedValue, it)
            } else {
                LoggerUtils.logE(LOG_TAG,"Error recovering data: ${RECOVER_ERROR.code}")
                return@flatMap Either.Left(SecurityError(RECOVER_ERROR.code, EMPTY_RECOVER))
            }
        }

        return decryptedData.flatMap { data ->
            LoggerUtils.logD(LOG_TAG,"Encoding data...")
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