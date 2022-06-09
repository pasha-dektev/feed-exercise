package com.lightricks.feedexercise.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * todo: add the abstract class that extents RoomDatabase here
 */

@Database(entities = [FeedItemEntity::class], version = 1, exportSchema = true)
abstract class FeedDatabase : RoomDatabase() {
    abstract fun provideFeedDao(): FeedDao
}
