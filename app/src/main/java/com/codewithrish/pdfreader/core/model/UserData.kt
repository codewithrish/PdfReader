package com.codewithrish.pdfreader.core.model

data class UserData(
    val themeBrand: ThemeBrand,
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val shouldHideOnboarding: Boolean,
    val appLanguage: AppLanguage
)
