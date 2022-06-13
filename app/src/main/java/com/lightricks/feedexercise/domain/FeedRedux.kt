package com.lightricks.feedexercise.domain

import com.lightricks.feedexercise.data.FeedItem
import com.lightricks.feedexercise.util.mvi.Return
import com.lightricks.feedexercise.util.mvi.pure
import com.lightricks.feedexercise.util.mvi.withEffect

object FeedRedux {

    /**
     * Feature State
     */
    sealed class State {
        object Loading : State()

        data class Error(
            val failure: FeedError
        ) : State()

        data class Content(
            val feed: List<FeedItem>,
            val isRefreshing: Boolean = false,
            val failure: FeedError? = null
        ) : State()
    }

    /**
     * Messages for reducing state
     */
    sealed class Message {
        object Refresh : Message()

        sealed class FeedLoading : Message() {
            object Success : FeedLoading()
            data class Failure(val error: FeedError) : FeedLoading()
        }

        data class FeedUpdated(
            val feed: List<FeedItem>
        ) : Message()
    }

    /**
     * Side effects for performing actions that must be done asynchronously from state reduction
     */
    sealed class Effect {
        object GetFeed : Effect()
    }

    /**
     * Reduces domain states and returns reduced state accompanied with optional Side-Effects
     * @param state current domain State
     * @param msg incoming Message
     * @return reduced State with optional Side-Effects
     */
    fun reduce(state: State, msg: Message): Return<State, Effect> = when (state) {
        is State.Loading -> reduceLoading(state, msg)
        is State.Error -> reduceError(state, msg)
        is State.Content -> reduceContent(state, msg)
    }

    private fun reduceLoading(state: State.Loading, msg: Message): Return<State, Effect> = when (msg) {
        is Message.FeedUpdated -> State.Content(feed = msg.feed).pure()
        is Message.FeedLoading.Failure -> State.Error(failure = msg.error).pure()
        else -> state.pure()
    }

    private fun reduceError(state: State.Error, msg: Message): Return<State, Effect> = when (msg) {
        is Message.Refresh -> State.Loading withEffect Effect.GetFeed
        else -> state.pure()
    }

    private fun reduceContent(state: State.Content, msg: Message): Return<State, Effect> = when (msg) {
        is Message.Refresh -> when (state.isRefreshing) {
            true -> state.pure()
            false -> state.copy(
                isRefreshing = true,
                failure = null
            ) withEffect Effect.GetFeed
        }
        is Message.FeedUpdated -> state.copy(
            feed = msg.feed
        ).pure()
        is Message.FeedLoading.Success -> state.copy(
            isRefreshing = false
        ).pure()
        is Message.FeedLoading.Failure -> state.copy(
            isRefreshing = false,
            failure = msg.error
        ).pure()
        else -> state.pure()
    }
}
