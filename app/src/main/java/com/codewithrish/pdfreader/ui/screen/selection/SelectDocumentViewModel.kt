package com.codewithrish.pdfreader.ui.screen.selection

import androidx.lifecycle.viewModelScope
import com.codewithrish.pdfreader.core.common.BaseViewModel
import com.codewithrish.pdfreader.core.common.network.DbResultState
import com.codewithrish.pdfreader.core.domain.usecase.GetAllDocumentsUseCase
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.model.room.toDocument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * [SelectDocumentScreen]
 */

@HiltViewModel
class SelectDocumentViewModel @Inject constructor(
    private val getAllDocumentsUseCase: GetAllDocumentsUseCase,
) : BaseViewModel<SelectDocumentUiState, SelectDocumentUiEvent>() {

    override fun initState(): SelectDocumentUiState = SelectDocumentUiState()

    override fun onEvent(event: SelectDocumentUiEvent) {
        when (event) {
            is SelectDocumentUiEvent.LoadDocuments -> {
                Timber.tag("SelectDocumentViewModel").d("LoadDocuments")
                loadDocuments()
            }
            is SelectDocumentUiEvent.DeleteDocument -> {
//                deleteImage(event.document)
            }
            is SelectDocumentUiEvent.OpenDocument -> {
//                openImage(event.document)
            }
            is SelectDocumentUiEvent.SelectDocument -> {
                selectDocument(event.document)
            }
            is SelectDocumentUiEvent.UnSelectDocument -> {
                unSelectDocument(event.document)
            }
        }
    }

    private fun loadDocuments() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            getAllDocumentsUseCase().collectLatest { result ->
                when (result) {
                    is DbResultState.Error -> updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = Pair(it.errorMessage.first, result.error)
                        )
                    }
                    is DbResultState.Idle -> updateState {
                        SelectDocumentUiState(isLoading = false)
                    }
                    is DbResultState.Loading -> updateState {
                        it.copy(isLoading = true)
                    }
                    is DbResultState.Success -> updateState {
                        it.copy(
                            documents = result.data.map { documentEntities ->
                                documentEntities.map { documentEntity ->
                                    documentEntity.toDocument()
                                }
                            },
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

    private fun selectDocument(document: Document) {
        updateState {
            it.copy(
                selectedDocuments = it.selectedDocuments + document
            )
        }
    }

    private fun unSelectDocument(document: Document) {
        updateState {
            it.copy(
                selectedDocuments = it.selectedDocuments - document
            )
        }
    }
}