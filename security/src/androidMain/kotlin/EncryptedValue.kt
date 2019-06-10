package com.babel.cdm.components.security

import java.util.*

data class EncryptedValue(
    var encrypted: ByteArray? = null,
    var iv: ByteArray? = null
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedValue

        if (!Arrays.equals(encrypted, other.encrypted)) return false
        if (!Arrays.equals(iv, other.iv)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = encrypted?.let { Arrays.hashCode(it) } ?: 0
        result = 31 * result + (iv?.let { Arrays.hashCode(it) } ?: 0)
        return result
    }
}