package com.lightricks.feedexercise.util.log

interface Logger {
    fun debug(tag: String, msg: String, throwable: Throwable? = null)
    fun info(tag: String, msg: String, throwable: Throwable? = null)
    fun warn(tag: String, msg: String, throwable: Throwable? = null)
    fun error(tag: String, msg: String, throwable: Throwable? = null)
}
