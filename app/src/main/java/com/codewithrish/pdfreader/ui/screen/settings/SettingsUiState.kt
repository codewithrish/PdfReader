package com.codewithrish.pdfreader.ui.screen.settings

import com.codewithrish.pdfreader.core.model.AppLanguage
import com.codewithrish.pdfreader.core.model.DarkThemeConfig
import com.codewithrish.pdfreader.core.model.ThemeBrand

data class SettingsUiState(
    val isLoading: Boolean = true,
    val brand: ThemeBrand = ThemeBrand.DEFAULT,
    val useDynamicColor: Boolean = false,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val appLanguage: AppLanguage = AppLanguage.ENGLISH,
    val appVersion: String? = null
)
