package com.codewithrish.pdfreader.ui.screen.settings

import androidx.lifecycle.viewModelScope
import com.codewithrish.pdfreader.core.common.BaseViewModel
import com.codewithrish.pdfreader.core.data.repository.UserDataRepository
import com.codewithrish.pdfreader.core.model.AppLanguage
import com.codewithrish.pdfreader.core.model.DarkThemeConfig
import com.codewithrish.pdfreader.core.model.ThemeBrand
import com.codewithrish.pdfreader.core.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : BaseViewModel<SettingsUiState, SettingsUiEvent>() {
    override fun initState(): SettingsUiState = SettingsUiState()

    override fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.UpdateDarkThemeConfig -> {
                Timber.tag("SettingsViewModel").d("UpdateDarkThemeConfig: ${event.darkThemeConfig}")
                updateDarkThemeConfig(event.darkThemeConfig)
            }
            is SettingsUiEvent.UpdateDynamicColorPreference -> {
                Timber.tag("SettingsViewModel").d("UpdateDynamicColorPreference: ${event.useDynamicColor}")
                updateDynamicColorPreference(event.useDynamicColor)
            }
            is SettingsUiEvent.UpdateThemeBrand -> {
                Timber.tag("SettingsViewModel").d("UpdateThemeBrand: ${event.themeBrand}")
                updateThemeBrand(event.themeBrand)
            }
            is SettingsUiEvent.UpdateAppLanguage -> {
                Timber.tag("SettingsViewModel").d("UpdateAppLanguage: ${event.appLanguage}")
                updateAppLanguage(event.appLanguage)
            }
            is SettingsUiEvent.AppVersionClick -> {
                Timber.tag("SettingsViewModel").d("AppVersionClick")
                updateAppVersion(event.appVersion)
            }
            SettingsUiEvent.PrivacyPolicyClick -> {
                Timber.tag("SettingsViewModel").d("PrivacyPolicyClick")
            }
            SettingsUiEvent.RateUsClick -> {
                Timber.tag("SettingsViewModel").d("RateUsClick")
            }
            SettingsUiEvent.ShareAppClick -> {
                Timber.tag("SettingsViewModel").d("ShareAppClick")
            }
        }
    }

    init {
        loadInitialTheme()
    }

    private fun loadInitialTheme() {
        viewModelScope.launch {
            val userData: UserData =  userDataRepository.userData.first()
            updateState {
                it.copy(
                    brand = userData.themeBrand,
                    useDynamicColor = userData.useDynamicColor,
                    darkThemeConfig = userData.darkThemeConfig,
                    appLanguage = userData.appLanguage
                )
            }
        }
    }

    fun updateThemeBrand(themeBrand: ThemeBrand) {
        viewModelScope.launch {
            userDataRepository.setThemeBrand(themeBrand)
            updateState { it.copy(brand = themeBrand) }
        }
    }

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            userDataRepository.setDarkThemeConfig(darkThemeConfig)
            updateState { it.copy(darkThemeConfig = darkThemeConfig) }
        }
    }

    fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        viewModelScope.launch {
            userDataRepository.setDynamicColorPreference(useDynamicColor)
            updateState { it.copy(useDynamicColor = useDynamicColor) }
        }
    }

    fun updateAppLanguage(appLanguage: AppLanguage) {
        viewModelScope.launch {
            userDataRepository.setAppLanguage(appLanguage)
            updateState { it.copy(appLanguage = appLanguage) }
        }
    }

    private fun updateAppVersion(appVersion: String?) {
        viewModelScope.launch {
            updateState { it.copy(appVersion = appVersion) }
        }
    }
}