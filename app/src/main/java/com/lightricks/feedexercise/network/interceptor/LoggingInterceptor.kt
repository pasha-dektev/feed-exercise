package com.lightricks.feedexercise.network.interceptor

import com.lightricks.feedexercise.util.log.Logger
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit interceptor for logging requests and responses
 * @param logTag tag to use in log messages
 */
@Singleton
class LoggingInterceptor(
    private val logTag: String,
    private val logger: Logger
) : Interceptor {

    @Inject
    constructor(logger: Logger) : this(logTag = "HTTP Client", logger = logger)

    private val delegate = HttpLoggingInterceptor { message -> logger.info(logTag, message) }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return delegate.intercept(chain)
    }
}
