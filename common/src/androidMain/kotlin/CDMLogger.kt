package com.babel.cdm.components.common

import android.util.Log

actual object CDMLogger {

    actual fun debug(tag: String, message: String) {
        Log.d("⚙️ DEBUG $tag", message)
    }

    actual fun info(tag: String, message: String) {
        Log.i("ℹ️ INFO $tag", message)
    }

    actual fun warn(tag: String, message: String) {
        Log.w("⚠️ WARN $tag", message)
    }

    actual fun error(tag: String, message: String) {
        Log.e("🛑️ ERROR $tag", message)
    }

}