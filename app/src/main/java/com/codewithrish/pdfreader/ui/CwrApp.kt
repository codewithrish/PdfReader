package com.codewithrish.pdfreader.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.codewithrish.pdfreader.core.designsystem.component.CwrNavigationDefaults
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.navigation.CwrNavHost
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialTextStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlin.reflect.KClass

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CwrApp(
    appState: CwrAppState,
    modifier: Modifier = Modifier,
) {
    val currentDestination = appState.currentDestination
    val isTopLevelDestination = currentDestination?.route in appState.topLevelDestinations.map { it.route.qualifiedName }

    Scaffold(
        content = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding()))
            ) {
                CwrNavHost(appState = appState)
            }
        },
        bottomBar = {
            if (isTopLevelDestination) {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .height(56.dp)
                        .shadow(elevation = 8.dp),
                    containerColor = materialColor().background,
                ) {
                    appState.topLevelDestinations.forEach { destination ->
                        val selected = currentDestination?.isRouteInHierarchy(destination.baseRoute) == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = { appState.navigateToTopLevelDestination(destination) },
                            icon = {
                                Icon(
                                    imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                CwrText(
                                    stringResource(destination.iconTextId),
                                    style = materialTextStyle().titleSmall,
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CwrNavigationDefaults.navigationSelectedItemColor(),
                                unselectedIconColor = CwrNavigationDefaults.navigationContentColor(),
                                selectedTextColor = CwrNavigationDefaults.navigationSelectedItemColor(),
                                unselectedTextColor = CwrNavigationDefaults.navigationContentColor(),
                                indicatorColor = CwrNavigationDefaults.navigationIndicatorColor(),
                            ),
                            modifier = Modifier
                                .testTag("CwrNavItem")
                                .padding(0.dp),
                            interactionSource = remember { object : MutableInteractionSource {
                                override suspend fun emit(interaction: Interaction) {}
                                override fun tryEmit(interaction: Interaction): Boolean = true
                                override val interactions: Flow<Interaction>
                                    get() = emptyFlow()
                            } }
                        )
                    }
                }
            }
        }
    )
}

@SuppressLint("RestrictedApi")
private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } == true





