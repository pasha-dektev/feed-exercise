package com.lightricks.feedexercise.network

import javax.inject.Inject
import javax.inject.Singleton

interface UrlProvider {
    val url: String
}

/**
 * Base url provider for Retrofit
 */
class RetrofitBaseUrlProvider @Inject constructor() : UrlProvider {
    override val url: String = "https://assets.swishvideoapp.com/"
}

/**
 * Base url provider for Image Loading
 */
class ThumbnailImageBaseUrlProvider @Inject constructor() : UrlProvider {
    override val url: String = "https://assets.swishvideoapp.com/Android/demo/catalog/thumbnails/"
}
