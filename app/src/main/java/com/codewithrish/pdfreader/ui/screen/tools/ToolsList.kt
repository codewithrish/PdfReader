package com.codewithrish.pdfreader.ui.screen.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.designsystem.component.CwrPreviews
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.ui.screen.home.DocumentType
import com.codewithrish.pdfreader.ui.theme.PdfReaderTheme
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialShape
import com.codewithrish.pdfreader.ui.theme.materialTextStyle

@Composable
fun ToolsList(
//    tools: List<Tool>,
    onToolClick: (ToolType) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkMode = isSystemInDarkTheme()
    val tools = toolsList(isDarkMode)

    LazyVerticalGrid(
        columns = GridCells.Adaptive(112.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
//        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
//        val tools = if (isDarkMode) toolsDark else toolsLight

        items(tools.size) {index ->
            ToolsItem(
                image = tools[index].image!!,
                contentDescription = tools[index].contentDescription,
                imageSize = tools[index].imageSize,
                featureName = tools[index].featureName,
                backgroundColors = tools[index].backgroundColors,
                modifier = tools[index].modifier
                    .clickable {
                        onToolClick(tools[index].toolType)
                    },
            )
        }
    }
}

@Composable
fun ToolsItem(
    image: Int,
    contentDescription: String? = null,
    imageSize: Dp,
    featureName: String,
    backgroundColors: List<Color>,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
//            .shadow(
//                elevation = 2.dp,
//                shape = materialShape().small, // Ensure this matches the clip shape
//                spotColor = pdfRedColor.copy(alpha = 1f)
//            )
            .padding(vertical = 4.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(imageSize)
                .clip(materialShape().medium)
                .background(
//                brush = Brush.linearGradient(
//                    colors = backgroundColors,
//                    start = Offset(0f, 0f),  // Top-left corner
//                    end = Offset(1000f, 1000f) // Bottom-right corner (adjust for desired angle)
//                ),
                    color = materialColor().surfaceVariant // .copy(alpha = 0.2f)
                )
                .padding(16.dp)

        )
        CwrText(
            text = featureName,
            style = materialTextStyle().titleSmall,
            modifier = Modifier.padding(top = 8.dp),
            color = materialColor().onBackground
        )
    }
}


@Composable
fun toolsList(isDarkMode: Boolean): List<Tool> {
    val backgroundColorsLight = { listOf<Color>(Color(0xFFFFF3F3), Color(0xFFFFE4E4)) }
    val backgroundColorsDark = { listOf<Color>(Color(0xFF3D0C0C), Color(0xFF5C1B1B)) } // Dark Red
    val backgroundColorsMergeLight = { listOf<Color>(Color(0xFFEFFBFF), Color(0xFFBAEEFF)) }
    val backgroundColorsMergeDark = { listOf<Color>(Color(0xFF0B3D3B), Color(0xFF2C6F6D)) } // Dark Teal
    val backgroundColorsExcelLight = { listOf<Color>(Color(0xFFF2FFE8), Color(0xFFC7EDA8)) }
    val backgroundColorsExcelDark = { listOf<Color>(Color(0xFF2C3E50), Color(0xFF34495E)) } // Dark Blue
    val backgroundColorsTextLight = { listOf<Color>(Color(0xFFEEF5FF), Color(0xFFD6E8FF)) }
    val backgroundColorsTextDark = { listOf<Color>(Color(0xFF2C3E50), Color(0xFF34495E)) } // Dark Blue
    val backgroundColorsDeleteLight = { listOf<Color>(Color(0xFFFFF3F3), Color(0xFFFFE4E4)) }
    val backgroundColorsDeleteDark = { listOf<Color>(Color(0xFF9E2A2F), Color(0xFFD63447)) } // Dark Red
    val backgroundColorsRotateLight = { listOf<Color>(Color(0xFFFFF7EB), Color(0xFFFFE8C6)) }
    val backgroundColorsRotateDark = { listOf<Color>(Color(0xFF8E44AD), Color(0xFF9B59B6)) } // Dark Purple
    val backgroundColorsImageLight = { listOf<Color>(Color(0xFFEFFBFF), Color(0xFFBAEEFF)) }
    val backgroundColorsImageDark = { listOf<Color>(Color(0xFF34495E), Color(0xFF2C3E50)) } // Dark Blue

    return listOf(
        Tool(
            image = R.drawable.img_split,
            featureName = stringResource(R.string.split_pdf),
            toolType = ToolType.SPLIT_PDF,
            backgroundColors = if (isDarkMode) backgroundColorsDark() else backgroundColorsLight()
        ),
        Tool(
            image = R.drawable.img_merge,
            featureName = stringResource(R.string.merge_pdf),
            toolType = ToolType.MERGE_PDF,
            backgroundColors = if (isDarkMode) backgroundColorsMergeDark() else backgroundColorsMergeLight()
        ),
        Tool(
            image = R.drawable.img_excel,
            featureName = stringResource(R.string.excel_to_pdf),
            toolType = ToolType.MERGE_PDF,
            backgroundColors = if (isDarkMode) backgroundColorsExcelDark() else backgroundColorsExcelLight()
        ),
        Tool(
            image = R.drawable.img_txt,
            featureName = stringResource(R.string.text_to_pdf),
            toolType = ToolType.MERGE_PDF,
            backgroundColors = if (isDarkMode) backgroundColorsTextDark() else backgroundColorsTextLight()
        ),
        Tool(
            image = R.drawable.img_delete_pages,
            featureName = stringResource(R.string.delete_pages),
            toolType = ToolType.MERGE_PDF,
            backgroundColors = if (isDarkMode) backgroundColorsDeleteDark() else backgroundColorsDeleteLight()
        ),
        Tool(
            image = R.drawable.img_rotate_pages,
            featureName = stringResource(R.string.rotate_pages),
            toolType = ToolType.MERGE_PDF,
            backgroundColors = if (isDarkMode) backgroundColorsRotateDark() else backgroundColorsRotateLight()
        ),
        Tool(
            image = R.drawable.img_img_to_pdf,
            featureName = stringResource(R.string.image_to_pdf),
            toolType = ToolType.IMAGE_TO_PDF,
            backgroundColors = if (isDarkMode) backgroundColorsImageDark() else backgroundColorsImageLight()
        )
    )
}

data class Tool(
    val image: Int? = null,
    val contentDescription: String? = null,
    val imageSize: Dp = 80.dp,
    val featureName: String,
    val backgroundColors: List<Color>,
    val toolType: ToolType,
    val modifier: Modifier = Modifier
)

enum class ToolType(val multiSelection: Boolean, val documentType: DocumentType) {
    SPLIT_PDF(false, DocumentType.PDF),
    MERGE_PDF(true, DocumentType.PDF),
    IMAGE_TO_PDF(false, DocumentType.IMAGE),
    DEFAULT(false, DocumentType.NONE)
}

@CwrPreviews
@Composable
private fun ToolsListPrev() {
    PdfReaderTheme {
        ToolsList(onToolClick = {})
    }
}