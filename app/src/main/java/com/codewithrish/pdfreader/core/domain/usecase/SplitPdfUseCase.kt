package com.codewithrish.pdfreader.core.domain.usecase

import com.codewithrish.pdfreader.core.common.network.DbResultState.Error
import com.codewithrish.pdfreader.core.common.network.DbResultState.Idle
import com.codewithrish.pdfreader.core.common.network.DbResultState.Loading
import com.codewithrish.pdfreader.core.common.network.DbResultState.Success
import com.codewithrish.pdfreader.core.common.util.safeDbCall
import com.codewithrish.pdfreader.core.data.repository.PdfOperationsRepository
import com.codewithrish.pdfreader.core.model.home.Document
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class SplitPdfUseCase @Inject constructor(
    private val pdfOperationsRepository: PdfOperationsRepository
) {
    operator fun invoke(
        document: Document, selectedPages: List<Int>
    ) = channelFlow {
        send(Loading)
        val result = safeDbCall { pdfOperationsRepository.splitPdf(document, selectedPages) }
        when (result) {
            is Error -> send(Error(result.error))
            is Success -> send(Success(result.data))
            else -> send(Idle)
        }
    }
}