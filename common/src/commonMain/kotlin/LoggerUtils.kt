package com.babel.cdm.components.common

interface Logger{
    fun setLevel(level: LoggerUtils.LogLevel)
    fun logD(tag: String, message: String)
    fun logI(tag: String, message: String)
    fun logW(tag: String, message: String)
    fun logE(tag: String, message: String)
}

var logLevel = LoggerUtils.LogLevel.DEBUG

object LoggerUtils: Logger {

    enum class LogLevel(val value: Int) {
        DEBUG(40),
        INFO(30),
        WARN(20),
        ERROR(10),
        NONE(0)
    }

    override fun setLevel(level: LogLevel) {
        logLevel = level
    }

    override fun logD(tag: String, message: String) {
        if (logLevel.value >= LogLevel.DEBUG.value) CDMLogger.debug(tag, message)
    }

    override fun logI(tag: String, message: String) {
        if (logLevel.value >= LogLevel.INFO.value) CDMLogger.info(tag, message)
    }

    override fun logW(tag: String, message: String) {
        if (logLevel.value >= LogLevel.WARN.value) CDMLogger.warn(tag, message)
    }

    override fun logE(tag: String, message: String) {
        if (logLevel.value >= LogLevel.ERROR.value) CDMLogger.error(tag, message)
    }

}