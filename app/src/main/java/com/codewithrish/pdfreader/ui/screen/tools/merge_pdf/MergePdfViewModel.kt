package com.codewithrish.pdfreader.ui.screen.tools.merge_pdf

import androidx.lifecycle.viewModelScope
import com.codewithrish.pdfreader.core.common.BaseViewModel
import com.codewithrish.pdfreader.core.common.network.DbResultState
import com.codewithrish.pdfreader.core.domain.usecase.GetDocumentsByIdsUseCase
import com.codewithrish.pdfreader.core.domain.usecase.MergePdfUseCase
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.model.room.toDocument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MergePdfViewModel @Inject constructor(
    private val getDocumentsByIdsUseCase: GetDocumentsByIdsUseCase,
    private val mergePdfUseCase: MergePdfUseCase,
) : BaseViewModel<MergePdfUiState, MergePdfUiEvent>() {
    override fun initState(): MergePdfUiState = MergePdfUiState()

    override fun onEvent(event: MergePdfUiEvent) {
        when (event) {
            is MergePdfUiEvent.LoadDocuments -> {
                Timber.tag("MergePdfViewModel").d("LoadDocuments")
                loadDocuments(event.selectedDocumentIds)
            }
            is MergePdfUiEvent.MergePdf -> {
                Timber.tag("MergePdfViewModel").d("MergePdf")
                mergePdf(event.selectedDocuments, event.outputFileName)
            }
        }
    }

    private fun loadDocuments(selectedDocumentIds: List<Long>) {
        viewModelScope.launch {
            getDocumentsByIdsUseCase(selectedDocumentIds).collectLatest { result ->
                when (result) {
                    is DbResultState.Error -> updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = Pair(it.errorMessage.first, result.error)
                        )
                    }
                    DbResultState.Idle -> updateState {
                        MergePdfUiState()
                    }
                    DbResultState.Loading -> updateState {
                        it.copy(isLoading = true)
                    }
                    is DbResultState.Success -> updateState {
                        it.copy(
                            isLoading = false,
                            selectedDocuments = result.data.map { it1 -> it1.toDocument() }
                        )
                    }
                }
            }
        }
    }

    private fun mergePdf(selectedDocumentIds: List<Document>, outputFileName: String) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            mergePdfUseCase(selectedDocumentIds.map { it.uri }, outputFileName).collectLatest { result ->
                Timber.tag("MergePdfViewModel").d("mergePdf: $result")
                when (result) {
                    is DbResultState.Error -> updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = Pair(it.errorMessage.first, result.error)
                        )
                    }
                    DbResultState.Idle -> updateState {
                        MergePdfUiState()
                    }
                    DbResultState.Loading -> updateState {
                        it.copy(isLoading = true)
                    }
                    is DbResultState.Success -> updateState {
                        it.copy(
                            isLoading = false,
                            mergedPdfDocument = result.data?.toDocument()
                        )
                    }
                }
            }
        }
    }

}
