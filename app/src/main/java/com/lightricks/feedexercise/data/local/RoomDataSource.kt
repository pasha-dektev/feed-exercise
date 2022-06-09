package com.lightricks.feedexercise.data.local

import com.lightricks.feedexercise.data.FeedItem
import com.lightricks.feedexercise.data.toFeedItem
import com.lightricks.feedexercise.data.toFeedItemEntity
import com.lightricks.feedexercise.database.FeedDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RoomDataSource(
    private val dao: FeedDao
): LocalFeedDataSource, LocalFeedDataStore {

    override suspend fun saveFeed(feed: List<FeedItem>) {
        dao.saveFeed(feed.map { it.toFeedItemEntity() })
    }

    override suspend fun getFeed(): Flow<List<FeedItem>> {
        return dao.getFeed().map { feed ->
            feed.map { it.toFeedItem() }
        }
    }
}
