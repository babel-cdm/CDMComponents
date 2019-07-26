package com.babel.cdm.components.security

import android.content.SharedPreferences
import android.util.Base64
import com.babel.cdm.components.common.CDMComponentsError
import com.babel.cdm.components.common.Either
import com.babel.cdm.components.common.LoggerUtils
import com.babel.cdm.components.common.flatMap
import com.babel.cdm.components.security.AndroidCode.APP_KEY_DOES_NOT_EXIST
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

actual class SecurityUtils actual constructor() {

    val LOG_TAG = "SecurityUtils"
    val EMPTY_VALUE = ""

    private lateinit var prefs: SharedPreferences
    private lateinit var keystoreDataSource: KeystoreDataSource

    private constructor(builder: Builder) : this() {
        this.prefs = builder.prefs!!
        this.keystoreDataSource = builder.keystoreDataSource!!
    }

    data class Builder(
        var prefs: SharedPreferences? = null,
        var keystoreDataSource: KeystoreDataSource? = null
    ) {
        fun prefs(preferences: SharedPreferences) = apply { this.prefs = preferences }
        fun keystoreDataSource(keystoreDataSource: KeystoreDataSource) =
            apply { this.keystoreDataSource = keystoreDataSource }

        fun build() = SecurityUtils(this)
    }

    actual fun storeSecure(key: String, value: String): Either<CDMComponentsError, String> {

        LoggerUtils.logD(LOG_TAG, "Obtaining secret key...")
        val secretKey = obtainSecretKey(keystoreDataSource)

        LoggerUtils.logD(LOG_TAG, "Encrypting value...")
        val encryptedValue = secretKey.map { encryptText(value, it) }

        return encryptedValue.map { e ->
            LoggerUtils.logD(LOG_TAG, "Storing encrypted value...")
            storeEncryptedValue(key, e)

            return@map key

        }
    }

    actual fun retrieveFromSecureStorage(key: String): Either<CDMComponentsError, String> {

        LoggerUtils.logD(LOG_TAG, "Recovering encrypted value...")
        val encryptedValue = retrieveEncryptedValue(key)

        LoggerUtils.logD(LOG_TAG, "Obtaining secret key...")
        val secretKey = obtainSecretKey(keystoreDataSource)

        LoggerUtils.logD(LOG_TAG, "Decrypting value...")
        return secretKey.flatMap { decryptText(encryptedValue, it) }

    }

    private fun storeEncryptedValue(key: String, e: EncryptedValue) {

        LoggerUtils.logD(LOG_TAG, "SharedPreferences: $prefs")

        val editor = prefs?.edit()
        editor?.putString(key, Base64.encodeToString(e.encrypted, Base64.NO_WRAP))
        editor?.putString(key + IV_SUFIX, Base64.encodeToString(e.iv, Base64.NO_WRAP))
        editor?.apply()

    }

    private fun retrieveEncryptedValue(key: String): EncryptedValue {

        LoggerUtils.logD(LOG_TAG, "SharedPreferences: $prefs")

        val encryptedB64 = prefs?.getString(key, EMPTY_VALUE)
        val ivB64 = prefs?.getString(key + IV_SUFIX, EMPTY_VALUE)

        LoggerUtils.logD(LOG_TAG, "Recovering B64 encrypted:$encryptedB64 iv:$ivB64")

        val encrypted = Base64.decode(encryptedB64, Base64.NO_WRAP)
        val iv = Base64.decode(ivB64, Base64.NO_WRAP)
        return EncryptedValue(encrypted, iv)

    }

    private fun obtainSecretKey(keystoreDataSource: KeystoreDataSource): Either<SecurityError, SecretKey> {
        LoggerUtils.logD(LOG_TAG, "Getting secret key...")
        return keystoreDataSource.getAppKey().fold(
            {
                if (it.id == APP_KEY_DOES_NOT_EXIST.code) {
                    LoggerUtils.logI(LOG_TAG, "Secret key does not exist...")
                    keystoreDataSource.generateAppKey()
                } else {
                    LoggerUtils.logE(LOG_TAG, "Error getting secret key...")
                    Either.Left(it)
                }
            },
            {
                Either.Right(it)
            })

    }

    private fun encryptText(toEncrypt: String, secretKey: SecretKey): EncryptedValue {

        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)

        LoggerUtils.logD(LOG_TAG, "Init cipher...")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        LoggerUtils.logD(LOG_TAG, "Generating IV...")
        val iv = cipher.iv

        LoggerUtils.logD(LOG_TAG, "Encrypting...")
        val encryption = cipher.doFinal(toEncrypt.toByteArray(Charsets.UTF_8))
        LoggerUtils.logD(LOG_TAG, "Encrypted")

        return EncryptedValue(encryption, iv)

    }

    private fun decryptText(toDecrypt: EncryptedValue, secretKey: SecretKey): Either<SecurityError,String> {

        toDecrypt.iv?.let {
            if (it.isEmpty()) return Either.Left(SecurityError(AndroidCode.DECRYPT_ERROR.code, NO_DESCRIPTION))
        }

        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)

        val spec = GCMParameterSpec(tLen, toDecrypt.iv)

        LoggerUtils.logD(LOG_TAG, "Init cipher...")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        LoggerUtils.logD(LOG_TAG, "Decrypting...")
        val decodedData = cipher.doFinal(toDecrypt.encrypted)
        LoggerUtils.logD(LOG_TAG, "Decrypted")

        return Either.Right(String(decodedData, Charsets.UTF_8))

    }

}