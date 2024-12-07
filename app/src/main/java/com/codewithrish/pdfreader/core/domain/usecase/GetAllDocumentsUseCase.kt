package com.codewithrish.pdfreader.core.domain.usecase

import com.codewithrish.pdfreader.core.common.network.DbResultState.*
import com.codewithrish.pdfreader.core.common.util.safeDbCall
import com.codewithrish.pdfreader.core.data.repository.DocumentsRepository
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class GetAllDocumentsUseCase @Inject constructor(
    private val documentsRepository: DocumentsRepository
) {
    operator fun invoke() = channelFlow {
        send(Loading)
        val result = safeDbCall { documentsRepository.getAllDocuments() }
        when (result) {
            is Error -> send(Error(result.error))
            is Success -> send(Success(result.data))
            else -> send(Idle)
        }
    }
}