package com.codewithrish.pdfreader.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineHeightStyle.Alignment
import androidx.compose.ui.text.style.LineHeightStyle.Trim
import androidx.compose.ui.unit.sp
import com.codewithrish.pdfreader.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

const val MANROPE = "Manrope"
val POETSEN_ONE = R.font.poetsen_one_regular

val nunitoFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont(MANROPE),
        fontProvider = provider,
    )
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont(MANROPE),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont(MANROPE),
        fontProvider = provider,
    )
)

val titleLargeFontFamily = FontFamily(
    androidx.compose.ui.text.font.Font(POETSEN_ONE)
)

val titleMediumFontFamily = FontFamily(
    androidx.compose.ui.text.font.Font(POETSEN_ONE)
)

val titleSmallFontFamily = FontFamily(
    androidx.compose.ui.text.font.Font(POETSEN_ONE)
)

val labelLargeFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont(MANROPE),
        fontProvider = provider,
        weight = FontWeight.SemiBold,
    )
)

val labelMediumFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont(MANROPE),
        fontProvider = provider,
        weight = FontWeight.Normal,
    )
)

val labelSmallFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont(MANROPE),
        fontProvider = provider,
        weight = FontWeight.Normal,
    )
)

// Default Material 3 typography values
val baseline = Typography()

val Typography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = nunitoFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = nunitoFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = nunitoFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = nunitoFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = nunitoFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = nunitoFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = nunitoFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = nunitoFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = nunitoFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = nunitoFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = nunitoFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = nunitoFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = nunitoFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = nunitoFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = nunitoFontFamily),
)

@Composable
fun materialTextStyle() = MaterialTheme.typography

//Typography Style	Default Font Size	Line Height	Weight
//Display Large	57.sp	64.sp	Normal (400)
//Display Medium	45.sp	52.sp	Normal (400)
//Display Small	36.sp	44.sp	Normal (400)
//Headline Large	32.sp	40.sp	Normal (400)
//Headline Medium	28.sp	36.sp	Normal (400)
//Headline Small	24.sp	32.sp	Normal (400)
//Title Large	22.sp	28.sp	Normal (400)
//Title Medium	16.sp	24.sp	Medium (500)
//Title Small	14.sp	20.sp	Medium (500)
//Body Large	16.sp	24.sp	Normal (400)
//Body Medium	14.sp	20.sp	Normal (400)
//Body Small	12.sp	16.sp	Normal (400)
//Label Large	14.sp	20.sp	Medium (500)
//Label Medium	12.sp	16.sp	Medium (500)
//Label Small	11.sp	16.sp	Medium (500)