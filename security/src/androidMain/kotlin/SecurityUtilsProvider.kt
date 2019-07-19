package com.babel.cdm.components.security

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.net.Uri
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator

const val SP = "CDMComponents_SP"

class SecurityUtilsProvider : ContentProvider() {
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null //NA
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return null //NA
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0 //NA
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0 //NA
    }

    override fun getType(uri: Uri): String? {
        return null //NA
    }

    override fun onCreate(): Boolean {

        CDMComponents.securityUtils = SecurityUtils.Builder()
            .prefs(context.getSharedPreferences(SP, MODE_PRIVATE))
            .keystoreDataSource(
                KeystoreDataSourceImp(
                    keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE),
                    keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
                )
            )
            .build()

        return true
    }


}