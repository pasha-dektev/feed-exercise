package com.lightricks.feedexercise.util.mvi

import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.StateFlow

/**
 * MVI Engine
 * @param input channel for receiving MVI messages
 * @param output flow for consuming reduced MVI state
 */
class ReduxEngine<S, M>(
    private val input: SendChannel<M>,
    val output: StateFlow<S>,
) {
    fun send(msg: M) {
        input.trySend(msg)
    }
}
