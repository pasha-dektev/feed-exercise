package com.lightricks.feedexercise.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFeed(feed: List<FeedItemEntity>)

    @Query("DELETE FROM ${FeedItemEntity.TABLE_NAME}")
    suspend fun deleteAllFeedItems()

    @Query("SELECT * FROM ${FeedItemEntity.TABLE_NAME}")
    fun getFeed(): Flow<List<FeedItemEntity>>

    @Query("SELECT COUNT(*) FROM ${FeedItemEntity.TABLE_NAME}")
    suspend fun getFeedCount(): Int
}
