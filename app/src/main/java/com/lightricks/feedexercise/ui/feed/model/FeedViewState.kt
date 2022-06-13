package com.lightricks.feedexercise.ui.feed.model

import androidx.annotation.StringRes
import com.lightricks.feedexercise.data.FeedItem

sealed class ViewState {
    object Loading : ViewState()

    data class Error(
        @StringRes val errorMessage: Int
    ) : ViewState()

    data class Content(
        val isReloading: Boolean = false,
        @StringRes val errorMessage: Int? = null,
        val feed: List<FeedItem>
    ) : ViewState()
}
