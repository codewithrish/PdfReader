package com.codewithrish.pdfreader.ui.screen.tools.split_pdf

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.codewithrish.pdfreader.core.common.BaseViewModel
import com.codewithrish.pdfreader.core.common.network.DbResultState
import com.codewithrish.pdfreader.core.domain.usecase.GetDocumentByIdUseCase
import com.codewithrish.pdfreader.core.domain.usecase.SplitPdfUseCase
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.model.room.toDocument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * [SplitPdfScreen]
 */

@HiltViewModel
class SplitPdfViewModel @Inject constructor(
    private val getDocumentByIdUseCase: GetDocumentByIdUseCase,
    private val splitPdfUseCase: SplitPdfUseCase,
) : BaseViewModel<SplitPdfUiState, SplitPdfUiEvent>() {
    override fun initState():SplitPdfUiState = SplitPdfUiState()

    override fun onEvent(event: SplitPdfUiEvent) {
        when (event) {
            is SplitPdfUiEvent.LoadDocument -> {
                Timber.tag("SplitPdfViewModel").d("LoadDocument")
                loadDocument(event.documentId)
            }

            is SplitPdfUiEvent.ShowDocumentPages -> {
                Timber.tag("SplitPdfViewModel").d("ShowDocumentPages")
                showDocumentPages(event.pages)
            }

            is SplitPdfUiEvent.TogglePageSelection -> {
                Timber.tag("SplitPdfViewModel").d("TogglePageSelection")
                togglePageSelection(event.pageNumber)
            }

            is SplitPdfUiEvent.ViewPage -> {
                Timber.tag("SplitPdfViewModel").d("ViewPage")
                viewPage(event.page)
            }

            SplitPdfUiEvent.ClosePage -> {
                Timber.tag("SplitPdfViewModel").d("ClosePage")
                closePage()
            }

            is SplitPdfUiEvent.SelectAllPages -> {
                Timber.tag("SplitPdfViewModel").d("SelectAllPages")
                selectAllPages(event.pages, event.selectedPages)
            }

            is SplitPdfUiEvent.SplitPdf -> {
                Timber.tag("SplitPdfViewModel").d("SplitPdf")
                splitPdf(event.document, event.selectedPages)
            }
        }
    }

    private fun loadDocument(documentId: Long) {
        viewModelScope.launch {
            getDocumentByIdUseCase(documentId).collectLatest { result ->
                when (result) {
                    is DbResultState.Error -> updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = Pair(it.errorMessage.first, result.error)
                        )
                    }
                    is DbResultState.Idle -> updateState {
                        SplitPdfUiState(isLoading = false)
                    }
                    is DbResultState.Loading -> updateState {
                        it.copy(isLoading = true)
                    }
                    is DbResultState.Success -> updateState {
                        it.copy(
                            document = result.data?.toDocument(),
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

    private fun showDocumentPages(bitmaps: List<ImageBitmap>) {
        updateState {
            it.copy(
                pages = bitmaps
            )
        }
    }

    private fun togglePageSelection(pageNumber: Int) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    selectedPages = if (it.selectedPages.contains(pageNumber)) {
                        it.selectedPages.filter { it != pageNumber }
                    } else {
                        it.selectedPages.plus(pageNumber)
                    }
                )
            }
        }
    }

    private fun viewPage(bitmap: ImageBitmap) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    viewPage = bitmap
                )
            }
        }
    }

    private fun closePage() {
        viewModelScope.launch {
            updateState {
                it.copy(
                    viewPage = null
                )
            }
        }
    }

    private fun selectAllPages(
        pages: List<ImageBitmap>,
        selectedPages: List<Int>
    ) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    selectedPages = if (pages.size == selectedPages.size) emptyList() else pages.indices.toList()
                )
            }
        }
    }

    private fun splitPdf(document: Document, selectedPages: List<Int>) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            splitPdfUseCase(document, selectedPages).collectLatest { result ->
                when (result) {
                    is DbResultState.Error -> updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = Pair(it.errorMessage.first, result.error)
                        )
                    }
                    is DbResultState.Idle -> updateState {
                        SplitPdfUiState(isLoading = false)
                    }
                    is DbResultState.Loading -> updateState {
                        it.copy(isLoading = true)
                    }
                    is DbResultState.Success -> updateState {
                        it.copy(
                            isLoading = false,
                            splitPdfDocuments = result.data.map { it1 -> it1.toDocument() }
                        )
                    }
                }
            }
        }
    }
}