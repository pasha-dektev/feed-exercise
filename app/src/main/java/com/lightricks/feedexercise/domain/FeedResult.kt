package com.lightricks.feedexercise.domain

import com.lightricks.feedexercise.data.FeedItem

data class FeedSuccess(
    val feed: List<FeedItem>
)

sealed class FeedError {
    object UnknownError : FeedError()
    object NetworkError : FeedError()
}
