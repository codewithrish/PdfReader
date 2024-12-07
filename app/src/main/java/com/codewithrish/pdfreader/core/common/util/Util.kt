package com.codewithrish.pdfreader.core.common.util

import com.codewithrish.pdfreader.core.common.network.DbResultState


const val UNKNOWN_ERROR = "UNKNOWN_ERROR"

suspend fun <T> safeDbCall(apiCall: suspend () -> T): DbResultState<T> {
    return try {
        DbResultState.Loading
        val response = apiCall()
        DbResultState.Success(response) // Success case
    } catch (e: Exception) {
        DbResultState.Error(error = e.message ?: UNKNOWN_ERROR)
    } catch (e: Exception) {
        e.printStackTrace()
        // Handle any other exceptions
        DbResultState.Error(error = UNKNOWN_ERROR)
    }
}

