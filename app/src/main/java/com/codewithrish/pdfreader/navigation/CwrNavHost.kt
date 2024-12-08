package com.codewithrish.pdfreader.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.codewithrish.pdfreader.ui.CwrAppState
import com.codewithrish.pdfreader.ui.screen.bookmark.bookmarksScreen
import com.codewithrish.pdfreader.ui.screen.home.HomeGraph
import com.codewithrish.pdfreader.ui.screen.home.homeSection
import com.codewithrish.pdfreader.ui.screen.document_viewer.navigateToDocumentView
import com.codewithrish.pdfreader.ui.screen.document_viewer.documentScreen
import com.codewithrish.pdfreader.ui.screen.tools.toolsSection

@Composable
fun CwrNavHost(
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
            onDocumentClick = { document ->
                navController.navigateToDocumentView(document = document)
            }
        )
        bookmarksScreen()
        toolsSection()

        documentScreen()
    }
}