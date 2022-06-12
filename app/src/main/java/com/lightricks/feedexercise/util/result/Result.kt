package com.lightricks.feedexercise.util.result

/**
 * Result monad with:
 * */
sealed class Result<out E : Any, out T : Any> {

    /**
     * Success state implementation.
     * */
    data class Success<out T : Any>(val value: T) : Result<Nothing, T>()

    /**
     * Failure state implementation.
     * */
    data class Failure<out E : Any>(val error: E) : Result<E, Nothing>()
}

fun <T : Any> T.toSuccessResult() = Result.Success(this)
fun <E : Any> E.toFailureResult() = Result.Failure(this)
