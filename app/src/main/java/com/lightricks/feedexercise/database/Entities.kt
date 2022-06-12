package com.lightricks.feedexercise.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = FeedItemEntity.TABLE_NAME)
data class FeedItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "is_premium")
    val isPremium: Boolean,
    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String,
) {
    companion object {
        const val TABLE_NAME = "feed_item"
    }
}
