package com.lightricks.feedexercise.network

import retrofit2.Response
import retrofit2.http.GET

interface FeedApiService {
    @GET("Android/demo/feed.json")
    suspend fun getFeed(): Response<Feed>
}
