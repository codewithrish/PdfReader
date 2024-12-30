package com.codewithrish.pdfreader.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.codewithrish.pdfreader.core.designsystem.component.CwrNavigationSuiteScaffold
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.navigation.CwrNavHost
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable object DashboardGraphRoute

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CwrApp(
    rootNavController: NavHostController,
    appState: CwrAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val currentDestination = appState.currentDestination
    val isTopLevelDestination = currentDestination?.route in appState.topLevelDestinations.map { it.route.qualifiedName }

    CwrNavigationSuiteScaffold (
        navigationSuiteItems = {
            appState.topLevelDestinations.forEach { destination ->
                val selected = currentDestination
                    .isRouteInHierarchy(destination.baseRoute)
                item(
                    selected = selected,
                    onClick = { appState.navigateToTopLevelDestination(destination) },
                    icon = {
                        Icon(
                            imageVector = destination.unselectedIcon,
                            contentDescription = null,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = destination.selectedIcon,
                            contentDescription = null,
                        )
                    },
                    label = { CwrText(stringResource(destination.iconTextId)) },
                    modifier =
                    Modifier
                        .testTag("CwrNavItem")
                        .then(Modifier),
                )
            }
        },
        windowAdaptiveInfo = windowAdaptiveInfo,
    ) {
        Scaffold(
            modifier = modifier.semantics {
                testTagsAsResourceId = true
            }.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets.statusBars,
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
                    )
                    .statusBarsPadding(),
            ) {
                Box(
                    modifier = Modifier.consumeWindowInsets(WindowInsets.statusBars),
                ) {
                    CwrNavHost(rootNavController = rootNavController, appState = appState)
                }
            }
        }
    }

//    Scaffold(
//        content = {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(PaddingValues(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding()))
//            ) {
//                CwrNavHost(rootNavController = rootNavController, appState = appState)
//            }
//        },
//        bottomBar = {
//            if (isTopLevelDestination) {
//                NavigationBar(
//                    modifier = Modifier
//                        .fillMaxWidth()
////                        .height(56.dp)
//                        .shadow(elevation = 8.dp),
//                    containerColor = materialColor().background,
//                ) {
//                    appState.topLevelDestinations.forEach { destination ->
//                        val selected = currentDestination?.isRouteInHierarchy(destination.baseRoute) == true
//                        NavigationBarItem(
//                            selected = selected,
//                            onClick = { appState.navigateToTopLevelDestination(destination) },
//                            icon = {
//                                Icon(
//                                    imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(24.dp)
//                                )
//                            },
//                            label = {
//                                CwrText(
//                                    stringResource(destination.iconTextId),
//                                    style = materialTextStyle().titleSmall,
//                                )
//                            },
//                            colors = NavigationBarItemDefaults.colors(
//                                selectedIconColor = CwrNavigationDefaults.navigationSelectedItemColor(),
//                                unselectedIconColor = CwrNavigationDefaults.navigationContentColor(),
//                                selectedTextColor = CwrNavigationDefaults.navigationSelectedItemColor(),
//                                unselectedTextColor = CwrNavigationDefaults.navigationContentColor(),
//                                indicatorColor = CwrNavigationDefaults.navigationIndicatorColor(),
//                            ),
//                            modifier = Modifier
//                                .testTag("CwrNavItem")
//                                .padding(0.dp),
//                            interactionSource = remember { object : MutableInteractionSource {
//                                override suspend fun emit(interaction: Interaction) {}
//                                override fun tryEmit(interaction: Interaction): Boolean = true
//                                override val interactions: Flow<Interaction>
//                                    get() = emptyFlow()
//                            } }
//                        )
//                    }
//                }
//            }
//        }
//    )
}

@SuppressLint("RestrictedApi")
private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } == true





