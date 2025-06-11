package com.slide.test.core

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Created by Stefan Halus on 19 May 2022
 */
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val throwable: Throwable? = null) : Result<Nothing>
    object Loading : Result<Nothing>
}

fun <T> Single<T>.asResult(errorHandler:(Throwable) -> Throwable = { it } ): Observable<Result<T>> {
    return this
        .toObservable()
        .map<Result<T>> { Result.Success(it) }
        .onErrorReturn { Result.Error(errorHandler.invoke(it)) }
}


fun <T, R> Observable<Result<T>>.mapResult(mapper: (T) -> R): Observable<Result<R>> {
    return this
        .map {
            when (it) {
                is Result.Success -> Result.Success(mapper.invoke(it.data))
                is Result.Error -> Result.Error(it.throwable)
                is Result.Loading -> Result.Loading
            }
        }
}


fun <T, R> Result<T>.map(mapper: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(mapper.invoke(this.data))
        is Result.Error -> Result.Error(this.throwable)
        is Result.Loading -> Result.Loading
    }
}