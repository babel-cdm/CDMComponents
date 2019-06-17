package com.babel.cdm.components.common

import android.util.Log

internal actual fun debug(tag: String, message: String){
    Log.d("🐞 DEBUG $tag",message)
}

internal actual fun info(tag: String, message: String){
    Log.i("ℹ️ INFO $tag",message)
}

internal actual fun warn(tag: String, message: String){
    Log.w("⚠️ WARN $tag",message)
}

internal actual fun error(tag: String, message: String){
    Log.e("🛑️ ERROR $tag",message)
}