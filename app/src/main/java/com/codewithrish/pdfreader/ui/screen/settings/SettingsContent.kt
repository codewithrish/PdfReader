package com.codewithrish.pdfreader.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.component.CwrCardView
import com.codewithrish.pdfreader.core.designsystem.component.CwrHorizontalDivider
import com.codewithrish.pdfreader.core.designsystem.component.CwrIcon
import com.codewithrish.pdfreader.core.designsystem.component.CwrPreviews
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialTextStyle

val cornerRadius = 16.dp

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SettingItem(
            title = "Theme",
            startIcon = CwrIcons.LightTheme,
            endIcon = CwrIcons.ChevronRight,
            shape = firstItemRoundedCornerShape()
        )
        SettingItem(
            title = "Language",
            startIcon = CwrIcons.Language,
            endIcon = CwrIcons.ChevronRight,
            shape = lastItemRoundedCornerShape()
        )
        CwrHorizontalDivider(
            color = materialColor().surface
        )
        SettingItem(
            title = "Share With Your Besties",
            startIcon = CwrIcons.Share,
//            endIcon = CwrIcons.ChevronRight,
            shape = firstItemRoundedCornerShape()
        )
        SettingItem(
            title = "Rate Us",
            startIcon = CwrIcons.StarRate,
            shape = middleItemRoundedCornerShape()
        )
        SettingItem(
            title = "Privacy Policy",
            startIcon = CwrIcons.PrivacyPolicy,
            endIcon = CwrIcons.Link
        )
        SettingItem(
            title = "App Version",
            startIcon = CwrIcons.AppVersion,
            endText = "v1.0.0",
            shape = lastItemRoundedCornerShape()
        )
    }
}

@Composable
fun SettingItem(
    startIcon: ImageVector = CwrIcons.LightTheme,
    endIcon: ImageVector? = null,
    endText: String? = null,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: RoundedCornerShape = middleItemRoundedCornerShape(),
    title: String,
    textStyle: TextStyle = materialTextStyle().bodyLarge,
    onClick: () -> Unit = {},
) {

    CwrCardView (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, end = 0.dp, bottom = 1.dp),
        shape = shape,
        onClick = onClick
    ) {

        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            CwrIcon(
                imageVector = startIcon,
                tint = iconTint,
                contentDescription = null
            )

            CwrText(
                text = title,
                style = textStyle,
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(1f),
            )

            endIcon?.let {
                CwrIcon(
                    imageVector = endIcon,
                    tint = iconTint,
                    contentDescription = null
                )
            }

            endText?.let {
                CwrText(
                    text = endText,
                    style = textStyle,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

fun singleItemRoundedCornerShape(
    topStart: Dp = cornerRadius,
    topEnd: Dp = cornerRadius,
    bottomStart: Dp = cornerRadius,
    bottomEnd: Dp = cornerRadius
) = RoundedCornerShape(
    topStart = topStart,
    topEnd = topEnd,
    bottomStart = bottomStart,
    bottomEnd = bottomEnd
)

fun firstItemRoundedCornerShape(
    topStart: Dp = cornerRadius,
    topEnd: Dp = cornerRadius,
    bottomStart: Dp = 0.dp,
    bottomEnd: Dp = 0.dp
) = RoundedCornerShape(
    topStart = topStart,
    topEnd = topEnd,
    bottomStart = bottomStart,
    bottomEnd = bottomEnd
)

fun middleItemRoundedCornerShape(
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomStart: Dp = 0.dp,
    bottomEnd: Dp = 0.dp
) = RoundedCornerShape(
    topStart = topStart,
    topEnd = topEnd,
    bottomStart = bottomStart,
    bottomEnd = bottomEnd
)

fun lastItemRoundedCornerShape(
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomStart: Dp = cornerRadius,
    bottomEnd: Dp = cornerRadius
) = RoundedCornerShape(
    topStart = topStart,
    topEnd = topEnd,
    bottomStart = bottomStart,
    bottomEnd = bottomEnd
)

@CwrPreviews
@Composable
private fun SettingsContentPreview() {
    SettingsContent()
}

@CwrPreviews
@Composable
private fun SettingItemPreview() {
    Column {
        SettingItem(
            title = "Choose Your Vibe: Dark Side or Light Side?",
            startIcon = CwrIcons.DarkTheme,
            endIcon = CwrIcons.ChevronRight,
            textStyle = materialTextStyle().bodySmall,
            shape = firstItemRoundedCornerShape()
        )
        SettingItem(
            title = "Speak Your Mind (or Someone Else’s)",
            startIcon = CwrIcons.Language,
            endIcon = CwrIcons.ChevronRight,
            textStyle = materialTextStyle().bodySmall,
        )
        SettingItem(
            title = "Brag About This App to Your Friends",
            startIcon = CwrIcons.Share,
            endIcon = CwrIcons.ChevronRight,
            shape = lastItemRoundedCornerShape(),
            textStyle = materialTextStyle().bodySmall,
        )
    }
}