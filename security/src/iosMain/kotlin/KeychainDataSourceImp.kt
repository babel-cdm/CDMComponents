package com.babel.cdm.components.security

import com.babel.cdm.components.common.Either
import com.babel.cdm.components.common.flatMap
import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.*
import platform.LocalAuthentication.LAContext
import platform.Security.*
import platform.darwin.NSUInteger
import platform.darwin.OSStatus
import platform.darwin.UInt8Var

const val NOT_FOUND = "Key not found"
const val WRONG_VALUE = "Wrong param"
const val SAVE_KEY = "Error saving the key"

class KeychainDataSourceImp : KeychainDataSource {
    override fun generateAppKey(): Either<SecurityError, SecKeyRef> {
        memScoped {

            val publicKeyAttr = createPublicKeyParams()
            val privateKeyAttr = createPrivateKeyParams()
            val generatePairKeyAttr = createGenerateKeyParams( publicKeyAttr, privateKeyAttr)

            val publicKey = alloc<SecKeyRefVar>()
            val privateKey = alloc<SecKeyRefVar>()

            val status =
                SecKeyGeneratePair(generatePairKeyAttr, publicKey.ptr, privateKey.ptr)

            if (status != errSecSuccess || publicKey.value == null || privateKey.value == null) {
                return Either.Left(SecurityError(IOSCode.WRONG_VALUE_PARAM.code, NOT_FOUND))
            }

            return saveInKeyChain(publicKey.value!!, false).flatMap {
                saveInKeyChain(privateKey.value!!, true).flatMap {
                    getAppKey(false)
                }
            }
        }


    }

    private fun createPublicKeyParams(): CFMutableDictionaryRef {

        val dict = CFDictionaryCreateMutable(null, 3, null, null)

        CFDictionaryAddValue(dict, kSecAttrIsPermanent, kCFBooleanTrue)
        CFDictionaryAddValue(dict, kSecAttrApplicationTag, stringToCFDict(APP_ALIAS))
        CFDictionaryAddValue(dict, kSecAttrLabel, stringToCFDict(PUBLIC_KEY_ALIAS))

        return dict!!

    }

    private fun createPrivateKeyParams(): CFMutableDictionaryRef {

        val dict = CFDictionaryCreateMutable(null, 6, null, null)

        val accessControl = SecAccessControlCreateWithFlags(
            kCFAllocatorDefault,
            kSecAttrAccessibleWhenUnlocked,
            kSecAccessControlUserPresence,
            null
        )

        CFDictionaryAddValue(dict, kSecAttrIsPermanent, kCFBooleanTrue)
        CFDictionaryAddValue(dict, kSecAttrApplicationTag, stringToCFDict(APP_ALIAS))
        CFDictionaryAddValue(dict, kSecAttrLabel, stringToCFDict(PRIVATE_KEY_ALIAS))
        CFDictionaryAddValue(dict, kSecUseAuthenticationContext, CFBridgingRetain(LAContext()))
        CFDictionaryAddValue(dict, kSecUseAuthenticationUI, kSecUseAuthenticationUIAllow)
        CFDictionaryAddValue(dict, kSecAttrAccessControl, accessControl)

        return dict!!

    }

    private fun createGenerateKeyParams(
        publicKeyAttr: CFMutableDictionaryRef,
        privateKeyAttr: CFMutableDictionaryRef
    ): CFMutableDictionaryRef {

        val dict = CFDictionaryCreateMutable(null, 4, null, null)

        CFDictionaryAddValue(dict, kSecAttrKeyType, kSecAttrKeyTypeRSA)
        CFDictionaryAddValue(dict, kSecAttrKeySizeInBits, CFBridgingRetain(NSNumber(KEY_SIZE)))
        CFDictionaryAddValue(dict, kSecPublicKeyAttrs, publicKeyAttr)
        CFDictionaryAddValue(dict, kSecPrivateKeyAttrs, privateKeyAttr)

        return dict!!

    }

    override fun getAppKey(private: Boolean): Either<SecurityError, SecKeyRef> {
        memScoped {

            val keyName = if (private) PRIVATE_KEY_ALIAS else PUBLIC_KEY_ALIAS

            val query = CFDictionaryCreateMutable(null, 6, null, null)

            CFDictionaryAddValue(query, kSecClass, kSecClassKey)
            CFDictionaryAddValue(query, kSecMatchLimit, kSecMatchLimitOne)
            CFDictionaryAddValue(query, kSecAttrKeyType, kSecAttrKeyTypeRSA)
            CFDictionaryAddValue(query, kSecAttrApplicationTag, stringToCFDict(APP_ALIAS))
            CFDictionaryAddValue(query, kSecAttrLabel, stringToCFDict(keyName))
            CFDictionaryAddValue(query, kSecReturnRef, kCFBooleanTrue)

            val result = alloc<CFTypeRefVar>()

            val status: OSStatus = SecItemCopyMatching(query, result.ptr)

            return if (status == errSecSuccess && result.value != null) {
                Either.Right(result.value as SecKeyRef)
            } else {
                Either.Left(SecurityError(IOSCode.KEY_NOT_FOUND.code, WRONG_VALUE))
            }

        }
    }

    private fun saveInKeyChain(key: SecKeyRef, private: Boolean): Either<SecurityError, Boolean> {
        memScoped {

            val keyClass = if (private) kSecAttrKeyClassPrivate else kSecAttrKeyClassPublic
            val keyName = if (private) PRIVATE_KEY_ALIAS else PUBLIC_KEY_ALIAS

            val query = CFDictionaryCreateMutable(null, 8, null, null)

            CFDictionaryAddValue(query, kSecClass, kSecClassKey)
            CFDictionaryAddValue(query, kSecAttrKeyType, kSecAttrKeyTypeRSA)
            CFDictionaryAddValue(query, kSecAttrKeyClass, keyClass)
            CFDictionaryAddValue(query, kSecAttrApplicationTag, stringToCFDict(APP_ALIAS))
            CFDictionaryAddValue(query, kSecAttrLabel, stringToCFDict(keyName))
            CFDictionaryAddValue(query, kSecValueRef, key)
            CFDictionaryAddValue(query, kSecAttrIsPermanent, kCFBooleanTrue)
            CFDictionaryAddValue(query, kSecReturnData, kCFBooleanTrue)

            val result = alloc<CFTypeRefVar>()

            var status = SecItemAdd(query, result.ptr)

            if (status == errSecDuplicateItem) {
                status = SecItemDelete(query)
                status = SecItemAdd(query, result.ptr)
            }

            return if (status == errSecSuccess) {
                Either.Right(true)
            } else {
                Either.Left(SecurityError(IOSCode.SAVE_KEY_ERROR.code, SAVE_KEY))
            }
        }
    }

    private fun stringToCFDict(string: String): CFTypeRef? {
        memScoped {
            return CFBridgingRetain(
                NSData.dataWithBytes(
                    bytes = string.cstr.ptr,
                    length = string.length.toULong()
                )
            )
        }
    }

}