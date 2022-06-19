package com.lightricks.feedexercise

import com.lightricks.feedexercise.data.FeedItem
import com.lightricks.feedexercise.domain.FeedError
import com.lightricks.feedexercise.domain.FeedRedux.Effect
import com.lightricks.feedexercise.domain.FeedRedux.Message
import com.lightricks.feedexercise.domain.FeedRedux.State
import com.lightricks.feedexercise.domain.FeedRedux.reduce
import com.lightricks.feedexercise.util.mvi.pure
import com.lightricks.feedexercise.util.mvi.withEffect
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FeedReduxTest {

    // STATE LOADING
    @Test
    fun stateLoading_messageFeedUpdated_stateContent() {
        reduce(State.Loading, Message.FeedUpdated(emptyList())).let {
            Assert.assertEquals(it, State.Content(emptyList()).pure<State, Effect>())
        }
    }

    @Test
    fun stateLoading_messageFeedLoadingFailure_stateError() {
        reduce(State.Loading, Message.FeedLoading.Failure(FeedError.NetworkError)).let {
            Assert.assertEquals(it, State.Error(FeedError.NetworkError).pure<State, Effect>())
        }
    }

    @Test
    fun stateLoading_messageFeedLoadingSuccess_stateLoading() {
        reduce(State.Loading, Message.FeedLoading.Success).let {
            Assert.assertEquals(it, State.Loading.pure<State, Effect>())
        }
    }

    @Test
    fun stateLoading_messageRefresh_stateLoading() {
        reduce(State.Loading, Message.Refresh).let {
            Assert.assertEquals(it, State.Loading.pure<State, Effect>())
        }
    }

    // STATE ERROR
    @Test
    fun stateError_messageRefresh_stateLoading() {
        reduce(State.Error(FeedError.NetworkError), Message.Refresh).let {
            Assert.assertEquals(it, State.Loading withEffect Effect.GetFeed)
        }
    }

    @Test
    fun stateError_messageFeedLoadingSuccess_stateError() {
        reduce(State.Error(FeedError.NetworkError), Message.FeedLoading.Success).let {
            Assert.assertEquals(it, State.Error(FeedError.NetworkError).pure<State, Effect>())
        }
    }

    @Test
    fun stateError_messageFeedLoadingFailure_stateError() {
        reduce(State.Error(FeedError.NetworkError), Message.FeedLoading.Failure(FeedError.NetworkError)).let {
            Assert.assertEquals(it, State.Error(FeedError.NetworkError).pure<State, Effect>())
        }
    }

    @Test
    fun stateError_messageFeedUpdated_stateError() {
        reduce(State.Error(FeedError.NetworkError), Message.FeedUpdated(emptyList())).let {
            Assert.assertEquals(it, State.Error(FeedError.NetworkError).pure<State, Effect>())
        }
    }

    // STATE CONTENT
    @Test
    fun stateContent_messageRefresh_stateContent() {
        reduce(State.Content(emptyList()), Message.Refresh).let {
            Assert.assertEquals(it, State.Content(emptyList(), isRefreshing = true) withEffect Effect.GetFeed)
        }
        reduce(State.Content(emptyList(), failure = FeedError.NetworkError), Message.Refresh).let {
            Assert.assertEquals(it, State.Content(emptyList(), isRefreshing = true) withEffect Effect.GetFeed)
        }
        reduce(State.Content(emptyList(), isRefreshing = true), Message.Refresh).let {
            Assert.assertEquals(it, State.Content(emptyList(), isRefreshing = true).pure<State, Effect>())
        }
    }

    @Test
    fun stateContent_messageFeedUpdated_stateContent() {
        val feed = listOf(FeedItem("1", "thumbnailUrl", false))
        reduce(State.Content(emptyList()), Message.FeedUpdated(feed)).let {
            Assert.assertEquals(it, State.Content(feed).pure<State, Effect>())
        }
    }

    @Test
    fun stateContent_messageFeedLoadingSuccess_stateContent() {
        reduce(State.Content(emptyList(), isRefreshing = true), Message.FeedLoading.Success).let {
            Assert.assertEquals(it, State.Content(emptyList(), isRefreshing = false).pure<State, Effect>())
        }
    }

    @Test
    fun stateContent_messageFeedLoadingFailure_stateContent() {
        reduce(State.Content(emptyList(), isRefreshing = true), Message.FeedLoading.Failure(FeedError.NetworkError))
            .let {
                Assert.assertEquals(
                    it,
                    State.Content(emptyList(), isRefreshing = false, failure = FeedError.NetworkError)
                        .pure<State, Effect>()
                )
            }
    }
}
