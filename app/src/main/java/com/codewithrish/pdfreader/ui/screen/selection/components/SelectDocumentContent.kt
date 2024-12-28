package com.codewithrish.pdfreader.ui.screen.selection.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.components.EmptyScreenWithText
import com.codewithrish.pdfreader.ui.screen.home.DocumentType
import com.codewithrish.pdfreader.ui.screen.selection.SelectDocumentUiEvent
import com.codewithrish.pdfreader.ui.screen.tools.ToolType

@Composable
fun SelectDocumentContent(
    toolType: ToolType,
    documents: List<Document>,
    selectedItems: List<Document>,
    goToToolScreen: (ToolType, Document) -> Unit,
    onEvent: (SelectDocumentUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (toolType) {
        ToolType.SPLIT_PDF -> {
            SelectDocumentList(
                toolType = toolType,
                pdfs = documents.filter { it.mimeType == DocumentType.PDF.name },
                selectedItems = selectedItems,
                goToToolScreen = goToToolScreen,
                onEvent = onEvent,
            )
//            PdfGrid(
//                toolType = toolType,
//                pdfs = documents.filter { it.mimeType == DocumentType.PDF.name },
//                selectedItems = selectedItems,
//                goToToolScreen = goToToolScreen,
//                onEvent = onEvent,
//            )
        }
        ToolType.MERGE_PDF -> {
            SelectDocumentList(
                toolType = toolType,
                pdfs = documents.filter { it.mimeType == DocumentType.PDF.name },
                selectedItems = selectedItems,
                goToToolScreen = goToToolScreen,
                onEvent = onEvent,
            )
//            PdfGrid(
//                toolType = toolType,
//                pdfs = documents.filter { it.mimeType == DocumentType.PDF.name },
//                selectedItems = selectedItems,
//                goToToolScreen = goToToolScreen,
//                onEvent = onEvent,
//            )
        }
        ToolType.IMAGE_TO_PDF -> {
            ImageGrid(
                toolType = toolType,
                images = documents.filter { it.mimeType == DocumentType.IMAGE.name },
                selectedItems = selectedItems,
                onEvent = onEvent,
            )
        }
        ToolType.DEFAULT -> {
            EmptyScreenWithText("Document Type Not Supported")
        }
    }
}