@file:Suppress("FunctionName")

package com.lightricks.feedexercise.util.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * MVI Effect Handler
 */
fun interface EffectHandler<in Eff> {
    fun handle(eff: Eff)
}

fun <Eff> EffectHandler<Eff>.handle(effects: List<Eff>) {
    effects.forEach { effect ->
        handle(effect)
    }
}

/**
 * MVI suspend Effect Handler
 */
interface CoroutineEffectHandler<in E, out M> {
    suspend fun handle(eff: E): M?
}

fun <E, M> CoroutineEffectHandler(
    scope: CoroutineScope,
    channel: SendChannel<M>,
    block: CoroutineEffectHandler<E, M>,
): EffectHandler<E> = EffectHandler { eff: E ->
    scope.launch {
        block.handle(eff)?.let { msg ->
            channel.send(msg)
        }
    }
}

fun interface MessageEmitter {
    fun emit()
}

fun interface CoroutineMessageEmitter<out Msg> {
    suspend fun emit(): Flow<Msg>
}

fun <Msg> SubscriptionMessageEmitter(
    scope: CoroutineScope,
    channel: SendChannel<Msg>,
    block: CoroutineMessageEmitter<Msg>,
): MessageEmitter = MessageEmitter {
    scope.launch {
        block.emit().collect { msg ->
            channel.send(msg)
        }
    }
}
