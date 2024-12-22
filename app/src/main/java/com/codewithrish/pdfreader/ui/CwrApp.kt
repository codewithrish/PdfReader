package com.codewithrish.pdfreader.ui


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.navigation.CwrNavHost
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
                    .padding(it)
            ) {
                CwrNavHost(appState = appState)
            }
        },
        bottomBar = {
            if (isTopLevelDestination) {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    appState.topLevelDestinations.forEach { destination ->
                        val selected = currentDestination?.isRouteInHierarchy(destination.baseRoute) == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = { appState.navigateToTopLevelDestination(destination) },
                            icon = {
                                Icon(
                                    imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                                    contentDescription = null
                                )
                            },
                            label = { CwrText(stringResource(destination.iconTextId)) },
                            modifier = Modifier.testTag("CwrNavItem")
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




