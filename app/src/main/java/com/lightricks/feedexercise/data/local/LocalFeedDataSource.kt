package com.lightricks.feedexercise.data.local

import com.lightricks.feedexercise.data.FeedItem
import kotlinx.coroutines.flow.Flow

interface LocalFeedDataSource {
    suspend fun getFeed(): Flow<List<FeedItem>>
}
