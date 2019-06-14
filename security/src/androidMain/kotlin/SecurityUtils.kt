package com.babel.cdm.components.security

import android.content.SharedPreferences
import android.security.keystore.KeyProperties
import android.util.Base64
import com.babel.cdm.components.common.CDMComponentsError
import com.babel.cdm.components.common.Either
import com.babel.cdm.components.security.AndroidCode.APP_KEY_DOES_NOT_EXIST
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

const val EMPTY_VALUE = ""

actual class SecurityUtils actual constructor() {

    private lateinit var prefs: SharedPreferences

    actual fun storeSecure(key: String, value: String): Either<CDMComponentsError, String> {

        val keystoreDataSource = KeystoreDataSourceImp(
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE),
            keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        )

        val secretKey = obtainSecretKey(keystoreDataSource)

        val encryptedValue = secretKey.map { encryptText(value, it) }

        return encryptedValue.map { e ->

            storeEncryptedValue(key, e)

            return@map key

        }
    }

    actual fun retrieveFromSecureStorage(key: String): Either<CDMComponentsError, String> {

        val encryptedValue = retrieveEncryptedValue(key)

        val keystoreDataSource = KeystoreDataSourceImp(
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE),
            keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        )

        val secretKey = obtainSecretKey(keystoreDataSource)

        return secretKey.map { decryptText(encryptedValue,it) }

    }

    private fun storeEncryptedValue(key: String, e: EncryptedValue) {

        val editor = prefs.edit()
        editor.putString(key, Base64.encodeToString(e.encrypted, Base64.NO_WRAP))
        editor.putString(key + IV_SUFIX, Base64.encodeToString(e.iv, Base64.NO_WRAP))
        editor.apply()

    }

    private fun retrieveEncryptedValue(key: String): EncryptedValue {

        val encrypted = Base64.decode(prefs.getString(key, EMPTY_VALUE), Base64.NO_WRAP)
        val iv = Base64.decode(prefs.getString(key + IV_SUFIX, EMPTY_VALUE), Base64.NO_WRAP)
        return EncryptedValue(encrypted, iv)

    }

    private fun obtainSecretKey(keystoreDataSource: KeystoreDataSourceImp): Either<SecurityError, SecretKey> {

        return keystoreDataSource.getAppKey().fold(
            {
                if (it.id == APP_KEY_DOES_NOT_EXIST.code) {
                    keystoreDataSource.generateAppKey()
                } else {
                    Either.Left(it)
                }
            },
            {
                Either.Right(it)
            })

    }

    private fun encryptText(toEncrypt: String, secretKey: SecretKey): EncryptedValue {

        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)

        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val iv = cipher.iv

        val encryption = cipher.doFinal(toEncrypt.toByteArray(Charsets.UTF_8))

        return EncryptedValue(encryption, iv)

    }

    private fun decryptText(toDecrypt: EncryptedValue, secretKey: SecretKey): String {

        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)

        val spec = GCMParameterSpec(tLen, toDecrypt.iv)

        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        val decodedData = cipher.doFinal(toDecrypt.encrypted)

        return String(decodedData, Charsets.UTF_8)

    }

}