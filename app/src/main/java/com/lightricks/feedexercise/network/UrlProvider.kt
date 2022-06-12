package com.lightricks.feedexercise.network

interface UrlProvider {
    val url: String
}

/**
 * Base url provider for Retrofit
 */
class RetrofitBaseUrlProvider : UrlProvider {
    override val url: String = " https://assets.swishvideoapp.com/"
}

/**
 * Base url provider for Image Loading
 */
class ThumbnailImageBaseUrlProvider : UrlProvider {
    override val url: String = " https://assets.swishvideoapp.com/Android/demo/catalog/thumbnails/"
}
