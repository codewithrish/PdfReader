package com.codewithrish.pdfreader.ui.screen.tools.merge_pdf

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dehaze
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.codewithrish.pdfreader.core.common.util.DataUnitConverter
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.helper.PdfUtils
import org.joda.time.DateTime

@Composable
fun MergePdfScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedPdfUris = remember { mutableStateListOf<Uri>() }
    var selectedPdfDocuments = remember { mutableStateListOf<Document?>() }

    val pickPdfsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        uris.forEach {  uri ->
            selectedPdfDocuments.add(PdfUtils.getDocumentFromUri(context, uri))
        }
        selectedPdfUris.addAll(uris)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
            ,
            onClick = {
                pickPdfsLauncher.launch(arrayOf("application/pdf"))
            }
        ) {
            Text("Select Multiple Files")
        }

        if (selectedPdfDocuments.isNotEmpty()) {
//            selectedPdfDocuments.filterNotNull().toMutableList()
            MyList()
        }
    }
}

@Composable
fun MyList() {
    var list1 by remember { mutableStateOf(List(5) { it }) }
    val list2 by remember { mutableStateOf(List(5) { it + 5 }) }
    val stateList = rememberLazyListState()

    var draggingItemIndex: Int? by remember {
        mutableStateOf(null)
    }

    var delta: Float by remember {
        mutableFloatStateOf(0f)
    }

    LazyColumn(
        modifier = Modifier
            .pointerInput(key1 = stateList) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        stateList.layoutInfo.visibleItemsInfo
                            .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
                            ?.also {
                                (it.contentType as? DraggableItem)?.let { draggableItem ->
                                    draggingItemIndex = draggableItem.index
                                }
                            }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        delta += dragAmount.y
                    },
                    onDragEnd = {
                        draggingItemIndex = null
                        delta = 0f
                    },
                    onDragCancel = {
                        draggingItemIndex = null
                        delta = 0f
                    },
                )
            },
        state = stateList,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(text = "Title 1", fontSize = 30.sp)
        }

        itemsIndexed(
            items = list1,
            contentType = { index, _ -> DraggableItem(index = index) },
        ) { index, item ->
            val modifier = if (draggingItemIndex == index) {
                Modifier
                    .zIndex(1f)
                    .graphicsLayer {
                        translationY = delta
                    }
            } else {
                Modifier
            }
            Item(
                modifier = modifier,
                index = item,
            )
        }

        item {
            Text(text = "Title 2", fontSize = 30.sp)
        }

        itemsIndexed(list2, key = { _, item -> item }) { _, item ->
            Item(index = item)
        }

    }
}


@Composable
private fun Item(modifier: Modifier = Modifier, index: Int) {
    Card(
        modifier = modifier
    ) {
        Text(
            "Item $index",
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}


@Composable
private fun MergeFileItem(
    modifier: Modifier = Modifier,
    document: Document,
    index: Int,
    stateList: LazyListState,
    onDrag: (offset: androidx.compose.ui.geometry.Offset) -> Unit,
    onDragMove: (dragAmount: androidx.compose.ui.geometry.Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val bitMap = PdfUtils.getFirstPdfPage(context = LocalContext.current, uri = Uri.parse(document.path))
        bitMap?.let {
            Image(
                painter = BitmapPainter(it.asImageBitmap()),
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = document.name,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
            )
            Text(
                text = "PDF . ${DateTime(document.dateTime).toString("dd MMM yyyy")} . ${DataUnitConverter.formatDataSize(document.size)}",
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
            )
        }
        // Draggable Handle
        Image(
            imageVector = Icons.Default.Dehaze,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier
                .size(24.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset -> onDrag(offset) },
                        onDrag = { _, dragAmount -> onDragMove(dragAmount) },
                        onDragEnd = { onDragEnd() }
                    )
                }
        )
    }
}

data class DraggableItem(val index: Int)