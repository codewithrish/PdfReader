package com.codewithrish.pdfreader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithrish.pdfreader.MainUiState.Loading
import com.codewithrish.pdfreader.MainUiState.Success
import com.codewithrish.pdfreader.core.common.network.DbResultState
import com.codewithrish.pdfreader.core.data.repository.UserDataRepository
import com.codewithrish.pdfreader.core.domain.usecase.DeleteDocumentUseCase
import com.codewithrish.pdfreader.core.domain.usecase.GetAllDocumentsUseCase
import com.codewithrish.pdfreader.core.domain.usecase.LoadFilesToDbUseCase
import com.codewithrish.pdfreader.core.model.UserData
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.model.room.toDocument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    private val loadFilesToDbUseCase: LoadFilesToDbUseCase,
    private val getAllDocumentsUseCase: GetAllDocumentsUseCase,
    private val deleteDocumentUseCase: DeleteDocumentUseCase,
) : ViewModel() {
    val uiState: StateFlow<MainUiState> = userDataRepository.userData.map {
        Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    private val _documentsState = MutableStateFlow<List<Document>?>(null)
    val documentsState: StateFlow<List<Document>?> = _documentsState

    init {
        loadFilesToDb()
        getAllDocuments()
    }

    private fun loadFilesToDb() = viewModelScope.launch {
        loadFilesToDbUseCase().collectLatest { result ->
            when (result) {
                is DbResultState.Error -> {}
                DbResultState.Idle -> {}
                DbResultState.Loading -> {}
                is DbResultState.Success -> {}
            }
        }
    }

    private fun getAllDocuments() {
        viewModelScope.launch {
            getAllDocumentsUseCase().collectLatest { result ->
                when (result) {
                    is DbResultState.Error -> {}
                    is DbResultState.Idle -> {}
                    is DbResultState.Loading -> {}
                    is DbResultState.Success -> {
                        result.data.map {
                            _documentsState.value = it.map { documentEntity -> documentEntity.toDocument() }
                        }
                    }
                }
            }
        }
    }

    fun deleteDocument(document: Document) {
        viewModelScope.launch {
            deleteDocumentUseCase(document).collectLatest { result ->
                when (result) {
                    is DbResultState.Error -> {}
                    DbResultState.Idle -> {}
                    DbResultState.Loading -> {}
                    is DbResultState.Success -> {}
                }
            }
        }
    }
}

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(val userData: UserData) : MainUiState
}