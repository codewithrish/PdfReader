package com.codewithrish.pdfreader.ui.screen.home

import androidx.lifecycle.viewModelScope
import com.codewithrish.pdfreader.core.common.BaseViewModel
import com.codewithrish.pdfreader.core.common.network.DbResultState
import com.codewithrish.pdfreader.core.domain.usecase.GetAllDocumentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllDocumentsUseCase: GetAllDocumentsUseCase
) : BaseViewModel<HomeUiState, HomeUiEvent>() {

    override fun initState(): HomeUiState = HomeUiState()

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnDocumentsLoad -> {
                getAllDocuments()
            }
            is HomeUiEvent.OpenDocument -> {

            }
        }
    }

    init {
        onEvent(HomeUiEvent.OnDocumentsLoad)
    }

    private fun getAllDocuments() {
        viewModelScope.launch {
            getAllDocumentsUseCase().collectLatest { result ->
                when (result) {
                    is DbResultState.Error -> updateState {
                        it.copy(errorMessage = Pair(it.errorMessage.first, result.error))
                    }
                    is DbResultState.Idle -> updateState {
                        HomeUiState() // Reset to default state
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
}