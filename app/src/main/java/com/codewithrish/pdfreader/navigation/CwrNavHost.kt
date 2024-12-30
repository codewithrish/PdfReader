package com.codewithrish.pdfreader.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.codewithrish.pdfreader.ui.CwrAppState
import com.codewithrish.pdfreader.ui.screen.bookmark.bookmarksScreen
import com.codewithrish.pdfreader.ui.screen.document_viewer.navigateToViewDocument
import com.codewithrish.pdfreader.ui.screen.home.HomeGraph
import com.codewithrish.pdfreader.ui.screen.home.homeSection
import com.codewithrish.pdfreader.ui.screen.selection.navigateToSelectDocument
import com.codewithrish.pdfreader.ui.screen.settings.navigateToSettings
import com.codewithrish.pdfreader.ui.screen.tools.toolsSection

@Composable
fun CwrNavHost(
    rootNavController: NavHostController,
    appState: CwrAppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = HomeGraph,
        modifier = modifier,
    ) {
        homeSection(
            onDocumentClick = rootNavController::navigateToViewDocument,
            onSettingsClick = rootNavController::navigateToSettings
        )
        bookmarksScreen(onDocumentClick = rootNavController::navigateToViewDocument)
        toolsSection(onToolClick = rootNavController::navigateToSelectDocument)
    }
}