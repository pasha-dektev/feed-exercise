package com.lightricks.feedexercise.util.mvi

/**
 *  Alias for MVI reduction
 */
typealias ComplexReducer<State, Msg, Eff> = (state: State, msg: Msg) -> Return<State, Eff>
