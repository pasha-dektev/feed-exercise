package com.lightricks.feedexercise.data.utils

import com.google.common.truth.Truth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FlowTestObserver<T>(flow: Flow<T>, coroutineScope: CoroutineScope) {
    private val collectedItems = mutableListOf<T>()
    private val collectJob = coroutineScope.launch {
        flow.collect { collectedItems.add(it) }
    }

    fun getCurrentValue(): T? {
        return collectedItems.lastOrNull()
    }

    fun requireCurrentValue(): T {
        return getCurrentValue()!!
    }

    /**
     * Asserts that this [FlowTestObserver] received exactly the values that were sent to the flow.
     */
    fun assertExactlyValues(vararg values: T) {
        Truth.assertThat(collectedItems).containsExactlyElementsIn(values)
    }

    fun assertNoValues() {
        Truth.assertThat(collectedItems.isEmpty()).isTrue()
    }

    fun dispose() {
        collectJob.cancel()
    }
}

suspend fun <T> Flow<T>.test(coroutineScope: CoroutineScope, block: suspend FlowTestObserver<T>.() -> Unit) {
    val testObserver = FlowTestObserver(this, coroutineScope)
    block(testObserver)
    testObserver.dispose()
}
