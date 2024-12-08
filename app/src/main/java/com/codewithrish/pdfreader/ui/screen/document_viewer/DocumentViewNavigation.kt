package com.codewithrish.pdfreader.ui.screen.document_viewer

import androidx.compose.material3.Surface
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.navigation.serializer.createCustomNavType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable data class DocumentViewRoute(val document: Document)

fun NavController.navigateToDocumentView(
    document: Document,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = DocumentViewRoute(document = document), navOptions)

fun NavGraphBuilder.documentScreen() {
    composable<DocumentViewRoute>(
        typeMap = mapOf(typeOf<Document>() to createCustomNavType<Document>())
    ) { backStackEntry ->
//        val documentJson = backStackEntry.arguments?.getString("documentJson") ?: ""
        val document = backStackEntry.toRoute<DocumentViewRoute>()
        Surface {
            PdfViewScreen(document = document.document)
        }
    }
}