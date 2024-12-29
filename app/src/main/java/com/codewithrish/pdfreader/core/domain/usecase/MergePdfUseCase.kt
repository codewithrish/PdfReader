package com.codewithrish.pdfreader.core.domain.usecase

import com.codewithrish.pdfreader.core.common.network.DbResultState.Error
import com.codewithrish.pdfreader.core.common.network.DbResultState.Idle
import com.codewithrish.pdfreader.core.common.network.DbResultState.Loading
import com.codewithrish.pdfreader.core.common.network.DbResultState.Success
import com.codewithrish.pdfreader.core.common.util.safeDbCall
import com.codewithrish.pdfreader.core.data.repository.PdfOperationsRepository
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject


class MergePdfUseCase @Inject constructor(
    private val pdfOperationsRepository: PdfOperationsRepository
) {
    operator fun invoke(
        selectedDocumentUris: List<String>, outputFileName: String
    ) = channelFlow {
        send(Loading)
        val result = safeDbCall { pdfOperationsRepository.mergePdf(selectedDocumentUris, outputFileName) }
        when (result) {
            is Error -> send(Error(result.error))
            is Success -> send(Success(result.data))
            else -> send(Idle)
        }
    }
}