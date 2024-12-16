package com.codewithrish.pdfreader.ui.screen.bookmark

import androidx.lifecycle.viewModelScope
import com.codewithrish.pdfreader.core.common.BaseViewModel
import com.codewithrish.pdfreader.core.common.network.DbResultState
import com.codewithrish.pdfreader.core.domain.usecase.DeleteDocumentUseCase
import com.codewithrish.pdfreader.core.domain.usecase.GetBookmarkedDocumentsUseCase
import com.codewithrish.pdfreader.core.domain.usecase.UpdateBookmarkStatusUseCase
import com.codewithrish.pdfreader.core.model.home.Document
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val getBookmarkedDocumentsUseCase: GetBookmarkedDocumentsUseCase,
    private val deleteDocumentUseCase: DeleteDocumentUseCase,
    private val updateBookmarkStatusUseCase: UpdateBookmarkStatusUseCase
) : BaseViewModel<BookmarksUiState, BookmarksUiEvent>() {
    override fun initState(): BookmarksUiState = BookmarksUiState()

    override fun onEvent(event: BookmarksUiEvent) {
        when(event) {
            is BookmarksUiEvent.LoadBookMarks -> {
                Timber.tag("BookmarksViewModel").d("LoadBookMarks")
                getBookmarkedDocuments()
            }

            is BookmarksUiEvent.OnBookmarkClick -> {
                Timber.tag("BookmarksViewModel").d("OnBookmarkClick")
                updateBookmarkStatus(event.id, event.isBookmarked)
            }

            is BookmarksUiEvent.DeleteDocument -> {
                Timber.tag("BookmarksViewModel").d("DeleteDocument")
                deleteDocument(event.document)
            }
        }
    }

    private fun getBookmarkedDocuments() {
        viewModelScope.launch {
            getBookmarkedDocumentsUseCase().collectLatest { result ->
                when (result) {
                    is DbResultState.Error -> updateState {
                        it.copy(errorMessage = Pair(it.errorMessage.first, result.error))
                    }
                    is DbResultState.Idle -> updateState {
                        BookmarksUiState() // Reset to default state
                    }
                    is DbResultState.Loading -> updateState {
                        it.copy(isLoading = true)
                    }
                    is DbResultState.Success -> updateState {
                        it.copy(documents = result.data)
                    }
                }
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
}