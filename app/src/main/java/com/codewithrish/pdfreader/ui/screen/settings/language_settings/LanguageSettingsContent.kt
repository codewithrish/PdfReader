package com.codewithrish.pdfreader.ui.screen.settings.language_settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.component.CwrCardView
import com.codewithrish.pdfreader.core.designsystem.component.CwrPreviews
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.model.AppLanguage
import com.codewithrish.pdfreader.ui.screen.settings.SettingsUiEvent
import com.codewithrish.pdfreader.ui.screen.settings.SettingsUiState
import com.codewithrish.pdfreader.ui.screen.settings.firstItemRoundedCornerShape
import com.codewithrish.pdfreader.ui.screen.settings.lastItemRoundedCornerShape
import com.codewithrish.pdfreader.ui.screen.settings.middleItemRoundedCornerShape
import com.codewithrish.pdfreader.ui.theme.materialTextStyle

@Composable
fun LanguageSettingsContent(
    state: SettingsUiState = SettingsUiState(),
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LanguageSettingItem(
            title = "English",
            isSelected = state.appLanguage == AppLanguage.ENGLISH,
            onClick = { onEvent(SettingsUiEvent.UpdateAppLanguage(AppLanguage.ENGLISH)) },
            shape = firstItemRoundedCornerShape()
        )
        LanguageSettingItem(
            title = "हिन्दी",
            isSelected = state.appLanguage == AppLanguage.HINDI,
            onClick = { onEvent(SettingsUiEvent.UpdateAppLanguage(AppLanguage.HINDI)) },
            shape = lastItemRoundedCornerShape()
        )
    }
}

@Composable
fun LanguageSettingItem(
    title: String,
    isSelected: Boolean,
    textStyle: TextStyle = materialTextStyle().bodyLarge,
    shape: RoundedCornerShape = middleItemRoundedCornerShape(),
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    CwrCardView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, end = 0.dp, bottom = 1.dp),
        shape = shape,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(start = 16.dp, end = 8.dp, top = 6.dp, bottom = 6.dp)
        ) {
            CwrText(
                text = title,
                style = textStyle,
                modifier = modifier.wrapContentHeight().weight(1f)
            )
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                modifier = modifier
            )
        }
    }
}

@CwrPreviews
@Composable
private fun LanguageSettingItemPreview() {
    LanguageSettingItem(title = "Light", isSelected = true)
}