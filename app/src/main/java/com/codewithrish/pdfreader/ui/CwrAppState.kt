package com.codewithrish.pdfreader.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.codewithrish.pdfreader.core.data.util.TimeZoneMonitor
import com.codewithrish.pdfreader.navigation.TopLevelDestination
import com.codewithrish.pdfreader.ui.screen.bookmark.navigateToBookmarks
import com.codewithrish.pdfreader.ui.screen.home.navigateToHome
import com.codewithrish.pdfreader.ui.screen.tools.navigateToTools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone

@SuppressLint("ComposableNaming")
@Composable
fun rememberCwrAppState(
    timeZoneMonitor: TimeZoneMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
) : CwrAppState {
    return remember(
        navController,
        coroutineScope,
    ) {
        CwrAppState(
            timeZoneMonitor = timeZoneMonitor,
            navController = navController,
            coroutineScope = coroutineScope,
        )
    }
}

@Stable
class CwrAppState(
    timeZoneMonitor: TimeZoneMonitor,
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val currentTimeZone = timeZoneMonitor.currentTimeZone
        .stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(5_000),
            TimeZone.currentSystemDefault(),
        )

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

            when (topLevelDestination) {
                TopLevelDestination.HOME -> navController.navigateToHome(topLevelNavOptions)
                TopLevelDestination.BOOKMARK -> navController.navigateToBookmarks(topLevelNavOptions)
                TopLevelDestination.TOOLS -> navController.navigateToTools(topLevelNavOptions)
            }
        }
    }
}