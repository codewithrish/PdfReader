package com.codewithrish.pdfreader.core.data.repository

import com.codewithrish.pdfreader.core.datastore.CwrPreferencesDataSource
import com.codewithrish.pdfreader.core.model.AppLanguage
import com.codewithrish.pdfreader.core.model.DarkThemeConfig
import com.codewithrish.pdfreader.core.model.ThemeBrand
import com.codewithrish.pdfreader.core.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserDataRepository {
    val userData: Flow<UserData>
    suspend fun setThemeBrand(themeBrand: ThemeBrand)
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)
    suspend fun setAppLanguage(appLanguage: AppLanguage)
}

internal class UserDataRepositoryImpl @Inject constructor(
    private val cwrPreferencesDataSource: CwrPreferencesDataSource
) : UserDataRepository {
    override val userData: Flow<UserData> =
        cwrPreferencesDataSource.userData

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        cwrPreferencesDataSource.setThemeBrand(themeBrand)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        cwrPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        cwrPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        cwrPreferencesDataSource.setShouldHideOnboarding(shouldHideOnboarding)
    }

    override suspend fun setAppLanguage(appLanguage: AppLanguage) {
        cwrPreferencesDataSource.setAppLanguage(appLanguage)
    }
}