package com.codewithrish.pdfreader.ui.screen.document_viewer

import androidx.compose.material3.Surface
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import com.codewithrish.pdfreader.navigation.wrapper.animatedComposable
import kotlinx.serialization.Serializable

@Serializable data class DocumentViewRoute(val documentJson: String)

fun NavController.navigateToDocumentView(documentJson: String, navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = DocumentViewRoute(documentJson = documentJson), navOptions)

fun NavGraphBuilder.documentScreen() {
    animatedComposable<DocumentViewRoute> { backStackEntry ->
        val documentJson = backStackEntry.arguments?.getString("documentJson") ?: ""
        Surface {
            PdfViewScreen(documentJson = documentJson)
        }
    }
}