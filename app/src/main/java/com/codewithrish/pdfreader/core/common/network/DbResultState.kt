package com.codewithrish.pdfreader.core.common.network

sealed class DbResultState<out R> {
    data object Idle : DbResultState<Nothing>()
    data object Loading : DbResultState<Nothing>()
    data class Error(val error: String, val errorCode: Int? = null) : DbResultState<Nothing>()
    data class Success<out T>(val data: T) : DbResultState<T>()
}