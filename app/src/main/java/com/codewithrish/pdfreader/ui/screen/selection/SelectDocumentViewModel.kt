package com.codewithrish.pdfreader.ui.screen.selection

import androidx.lifecycle.viewModelScope
import com.codewithrish.pdfreader.core.common.BaseViewModel
import com.codewithrish.pdfreader.core.common.network.DbResultState
import com.codewithrish.pdfreader.core.domain.usecase.CheckFileExistsUseCase
import com.codewithrish.pdfreader.core.domain.usecase.DeleteDocumentByUriUseCase
import com.codewithrish.pdfreader.core.domain.usecase.GetAllDocumentsUseCase
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.model.room.toDocument
import com.codewithrish.pdfreader.ui.screen.tools.ToolType
import com.codewithrish.pdfreader.ui.util.toDocumentsFlow
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
    private val checkFileExistsUseCase: CheckFileExistsUseCase,
    private val deleteDocumentByUriUseCase: DeleteDocumentByUriUseCase,
) : BaseViewModel<SelectDocumentUiState, SelectDocumentUiEvent>() {

    override fun initState(): SelectDocumentUiState = SelectDocumentUiState()

    override fun onEvent(event: SelectDocumentUiEvent) {
        when (event) {
            is SelectDocumentUiEvent.SaveToolType -> {
                Timber.tag("SelectDocumentViewModel").d("SaveToolType")
                saveToolType(event.toolType)
            }
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
                Timber.tag("SelectDocumentViewModel").d("SelectDocument")
                selectDocument(event.document)
            }
            is SelectDocumentUiEvent.UnSelectDocument -> {
                Timber.tag("SelectDocumentViewModel").d("UnSelectDocument")
                unSelectDocument(event.document)
            }
            is SelectDocumentUiEvent.CheckFileExists -> {
                Timber.tag("SelectDocumentViewModel").d("CheckFileExists")
                checkFileExists(event.uri)
            }
        }
    }

    private fun saveToolType(toolType: ToolType) {
        updateState {
            it.copy(toolType = toolType)
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
                            documents = result.data.toDocumentsFlow(),
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

    private fun checkFileExists(uri: String) {
        viewModelScope.launch {
            checkFileExistsUseCase(uri).collectLatest { result ->
                when (result) {
                    is DbResultState.Error -> {}
                    DbResultState.Idle -> {}
                    DbResultState.Loading -> {}
                    is DbResultState.Success -> {
                        if (!result.data) {
                            deleteDocumentByUri(uri)
                        }
                    }
                }
            }
        }
    }

    private fun deleteDocumentByUri(uri: String) {
        viewModelScope.launch {
            deleteDocumentByUriUseCase(uri).collectLatest { result ->
                when (result) {
                    is DbResultState.Error -> updateState {
                        it.copy(errorMessage = Pair(it.errorMessage.first, result.error))
                    }
                    DbResultState.Idle -> {}
                    DbResultState.Loading -> {}
                    is DbResultState.Success -> {}
                }
            }
        }
    }
}