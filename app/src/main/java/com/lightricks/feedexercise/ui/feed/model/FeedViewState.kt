package com.lightricks.feedexercise.ui.feed.model

import com.lightricks.feedexercise.data.FeedItem

sealed class ViewState {
    object Loading : ViewState()

    data class Error(
        val errorMessage: String
    ) : ViewState()

    data class Content(
        val isReloading: Boolean = false,
        val errorMessage: String? = null,
        val feed: List<FeedItem>
    )
}
