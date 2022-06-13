package com.lightricks.feedexercise.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FeedItemEntity::class], version = 1, exportSchema = true)
abstract class FeedDatabase : RoomDatabase() {
    abstract fun provideFeedDao(): FeedDao
}
