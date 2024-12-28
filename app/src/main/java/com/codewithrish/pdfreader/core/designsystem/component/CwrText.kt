package com.codewithrish.pdfreader.core.designsystem.component

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

@Composable
fun CwrText(
    text: String,
//    fontWeight: FontWeight? = LocalTextStyle.current.fontWeight,
//    fontFamily: FontFamily? = LocalTextStyle.current.fontFamily,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    color: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip,
    style: TextStyle = LocalTextStyle.current,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
//        fontFamily = fontFamily,
//        fontWeight = fontWeight,
        fontSize = fontSize,
        textAlign = textAlign,
        color = color,
        maxLines = maxLines,
        minLines = minLines,
        overflow = overflow,
        style = style,
        modifier = modifier,
    )
}

@CwrPreviews
@Composable
fun CwrTextPreview() {
    CwrText(text = "Hello World")
}
