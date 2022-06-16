package com.lightricks.feedexercise.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lightricks.feedexercise.R
import com.lightricks.feedexercise.domain.FeedError
import com.lightricks.feedexercise.domain.FeedRedux
import com.lightricks.feedexercise.domain.FeedRedux.Effect
import com.lightricks.feedexercise.domain.FeedRedux.Message
import com.lightricks.feedexercise.domain.FeedRedux.State
import com.lightricks.feedexercise.ui.feed.model.ViewState
import com.lightricks.feedexercise.util.mvi.CoroutineEffectHandler
import com.lightricks.feedexercise.util.mvi.CoroutineMessageEmitter
import com.lightricks.feedexercise.util.mvi.LaunchReduxEngine
import com.lightricks.feedexercise.util.mvi.withEffect
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

open class FeedViewModel(
    feedEffectHandler: CoroutineEffectHandler<Effect, Message>,
    feedMessageEmitter: CoroutineMessageEmitter<Message>
) : ViewModel() {

    private val initialState = State.Loading

    private val engine = LaunchReduxEngine(
        initial = initialState withEffect Effect.GetFeed,
        reducer = FeedRedux::reduce,
        messageEmitter = feedMessageEmitter,
        effectHandler = feedEffectHandler
    )

    val viewState: StateFlow<ViewState> = engine.output
        .map { state -> state.toViewState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = initialState.toViewState()
        )

    /**
     * Converts domain state to view state
     * @return view state
     */
    private fun State.toViewState(): ViewState {
        return when (this) {
            State.Loading -> ViewState.Loading
            is State.Error -> ViewState.Error(errorMessage = mapFeedErrorText(failure))
            is State.Content -> ViewState.Content(
                isReloading = isRefreshing,
                errorMessage = failure?.let { mapFeedErrorText(it) },
                feed = feed
            )
        }
    }

    private fun mapFeedErrorText(it: FeedError) = when (it) {
        FeedError.UnknownError -> R.string.unknown_error
        FeedError.NetworkError -> R.string.network_error
    }

    fun onRefresh() {
        engine.send(Message.Refresh)
    }
}

/**
 * This class creates instances of [FeedViewModel].
 * It's not necessary to use this factory at this stage. But if we will need to inject
 * dependencies into [FeedViewModel] in the future, then this is the place to do it.
 */
@JvmSuppressWildcards
class FeedViewModelFactory @Inject constructor(
    private val feedMessageEmitter: CoroutineMessageEmitter<Message>,
    private val feedEffectHandler: CoroutineEffectHandler<Effect, Message>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            throw IllegalArgumentException("factory used with a wrong class")
        }
        @Suppress("UNCHECKED_CAST")
        return FeedViewModel(feedEffectHandler, feedMessageEmitter) as T
    }
}
