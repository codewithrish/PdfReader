package com.codewithrish.pdfreader.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.codewithrish.pdfreader.ui.CwrAppState
import com.codewithrish.pdfreader.ui.screen.bookmark.bookmarksScreen
import com.codewithrish.pdfreader.ui.screen.home.HomeBaseRoute
import com.codewithrish.pdfreader.ui.screen.home.homeSection
import com.codewithrish.pdfreader.ui.screen.document_viewer.navigateToDocumentView
import com.codewithrish.pdfreader.ui.screen.document_viewer.documentScreen
import com.codewithrish.pdfreader.ui.screen.tools.toolsScreen

@Composable
fun CwrNavHost(
    appState: CwrAppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = HomeBaseRoute,
        modifier = modifier,
    ) {
        homeSection(
            onDocumentClick = { documentJson ->
                navController.navigateToDocumentView(documentJson = documentJson)
            }
        )
        bookmarksScreen()
        toolsScreen()

        documentScreen()
    }
}