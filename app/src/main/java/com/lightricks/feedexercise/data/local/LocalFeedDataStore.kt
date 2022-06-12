package com.lightricks.feedexercise.data.local

import com.lightricks.feedexercise.data.FeedItem

interface LocalFeedDataStore {
    suspend fun saveFeed(feed: List<FeedItem>)
}
