package com.codewithrish.pdfreader.core.domain.usecase

import com.codewithrish.pdfreader.core.common.network.DbResultState.Error
import com.codewithrish.pdfreader.core.common.network.DbResultState.Idle
import com.codewithrish.pdfreader.core.common.network.DbResultState.Loading
import com.codewithrish.pdfreader.core.common.network.DbResultState.Success
import com.codewithrish.pdfreader.core.common.util.safeDbCall
import com.codewithrish.pdfreader.core.data.repository.FileRepository
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class LoadFilesToDbUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    operator fun invoke() = channelFlow {
        send(Loading)
        val result = safeDbCall { fileRepository.loadAllFilesToDatabase() }
        when (result) {
            is Error -> send(Error(result.error))
            is Success -> send(Success(result.data))
            else -> send(Idle)
        }
    }
}