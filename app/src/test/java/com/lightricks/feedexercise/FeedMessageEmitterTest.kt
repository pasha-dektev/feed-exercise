package com.lightricks.feedexercise

import com.lightricks.feedexercise.data.FeedRepository
import com.lightricks.feedexercise.domain.FeedEffectHandler
import com.lightricks.feedexercise.domain.FeedRedux.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
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
class FeedMessageEmitterTest {

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
    fun getFeed_messageFeedUpdated() = runTest(dispatcher) {
        whenever(repoMock.getFeed()).doReturn(flowOf(emptyList()))
        feedEffectHandler.emit().flowOn(dispatcher).single().also { message ->
            Assert.assertEquals(message, Message.FeedUpdated(emptyList()))
        }
    }
}
