package com.codewithrish.pdfreader.ui.screen.settings

import androidx.compose.material3.Surface
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.codewithrish.pdfreader.navigation.wrapper.animatedComposable
import kotlinx.serialization.Serializable


@Serializable object  SettingsGraph
@Serializable object  SettingsScreen

fun NavController.navigateToSettings() =
    navigate(route = SettingsGraph)

fun NavGraphBuilder.settingsGraph(
    goBack: () -> Unit,
) {
    navigation<SettingsGraph>(startDestination = SettingsScreen) {
        animatedComposable<SettingsScreen> {
            Surface {
                SettingsScreen(
                    goBack = goBack
                )
            }
        }
    }
}
