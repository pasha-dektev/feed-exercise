package com.lightricks.feedexercise.data

import com.lightricks.feedexercise.data.local.LocalFeedDataStore
import com.lightricks.feedexercise.data.local.LocalFeedDataSource
import com.lightricks.feedexercise.data.remote.RemoteFeedDataSource
import com.lightricks.feedexercise.domain.FeedError
import com.lightricks.feedexercise.util.result.Result
import com.lightricks.feedexercise.util.result.toSuccessResult
import kotlinx.coroutines.flow.Flow

/**
 * This is our data layer abstraction. Users of this class don't need to know
 * where the data actually comes from (network, database or somewhere else).
 */
interface FeedRepository {
    suspend fun refresh(): Result<FeedError, Unit>
    suspend fun getFeed(): Flow<List<FeedItem>>
}

internal class FeedRepositoryImpl(
    private val localFeedDataSource: LocalFeedDataSource,
    private val localFeedDataStore: LocalFeedDataStore,
    private val remoteFeedDataSource: RemoteFeedDataSource
) : FeedRepository {

    override suspend fun refresh(): Result<FeedError, Unit> {
        return when (val result = remoteFeedDataSource.getFeed()) {
            is Result.Success -> localFeedDataStore.saveFeed(result.value.feed).let { Unit.toSuccessResult() }
            is Result.Failure -> result
        }
    }

    override suspend fun getFeed(): Flow<List<FeedItem>> {
        return localFeedDataSource.getFeed()
    }
}
