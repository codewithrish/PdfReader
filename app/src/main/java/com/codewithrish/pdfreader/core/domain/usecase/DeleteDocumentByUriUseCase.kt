package com.codewithrish.pdfreader.core.domain.usecase

import com.codewithrish.pdfreader.core.common.network.DbResultState.Error
import com.codewithrish.pdfreader.core.common.network.DbResultState.Idle
import com.codewithrish.pdfreader.core.common.network.DbResultState.Loading
import com.codewithrish.pdfreader.core.common.network.DbResultState.Success
import com.codewithrish.pdfreader.core.common.util.safeDbCall
import com.codewithrish.pdfreader.core.data.repository.DocumentsRepository
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class DeleteDocumentByUriUseCase @Inject constructor(
    private val documentsRepository: DocumentsRepository
) {
    operator fun invoke(
        uri: String
    ) = channelFlow {
        send(Loading)
        val result = safeDbCall { documentsRepository.deleteByUri(uri) }
        when (result) {
            is Error -> send(Error(result.error))
            is Success -> send(Success(result.data))
            else -> send(Idle)
        }
    }
}