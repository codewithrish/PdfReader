package com.codewithrish.pdfreader.core.data.repository

import com.codewithrish.pdfreader.core.datastore.CwrPreferencesDataSource
import com.codewithrish.pdfreader.model.DarkThemeConfig
import com.codewithrish.pdfreader.model.ThemeBrand
import com.codewithrish.pdfreader.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
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
}