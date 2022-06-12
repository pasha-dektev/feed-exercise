package com.lightricks.feedexercise.data.remote

import com.lightricks.feedexercise.data.toFeed
import com.lightricks.feedexercise.domain.FeedError
import com.lightricks.feedexercise.domain.FeedSuccess
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.UrlProvider
import com.lightricks.feedexercise.util.result.toFailureResult
import com.lightricks.feedexercise.util.result.toSuccessResult
import java.net.UnknownHostException
import com.lightricks.feedexercise.util.result.Result

interface RemoteFeedDataSource {
    suspend fun getFeed(): Result<FeedError, FeedSuccess>
}

internal class RetrofitFeedDataSource(
    private val apiService: FeedApiService,
    private val thumbnailBaseUrlProvider: UrlProvider
) : RemoteFeedDataSource {
    override suspend fun getFeed(): Result<FeedError, FeedSuccess> {
        return try {
            val response = apiService.getFeed()
            return when {
                response.isSuccessful && response.body() != null -> requireNotNull(response.body())
                    .toFeed(thumbnailBaseUrlProvider)
                    .let { feed -> FeedSuccess(feed = feed) }
                    .toSuccessResult()
                else -> FeedError.NetworkError.toFailureResult()
            }
        } catch (e: UnknownHostException) {
            FeedError.NetworkError.toFailureResult()
        } catch (e: Throwable) {
            FeedError.UnknownError.toFailureResult()
        }
    }
}
