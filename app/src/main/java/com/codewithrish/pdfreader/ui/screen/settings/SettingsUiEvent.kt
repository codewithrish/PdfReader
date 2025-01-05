package com.codewithrish.pdfreader.ui.screen.settings

import com.codewithrish.pdfreader.core.model.AppLanguage
import com.codewithrish.pdfreader.core.model.DarkThemeConfig
import com.codewithrish.pdfreader.core.model.ThemeBrand

sealed class SettingsUiEvent {
    data class UpdateThemeBrand(val themeBrand: ThemeBrand): SettingsUiEvent()
    data class UpdateDarkThemeConfig(val darkThemeConfig: DarkThemeConfig): SettingsUiEvent()
    data class UpdateDynamicColorPreference(val useDynamicColor: Boolean): SettingsUiEvent()
    data class UpdateAppLanguage(val appLanguage: AppLanguage): SettingsUiEvent()
    data object ShareAppClick: SettingsUiEvent()
    data object RateUsClick: SettingsUiEvent()
    data object PrivacyPolicyClick: SettingsUiEvent()
    data class AppVersionClick(val appVersion: String?): SettingsUiEvent()
}
