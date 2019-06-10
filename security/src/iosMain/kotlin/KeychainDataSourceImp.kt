package com.babel.cdm.components.security

import com.babel.cdm.components.common.Either
import com.babel.cdm.components.common.flatMap
import kotlinx.cinterop.CValuesRef
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.CFTypeRefVar
import platform.Foundation.NSDictionary
import platform.Security.*
import platform.darwin.OSStatus

const val NOT_FOUND = "Key not found"
const val WRONG_VALUE = "Wrong param"
const val SAVE_KEY = "Error saving the key"

class KeychainDataSourceImp : KeychainDataSource {
    override fun generateAppKey(): Either<SecurityError, SecKeyRef> {

        var publicKeyAttr: Map<CFStringRef?, Any> = createPublicKeyParams(KEY_ALIAS)
        var generatePairKeyAttr: Map<CFStringRef?, Any> = createGenerateKeyParams(publicKeyAttr)

        var publicKey: CValuesRef<SecKeyRefVar>? = null

        val status =
            SecKeyGeneratePair(generatePairKeyAttr as CFDictionaryRef, publicKey, null)

        if (status != errSecSuccess) {
            return Either.Left(SecurityError(IOSCode.WRONG_VALUE_PARAM.code, NOT_FOUND))
        }

        return saveInKeyChain(publicKey as SecKeyRef).flatMap {
            getAppKey()
        }


    }

    override fun getAppKey(): Either<SecurityError, SecKeyRef> {
        val query = mapOf(
            kSecClass to kSecClassKey,
            kSecAttrKeyType to kSecAttrKeyTypeRSA,
            kSecAttrApplicationTag to APP_ALIAS,
            kSecAttrLabel to KEY_ALIAS,
            kSecReturnRef to true
        ) as NSDictionary

        var result: CValuesRef<CFTypeRefVar>? = null

        val status: OSStatus = SecItemCopyMatching(query as CFDictionaryRef, result)

        return when (status) {
            errSecSuccess -> {
                Either.Right(result as SecKeyRef)
            }
            else -> {
                Either.Left(SecurityError(IOSCode.KEY_NOT_FOUND.code, WRONG_VALUE))
            }
        }
    }

    private fun createPublicKeyParams(tagPublic: String): Map<CFStringRef?, Any> =
        mapOf(
            kSecAttrIsPermanent to false,
            kSecAttrApplicationTag to APP_ALIAS,
            kSecAttrLabel to tagPublic
        )

    private fun createGenerateKeyParams(publicKeyAttr: Map<CFStringRef?, Any>): Map<CFStringRef?, Any> =
        mapOf(
            kSecAttrKeyType to kSecAttrKeyTypeRSA!!,
            kSecAttrKeySizeInBits to KEY_SIZE,
            kSecPublicKeyAttrs to publicKeyAttr
        )

    private fun saveInKeyChain(publicKey: SecKeyRef): Either<SecurityError,Boolean> {
        val query = mapOf(
            kSecClass to kSecClassKey,
            kSecAttrKeyType to kSecAttrKeyTypeRSA,
            kSecAttrKeyClass to kSecAttrKeyClassPublic,
            kSecAttrLabel to KEY_ALIAS,
            kSecAttrApplicationTag to APP_ALIAS,
            kSecValueRef to publicKey,
            kSecAttrIsPermanent to true,
            kSecReturnData to true
        )

        var raw: CValuesRef<CFTypeRefVar>? = null

        var status = SecItemAdd(query as CFDictionaryRef,raw)

        if(status == errSecDuplicateItem){
            status = SecItemDelete(query as CFDictionaryRef)
            status = SecItemAdd(query as CFDictionaryRef, raw)
        }

        return if(status == errSecSuccess){
            Either.Right(true)
        } else {
            Either.Left(SecurityError(IOSCode.SAVE_KEY_ERROR.code, SAVE_KEY))
        }
    }

}