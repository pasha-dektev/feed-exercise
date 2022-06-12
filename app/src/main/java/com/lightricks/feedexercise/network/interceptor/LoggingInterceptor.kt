package com.lightricks.feedexercise.network.interceptor

import com.lightricks.feedexercise.util.log.Logger
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Retrofit interceptor for logging requests and responses
 * @param logTag tag to use in log messages
 */
class LoggingInterceptor(
    private val logTag: String = "HTTP Client",
    private val logger: Logger
) : Interceptor {

    private val delegate = HttpLoggingInterceptor { message -> logger.info(logTag, message) }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return delegate.intercept(chain)
    }
}
