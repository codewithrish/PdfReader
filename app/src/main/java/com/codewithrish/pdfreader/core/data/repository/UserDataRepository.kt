package com.codewithrish.pdfreader.core.data.repository

import com.codewithrish.pdfreader.model.DarkThemeConfig
import com.codewithrish.pdfreader.model.ThemeBrand
import com.codewithrish.pdfreader.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>
    suspend fun setThemeBrand(themeBrand: ThemeBrand)
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)
}