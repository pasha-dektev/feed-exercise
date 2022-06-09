package com.lightricks.feedexercise.data

import com.lightricks.feedexercise.database.FeedItemEntity
import com.lightricks.feedexercise.network.Feed
import com.lightricks.feedexercise.network.Template
import com.lightricks.feedexercise.network.UrlProvider

internal fun Template.toFeedItem(
    thumbnailBaseUrlProvider: UrlProvider
): FeedItem {
    return FeedItem(
        id = id,
        thumbnailUrl = "${thumbnailBaseUrlProvider.url}$thumbnailUrl",
        isPremium = isPremium
    )
}

internal fun Feed.toFeed(
    thumbnailBaseUrlProvider: UrlProvider
): List<FeedItem> {
    return templates.map { it.toFeedItem(thumbnailBaseUrlProvider) }
}

internal fun FeedItemEntity.toFeedItem(): FeedItem {
    return FeedItem(
        id = id,
        thumbnailUrl = thumbnailUrl,
        isPremium = isPremium
    )
}

internal fun FeedItem.toFeedItemEntity(): FeedItemEntity {
    return FeedItemEntity(
        id = id,
        isPremium = isPremium,
        thumbnailUrl = thumbnailUrl
    )
}
