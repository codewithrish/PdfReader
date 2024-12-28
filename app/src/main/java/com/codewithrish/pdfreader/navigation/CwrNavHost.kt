package com.codewithrish.pdfreader.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.CwrAppState
import com.codewithrish.pdfreader.ui.screen.bookmark.bookmarksScreen
import com.codewithrish.pdfreader.ui.screen.home.HomeGraph
import com.codewithrish.pdfreader.ui.screen.home.homeSection
import com.codewithrish.pdfreader.ui.screen.document_viewer.navigateToViewDocument
import com.codewithrish.pdfreader.ui.screen.document_viewer.documentScreen
import com.codewithrish.pdfreader.ui.screen.settings.navigateToSettings
import com.codewithrish.pdfreader.ui.screen.settings.settingsGraph
import com.codewithrish.pdfreader.ui.screen.tools.ToolType
import com.codewithrish.pdfreader.ui.screen.tools.merge_pdf.mergePdfScreen
import com.codewithrish.pdfreader.ui.screen.tools.merge_pdf.navigateToMergePdf
import com.codewithrish.pdfreader.ui.screen.selection.navigateToSelectDocument
import com.codewithrish.pdfreader.ui.screen.selection.selectDocumentScreen
import com.codewithrish.pdfreader.ui.screen.tools.split_pdf.navigateToSplitPdf
import com.codewithrish.pdfreader.ui.screen.tools.split_pdf.splitPdfScreen
import com.codewithrish.pdfreader.ui.screen.tools.toolsSection

@Composable
fun CwrNavHost(
    appState: CwrAppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController

    // Define the `onToolClick` callback
    val onToolClick: (ToolType, Document) -> Unit = { toolType, document ->
        navController.navigateToTool(toolType, document)
    }

    NavHost(
        navController = navController,
        startDestination = HomeGraph,
        modifier = modifier,
    ) {
        homeSection(
            onDocumentClick = navController::navigateToViewDocument,
            onSettingsClick = navController::navigateToSettings
        )
        bookmarksScreen(onDocumentClick = navController::navigateToViewDocument)
        toolsSection(onToolClick = navController::navigateToSelectDocument)
        documentScreen(goBack = navController::navigateUp)
        // Select File
        selectDocumentScreen(
            goBack = navController::navigateUp,
            goToToolScreen = navController::navigateToTool
        )
        // Tools Screen
        splitPdfScreen(goBack = navController::navigateUp)
        mergePdfScreen()
        // Settings
        settingsGraph(goBack = navController::navigateUp)
    }
}

fun NavController.navigateToTool(toolType: ToolType, document: Document) {
    when (toolType) {
        ToolType.SPLIT_PDF -> navigateToSplitPdf(document.id)
        ToolType.MERGE_PDF -> navigateToMergePdf()
        ToolType.IMAGE_TO_PDF -> {}
        ToolType.DEFAULT -> {}
    }
}