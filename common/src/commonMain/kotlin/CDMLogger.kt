package com.babel.cdm.components.common

import com.babel.cdm.components.common.LogLevel.*


sealed class LogLevel(val value: Int) {
    object DEBUG : LogLevel(40)
    object INFO : LogLevel(30)
    object WARN : LogLevel(20)
    object ERROR : LogLevel(10)
    object NONE: LogLevel(0)
}

var logLevel: LogLevel = INFO

fun d(tag: String, message: String){
    if(logLevel.value >= DEBUG.value) info(tag, message)
}

fun i(tag: String, message: String){
    if(logLevel.value >= INFO.value) info(tag, message)
}

fun w(tag: String, message: String){
    if(logLevel.value >= WARN.value) info(tag, message)
}

fun e(tag: String, message: String){
    if(logLevel.value >= ERROR.value) info(tag, message)
}

internal expect fun debug(tag: String, message: String)

internal expect fun info(tag: String, message: String)

internal expect fun warn(tag: String, message: String)

internal expect fun error(tag: String, message: String)



