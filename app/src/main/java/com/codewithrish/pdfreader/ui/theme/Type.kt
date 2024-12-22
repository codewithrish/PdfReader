package com.codewithrish.pdfreader.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.codewithrish.pdfreader.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Manrope"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Manrope"),
        fontProvider = provider,
    )
)

val titleLargeFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Manrope"),
        fontProvider = provider,
        weight = FontWeight.ExtraBold,
    )
)

val titleMediumFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Manrope"),
        fontProvider = provider,
        weight = FontWeight.Bold,
    )
)

val titleSmallFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Manrope"),
        fontProvider = provider,
        weight = FontWeight.SemiBold,
    )
)


val labelLargeFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Manrope"),
        fontProvider = provider,
        weight = FontWeight.SemiBold,
    )
)

val labelMediumFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Manrope"),
        fontProvider = provider,
        weight = FontWeight.Normal,
    )
)

val labelSmallFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Manrope"),
        fontProvider = provider,
        weight = FontWeight.Normal,
    )
)

// Default Material 3 typography values
val baseline = Typography()

val Typography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = titleLargeFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = titleMediumFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = titleSmallFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = labelLargeFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = labelMediumFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = labelSmallFontFamily),
)

@Composable
fun displayLargeTextStyle() = MaterialTheme.typography.displayLarge

@Composable
fun displayMediumTextStyle() = MaterialTheme.typography.displayMedium

@Composable
fun displaySmallTextStyle() = MaterialTheme.typography.displaySmall

@Composable
fun headlineLargeTextStyle() = MaterialTheme.typography.headlineLarge

@Composable
fun headlineMediumTextStyle() = MaterialTheme.typography.headlineMedium

@Composable
fun headlineSmallTextStyle() = MaterialTheme.typography.headlineSmall

@Composable
fun titleLargeTextStyle() = MaterialTheme.typography.titleLarge

@Composable
fun titleMediumTextStyle() = MaterialTheme.typography.titleMedium

@Composable
fun titleSmallTextStyle() = MaterialTheme.typography.titleSmall

@Composable
fun bodyLargeTextStyle() = MaterialTheme.typography.bodyLarge

@Composable
fun bodyMediumTextStyle() = MaterialTheme.typography.bodyMedium

@Composable
fun bodySmallTextStyle() = MaterialTheme.typography.bodySmall

@Composable
fun labelLargeTextStyle() = MaterialTheme.typography.labelLarge

@Composable
fun labelMediumTextStyle() = MaterialTheme.typography.labelMedium

@Composable
fun labelSmallTextStyle() = MaterialTheme.typography.labelSmall

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