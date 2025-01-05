package com.codewithrish.pdfreader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithrish.pdfreader.MainUiState.Loading
import com.codewithrish.pdfreader.MainUiState.Success
import com.codewithrish.pdfreader.core.data.repository.UserDataRepository
import com.codewithrish.pdfreader.core.model.DarkThemeConfig
import com.codewithrish.pdfreader.core.model.ThemeBrand
import com.codewithrish.pdfreader.core.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
) : ViewModel() {
    val uiState: StateFlow<MainUiState> = userDataRepository.userData.map {
        Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(val userData: UserData) : MainUiState {
        override val shouldDisableDynamicTheming = !userData.useDynamicColor

        override val shouldUseAndroidTheme: Boolean = when (userData.themeBrand) {
            ThemeBrand.DEFAULT -> false
            ThemeBrand.ANDROID -> true
        }

        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) =
            when (userData.darkThemeConfig) {
                DarkThemeConfig.FOLLOW_SYSTEM -> isSystemDarkTheme
                DarkThemeConfig.LIGHT -> false
                DarkThemeConfig.DARK -> true
            }

        override val currentAppLanguage: String = userData.appLanguage.languageCode
    }

    /**
     * Returns `true` if the state wasn't loaded yet and it should keep showing the splash screen.
     */
    fun shouldKeepSplashScreen() = this is Loading

    /**
     * Returns `true` if the dynamic color is disabled.
     */
    val shouldDisableDynamicTheming: Boolean get() = true

    /**
     * Returns `true` if the Android theme should be used.
     */
    val shouldUseAndroidTheme: Boolean get() = false

    /**
     * Returns `true` if dark theme should be used.
     */
    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = isSystemDarkTheme

    val currentAppLanguage: String get() = "en"
}
