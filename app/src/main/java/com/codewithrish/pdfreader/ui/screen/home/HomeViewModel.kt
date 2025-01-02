package com.codewithrish.pdfreader.ui.screen.home

import androidx.lifecycle.viewModelScope
import com.codewithrish.pdfreader.core.common.BaseViewModel
import com.codewithrish.pdfreader.core.common.network.DbResultState
import com.codewithrish.pdfreader.core.domain.usecase.CheckFileExistsUseCase
import com.codewithrish.pdfreader.core.domain.usecase.DeleteDocumentByUriUseCase
import com.codewithrish.pdfreader.core.domain.usecase.DeleteDocumentUseCase
import com.codewithrish.pdfreader.core.domain.usecase.GetAllDocumentsUseCase
import com.codewithrish.pdfreader.core.domain.usecase.LoadFilesToDbUseCase
import com.codewithrish.pdfreader.core.domain.usecase.UpdateBookmarkStatusUseCase
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.model.room.toDocument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllDocumentsUseCase: GetAllDocumentsUseCase,
    private val deleteDocumentUseCase: DeleteDocumentUseCase,
    private val updateBookmarkStatusUseCase: UpdateBookmarkStatusUseCase,
    private val loadFilesUseCase: LoadFilesToDbUseCase,
    private val checkFileExistsUseCase: CheckFileExistsUseCase,
    private val deleteDocumentByUriUseCase: DeleteDocumentByUriUseCase,
) : BaseViewModel<HomeUiState, HomeUiEvent>() {

    override fun initState(): HomeUiState = HomeUiState()

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnDocumentsLoadInDb -> {
                Timber.tag("HomeViewModel").d("OnDocumentsLoadInDb")
                loadFiles()
            }
            is HomeUiEvent.OnDocumentsLoad -> {
                Timber.tag("HomeViewModel").d("OnDocumentsLoad")
                getAllDocuments()
            }
            is HomeUiEvent.OpenDocument -> {
                Timber.tag("HomeViewModel").d("OpenDocument: $event")
            }
            is HomeUiEvent.DeleteDocument -> {
                Timber.tag("HomeViewModel").d("DeleteDocument: $event")
                deleteDocument(event.document)
            }

            is HomeUiEvent.OnBookmarkClick -> {
                Timber.tag("HomeViewModel").d("OnBookmarkClick: $event")
                updateBookmarkStatus(event.id, event.isBookmarked)
            }

            is HomeUiEvent.CheckFileExists -> {
                Timber.tag("HomeViewModel").d("CheckFileExists: $event")
                checkFileExists(event.uri)
            }
        }
    }

    private fun updateBookmarkStatus(
        id: Long,
        isBookmarked: Boolean
    ) {
        viewModelScope.launch {
            updateBookmarkStatusUseCase(id, isBookmarked).collectLatest { result ->
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

    init {
        onEvent(HomeUiEvent.OnDocumentsLoad)
    }

    private fun loadFiles() = viewModelScope.launch {
        loadFilesUseCase().collectLatest { result ->
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

    private fun deleteDocument(document: Document) {
        viewModelScope.launch {
            deleteDocumentUseCase(document).collectLatest { result ->
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

    private fun getAllDocuments() {
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
                        HomeUiState(isLoading = false)
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
