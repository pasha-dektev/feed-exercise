@file:Suppress("FunctionName")

package com.lightricks.feedexercise.util.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/** MVI Redux Engine launcher
 * @param scope coroutine scope to run reduction
 * @param initial initial MVI state
 * @param reducer reducer function to change MVI state
 * @param effectHandlerFactory factory to produce effectHandler for handling effects from state reduction
 */
fun <S, M, E> LaunchReduxEngine(
    scope: CoroutineScope,
    initial: Return<S, E>,
    reducer: ComplexReducer<S, in M, E>,
    messageEmitterFactory: (SendChannel<M>) -> MessageEmitter,
    effectHandlerFactory: (SendChannel<M>) -> EffectHandler<E>,
): ReduxEngine<S, M> {
    val messages = Channel<M>(Channel.UNLIMITED)
    val states = MutableStateFlow(initial.state)
    val effectHandler = effectHandlerFactory.invoke(messages)
    messageEmitterFactory.invoke(messages).emit()
    effectHandler.handle(initial.effects)
    scope.launch {
        for (msg in messages) {
            val prevState = states.value
            val (state, effects) = reducer.invoke(prevState, msg)
            states.value = state
            effectHandler.handle(effects)
        }
    }
    return ReduxEngine(messages, states)
}

/** MVI Redux Engine launcher extension for ViewModel */
fun <S, M, E> ViewModel.LaunchReduxEngine(
    initial: Return<S, E>,
    reducer: ComplexReducer<S, in M, E>,
    messageEmitter: CoroutineMessageEmitter<M>,
    effectHandler: CoroutineEffectHandler<E, M>,
) = LaunchReduxEngine(
    scope = viewModelScope,
    initial = initial,
    reducer = reducer,
    messageEmitterFactory = { channel ->
        SubscriptionMessageEmitter(viewModelScope, channel, messageEmitter)
    },
    effectHandlerFactory = { channel ->
        CoroutineEffectHandler(viewModelScope, channel, effectHandler)
    }
)
