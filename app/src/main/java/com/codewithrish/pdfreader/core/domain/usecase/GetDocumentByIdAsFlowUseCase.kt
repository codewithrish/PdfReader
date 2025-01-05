package com.codewithrish.pdfreader.core.domain.usecase

import com.codewithrish.pdfreader.core.common.network.DbResultState
import com.codewithrish.pdfreader.core.common.network.DbResultState.Error
import com.codewithrish.pdfreader.core.common.network.DbResultState.Idle
import com.codewithrish.pdfreader.core.common.network.DbResultState.Loading
import com.codewithrish.pdfreader.core.common.network.DbResultState.Success
import com.codewithrish.pdfreader.core.common.util.safeDbCall
import com.codewithrish.pdfreader.core.data.repository.DocumentsRepository
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class GetDocumentByIdAsFlowUseCase @Inject constructor(
    private val documentsRepository: DocumentsRepository
) {
    operator fun invoke(
        documentId: Long
    ) = channelFlow {
        send(Loading)
        val result = safeDbCall { documentsRepository.getDocumentByIdAsFlow(documentId) }
        when (result) {
            is DbResultState.Error -> send(Error(result.error))
            is Success -> send(Success(result.data))
            else -> send(Idle)
        }
    }
}