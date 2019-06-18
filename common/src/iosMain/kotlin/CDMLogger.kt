package com.babel.cdm.components.common

actual object CDMLogger {

    actual fun debug(tag: String, message: String) {
        println("⚙️ DEBUG $tag - $message")
    }

    actual fun info(tag: String, message: String) {
        println("ℹ️ INFO $tag - $message")
    }

    actual fun warn(tag: String, message: String) {
        println("⚠️ WARN $tag - $message")
    }

    actual fun error(tag: String, message: String) {
        println("🛑️ ERROR $tag - $message")
    }

}