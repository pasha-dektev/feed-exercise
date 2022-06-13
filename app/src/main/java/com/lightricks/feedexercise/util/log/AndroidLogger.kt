package com.lightricks.feedexercise.util.log

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Android implementation for Logger interface
 */
@Singleton
class AndroidLogger @Inject constructor() : Logger {
    override fun debug(tag: String, msg: String, throwable: Throwable?) {
        Log.d(tag, msg, throwable)
    }

    override fun info(tag: String, msg: String, throwable: Throwable?) {
        Log.i(tag, msg, throwable)
    }

    override fun warn(tag: String, msg: String, throwable: Throwable?) {
        Log.w(tag, msg, throwable)
    }

    override fun error(tag: String, msg: String, throwable: Throwable?) {
        Log.e(tag, msg, throwable)
    }
}
