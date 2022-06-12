package com.lightricks.feedexercise.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

interface RetrofitProvider {
    val retrofit: Retrofit
}

/** Entity responsible for providing Retrofit instance
 *  @param urlProvider provider for base url
 *  @param loggingInterceptor interceptor for logging requests/responses
 */
class RetrofitProviderImpl(
    private val urlProvider: UrlProvider,
    private val loggingInterceptor: Interceptor,
    private val converterFactory: Converter.Factory,
) : RetrofitProvider {

    override val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(urlProvider.url)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()
            )
            .addConverterFactory(converterFactory)
            .build()
    }
}
