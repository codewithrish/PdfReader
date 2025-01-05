package com.codewithrish.pdfreader.ui.screen.settings

import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.codewithrish.pdfreader.navigation.wrapper.animatedComposable
import com.codewithrish.pdfreader.ui.screen.settings.language_settings.LanguageSettingsScreen
import com.codewithrish.pdfreader.ui.screen.settings.theme_settings.ThemeSettingsScreen
import kotlinx.serialization.Serializable

@Serializable object  SettingsGraph
@Serializable object  SettingsScreen

@Serializable object ThemeSettingsRoute
@Serializable object LanguageSettingsRoute

fun NavController.navigateToSettings() =
    navigate(route = SettingsGraph)

fun NavController.navigateToThemeSettings() =
    navigate(route = ThemeSettingsRoute)

fun NavController.navigateToLanguageSettings() =
    navigate(route = LanguageSettingsRoute)

fun NavGraphBuilder.settingsGraph(
    goToThemeSettings: () -> Unit,
    goToLanguageSettings: () -> Unit,
    goBack: () -> Unit,
) {
    navigation<SettingsGraph>(startDestination = SettingsScreen) {
        animatedComposable<SettingsScreen> {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsUiState by settingsViewModel.state.collectAsStateWithLifecycle()
            val settingsUiEvent = settingsViewModel.onEvent

            Surface {
                SettingsScreen(
                    state = settingsUiState,
                    onEvent = settingsUiEvent,
                    goToThemeSettings = goToThemeSettings,
                    goToLanguageSettings = goToLanguageSettings,
                    goBack = goBack
                )
            }
        }

        animatedComposable<ThemeSettingsRoute> {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsUiState by settingsViewModel.state.collectAsStateWithLifecycle()
            val settingsUiEvent = settingsViewModel.onEvent

            Surface {
                ThemeSettingsScreen(
                    state = settingsUiState,
                    onEvent = settingsUiEvent,
                    goBack = goBack
                )
            }
        }

        animatedComposable<LanguageSettingsRoute> {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsUiState by settingsViewModel.state.collectAsStateWithLifecycle()
            val settingsUiEvent = settingsViewModel.onEvent

            Surface {
                LanguageSettingsScreen(
                    state = settingsUiState,
                    onEvent = settingsUiEvent,
                    goBack = goBack
                )
            }
        }
    }
}
