package com.babel.cdm.components.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.babel.cdm.components.common.Either
import com.babel.cdm.components.security.AndroidCode.*
import java.security.KeyException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.UnrecoverableKeyException
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

private const val NO_DESCRIPTION = "No description"

class KeystoreDataSourceImp(
    private val keyStore: KeyStore,
    private val keyGenerator: KeyGenerator
) : KeystoreDataSource {

    override fun generateAppKey(): Either<SecurityError, SecretKey> {
        try {

            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()

            keyGenerator.init(keyGenParameterSpec)

            return Either.Right(keyGenerator.generateKey())

        } catch (e: KeyStoreException) {
            return Either.Left(SecurityError(KEYSTORE_EXCEPTION.code, e.message ?: NO_DESCRIPTION ))
        } catch (e: KeyException) {
            return Either.Left(SecurityError(KEY_EXCEPTION.code, e.message ?: NO_DESCRIPTION))
        } catch (e: NullPointerException) {
            return Either.Left(SecurityError(NULL_POINTER.code, e.message ?: NO_DESCRIPTION))
        }
    }

    override fun getAppKey(): Either<SecurityError, SecretKey> {
        try {

            keyStore.load(null)

            val secretKeyEntry = keyStore.getEntry(ALIAS, null) as KeyStore.SecretKeyEntry

            return Either.Right(secretKeyEntry.secretKey)

        } catch (e: KeyStoreException) {
            return Either.Left(SecurityError(KEYSTORE_EXCEPTION.code, e.message ?: NO_DESCRIPTION))
        } catch (e: KeyException) {
            return Either.Left(SecurityError(KEY_EXCEPTION.code, e.message ?: NO_DESCRIPTION))
        } catch (e: NullPointerException) {
            return Either.Left(SecurityError(NULL_POINTER.code, e.message ?: NO_DESCRIPTION))
        } catch (e: UnrecoverableKeyException) {
            return Either.Left(SecurityError(APP_KEY_DOES_NOT_EXIST.code, e.message ?: NO_DESCRIPTION))
        } catch (e: TypeCastException) {
            return Either.Left(SecurityError(APP_KEY_DOES_NOT_EXIST.code, e.message ?: NO_DESCRIPTION))
        }
    }

}