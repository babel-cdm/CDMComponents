package com.babel.cdm.components.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.babel.cdm.components.common.Either
import com.babel.cdm.components.common.LoggerUtils
import com.babel.cdm.components.security.AndroidCode.*
import java.security.KeyException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.UnrecoverableKeyException
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

const val NO_DESCRIPTION = "No description"

class KeystoreDataSourceImp(
    private val keyStore: KeyStore,
    private val keyGenerator: KeyGenerator
) : KeystoreDataSource {

    val LOG_TAG = "KeystoreDataSourceImp"

    override fun generateAppKey(): Either<SecurityError, SecretKey> {
        try {

            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            LoggerUtils.logD(LOG_TAG,"Creating KeyGenerator...")
            keyGenerator.init(keyGenParameterSpec)

            LoggerUtils.logD(LOG_TAG,"Generating key...")
            return Either.Right(keyGenerator.generateKey())

        } catch (e: KeyStoreException) {
            LoggerUtils.logE(LOG_TAG,"KEYSTORE_EXCEPTION: ${e.message}")
            return Either.Left(SecurityError(KEYSTORE_EXCEPTION.code, e.message ?: NO_DESCRIPTION ))
        } catch (e: KeyException) {
            LoggerUtils.logE(LOG_TAG,"KEY_EXCEPTION: ${e.message}")
            return Either.Left(SecurityError(KEY_EXCEPTION.code, e.message ?: NO_DESCRIPTION))
        } catch (e: NullPointerException) {
            LoggerUtils.logE(LOG_TAG,"NULL_POINTER")
            return Either.Left(SecurityError(NULL_POINTER.code, e.message ?: NO_DESCRIPTION))
        }
    }

    override fun getAppKey(): Either<SecurityError, SecretKey> {
        try {

            LoggerUtils.logD(LOG_TAG,"Loading keystore...")
            keyStore.load(null)

            LoggerUtils.logD(LOG_TAG,"Getting key...")
            val secretKeyEntry = keyStore.getEntry(ALIAS, null) as KeyStore.SecretKeyEntry

            return Either.Right(secretKeyEntry.secretKey)

        } catch (e: KeyStoreException) {
            LoggerUtils.logE(LOG_TAG,"KEYSTORE_EXCEPTION: ${e.message}")
            return Either.Left(SecurityError(KEYSTORE_EXCEPTION.code, e.message ?: NO_DESCRIPTION))
        } catch (e: KeyException) {
            LoggerUtils.logE(LOG_TAG,"KEY_EXCEPTION: ${e.message}")
            return Either.Left(SecurityError(KEY_EXCEPTION.code, e.message ?: NO_DESCRIPTION))
        } catch (e: NullPointerException) {
            LoggerUtils.logE(LOG_TAG,"NULL_POINTER: ${e.message}")
            return Either.Left(SecurityError(NULL_POINTER.code, e.message ?: NO_DESCRIPTION))
        } catch (e: UnrecoverableKeyException) {
            LoggerUtils.logE(LOG_TAG,"APP_KEY_DOES_NOT_EXIST: ${e.message}")
            return Either.Left(SecurityError(APP_KEY_DOES_NOT_EXIST.code, e.message ?: NO_DESCRIPTION))
        } catch (e: TypeCastException) {
            LoggerUtils.logE(LOG_TAG,"APP_KEY_DOES_NOT_EXIST: ${e.message}")
            return Either.Left(SecurityError(APP_KEY_DOES_NOT_EXIST.code, e.message ?: NO_DESCRIPTION))
        }
    }

}