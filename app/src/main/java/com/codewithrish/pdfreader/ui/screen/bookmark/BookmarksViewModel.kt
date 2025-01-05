package com.codewithrish.pdfreader.ui.screen.bookmark

import androidx.lifecycle.viewModelScope
import com.codewithrish.pdfreader.core.common.BaseViewModel
import com.codewithrish.pdfreader.core.common.network.DbResultState
import com.codewithrish.pdfreader.core.domain.usecase.DeleteDocumentUseCase
import com.codewithrish.pdfreader.core.domain.usecase.GetBookmarkedDocumentsUseCase
import com.codewithrish.pdfreader.core.domain.usecase.UpdateBookmarkStatusUseCase
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.model.room.toDocument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * [BookmarksScreen]
 */

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

            is BookmarksUiEvent.OnThreeDotClick -> {
                Timber.tag("BookmarksViewModel").d("OnThreeDotClick")
//                updateState { it.copy(isThreeDotMenuVisible = !it.isThreeDotMenuVisible) }
            }
        }
    }

    private fun getBookmarkedDocuments() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            getBookmarkedDocumentsUseCase().collectLatest { result ->
                when (result) {
                    is DbResultState.Error -> updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = Pair(it.errorMessage.first, result.error)
                        )
                    }
                    is DbResultState.Idle -> updateState {
                        BookmarksUiState()
                    }
                    is DbResultState.Loading -> updateState {
                        it.copy(isLoading = true)
                    }
                    is DbResultState.Success -> {
                        val documents: Flow<List<Document>> = result.data.map { entityList ->
                            entityList.orEmpty() // Handles null lists
                                .filterNotNull() // Filters out null DocumentEntity
                                .map { it.toDocument() } // Maps each DocumentEntity to Document
                        }
                        updateState {
                            it.copy(
                                documents = documents,
                                isLoading = false,
                            )
                        }
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