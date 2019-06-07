package com.babel.cdm.components.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.babel.cdm.components.common.Either
import com.babel.cdm.components.security.SecurityError.AndroidCode.*
import java.security.KeyException
import java.security.KeyStoreException
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class KeystoreDataSourceImp(
    private val keyGenerator: KeyGenerator
) : KeystoreDataSource {

    override fun generateAppKey(): Either<SecurityError, SecretKey?> {
        try {

            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KeystoreDataSource.ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()

            keyGenerator.init(keyGenParameterSpec)

            return Either.Right(keyGenerator.generateKey())

        } catch (e: KeyStoreException) {
            return Either.Left(SecurityError(KEYSTORE_EXCEPTION.code, e.message!!))
        } catch (e: KeyException) {
            return Either.Left(SecurityError(KEY_EXCEPTION.code, e.message!!))
        } catch (e: NullPointerException) {
            return Either.Left(SecurityError(NULL_POINTER.code, e.message!!))
        }
    }

}