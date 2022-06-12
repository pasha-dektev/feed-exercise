package com.lightricks.feedexercise.util.mvi

/**
 * State and List of possible side effects
 */
data class Return<out State, out Eff>(val state: State, val effects: List<Eff> = listOf())

/**
 * Converts state to [Return] with no Side-Effects
 */
fun <State, Eff> State.pure(): Return<State, Eff> {
    return Return(this)
}

/**
 * Converts state to [Return] with one Side-Effect
 * @param effect Side-Effect
 */
infix fun <State, Eff> State.withEffect(effect: Eff): Return<State, Eff> {
    return Return(this, listOf(effect))
}
