package com.codewithrish.pdfreader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithrish.pdfreader.MainUiState.Loading
import com.codewithrish.pdfreader.MainUiState.Success
import com.codewithrish.pdfreader.core.data.repository.LoadFilesRepository
import com.codewithrish.pdfreader.core.data.repository.UserDataRepository
import com.codewithrish.pdfreader.core.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    private val loadFilesRepository: LoadFilesRepository
) : ViewModel() {
    val uiState: StateFlow<MainUiState> = userDataRepository.userData.map {
        Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    fun loadFiles() = viewModelScope.launch {
        loadFilesRepository.loadAllFilesToDatabase()
    }
}

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(val userData: UserData) : MainUiState
}