package com.jskaleel.sangaelakkiyangal.core.model

sealed class ResultState<out T> {
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val message: String, val code: Int? = null) : ResultState<Nothing>()
}
