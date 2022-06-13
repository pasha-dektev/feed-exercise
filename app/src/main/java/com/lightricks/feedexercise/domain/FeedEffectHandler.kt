package com.lightricks.feedexercise.domain

import com.lightricks.feedexercise.data.FeedRepository
import com.lightricks.feedexercise.domain.FeedRedux.Effect
import com.lightricks.feedexercise.domain.FeedRedux.Message
import com.lightricks.feedexercise.util.mvi.CoroutineEffectHandler
import com.lightricks.feedexercise.util.mvi.CoroutineMessageEmitter
import com.lightricks.feedexercise.util.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class FeedEffectHandler @Inject constructor(
    private val feedRepository: FeedRepository
) : CoroutineEffectHandler<Effect, Message>, CoroutineMessageEmitter<Message> {
    override suspend fun handle(eff: Effect): Message? {
        return when (eff) {
            Effect.GetFeed -> feedRepository.refresh().let { result ->
                when (result) {
                    is Result.Failure -> Message.FeedLoading.Failure(result.error)
                    is Result.Success -> Message.FeedLoading.Success
                }
            }
        }
    }

    override suspend fun emit(): Flow<Message> {
        return feedRepository.getFeed().map { Message.FeedUpdated(it) }
    }
}
