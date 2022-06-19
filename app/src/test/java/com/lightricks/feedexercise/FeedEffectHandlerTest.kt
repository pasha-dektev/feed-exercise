package com.lightricks.feedexercise

import com.lightricks.feedexercise.data.FeedRepository
import com.lightricks.feedexercise.domain.FeedEffectHandler
import com.lightricks.feedexercise.domain.FeedError
import com.lightricks.feedexercise.domain.FeedRedux.Effect
import com.lightricks.feedexercise.domain.FeedRedux.Message
import com.lightricks.feedexercise.util.result.toFailureResult
import com.lightricks.feedexercise.util.result.toSuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class FeedEffectHandlerTest {

    private val repoMock = mock(FeedRepository::class.java)
    private val feedEffectHandler = FeedEffectHandler(repoMock)

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun effectGetFeed_refreshSuccess_messageFeedLoadingSuccess() = runTest(dispatcher) {
        whenever(repoMock.refresh()).doReturn(Unit.toSuccessResult())
        feedEffectHandler.handle(Effect.GetFeed).also { message ->
            Assert.assertEquals(message, Message.FeedLoading.Success)
        }
    }

    @Test
    fun effectGetFeed_refreshFailure_messageFeedLoadingFailure() = runTest(dispatcher) {
        whenever(repoMock.refresh()).doReturn(FeedError.NetworkError.toFailureResult())
        feedEffectHandler.handle(Effect.GetFeed).also { message ->
            Assert.assertEquals(message, Message.FeedLoading.Failure(FeedError.NetworkError))
        }
    }
}
