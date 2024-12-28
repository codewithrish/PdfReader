package com.codewithrish.pdfreader.ui.screen.document_viewer

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.codewithrish.pdfreader.core.common.BaseViewModel
import com.codewithrish.pdfreader.core.model.home.Document
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * [ViewDocumentScreen]
 */

@HiltViewModel
class ViewDocumentViewModel @Inject constructor(

) : BaseViewModel<ViewDocumentUiState, ViewDocumentUiEvent>() {

    override fun initState():ViewDocumentUiState = ViewDocumentUiState()

    override fun onEvent(event: ViewDocumentUiEvent) {
        when(event) {
            is ViewDocumentUiEvent.LoadDocument -> {
                Timber.tag("ViewDocumentViewModel").d("LoadDocument")
                loadDocument(event.document, event.pages)
            }

            is ViewDocumentUiEvent.OnPageChange -> {
                Timber.tag("ViewDocumentViewModel").d("OnPageChange")
                updatePageNumber(event.pageNumber)
            }

            ViewDocumentUiEvent.ToggleTopBottomBars -> {
                Timber.tag("ViewDocumentViewModel").d("ToggleTopBottomBars")
                toggleTopBottomBars()
            }

            ViewDocumentUiEvent.ToggleBottomSheet -> {
                Timber.tag("ViewDocumentViewModel").d("ToggleBottomSheet")
                toggleBottomSheet()
            }
        }
    }

    private fun loadDocument(document: Document, pages: List<ImageBitmap>) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    document = document,
                    pages = pages
                )
            }
        }
    }

    private fun updatePageNumber(pageNumber: Pair<Int, Int>) {
        updateState {
            it.copy(
                pageNumber = pageNumber
            )
        }
    }

    private fun toggleTopBottomBars() {
        updateState {
            it.copy(
                toggleTopBottomBars = !it.toggleTopBottomBars
            )
        }
    }

    private fun toggleBottomSheet() {
        updateState {
            it.copy(
                toggleBottomSheet = !it.toggleBottomSheet
            )
        }
    }
}