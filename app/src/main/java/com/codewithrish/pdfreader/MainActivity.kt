package com.codewithrish.pdfreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.codewithrish.pdfreader.MainUiState.Loading
import com.codewithrish.pdfreader.MainUiState.Success
import com.codewithrish.pdfreader.model.DarkThemeConfig
import com.codewithrish.pdfreader.model.ThemeBrand
import com.codewithrish.pdfreader.ui.theme.PdfReaderTheme
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainUiState by mutableStateOf(Loading)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { uiState = it }
                    .collect()
            }
        }

        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                Loading -> true
                is Success -> false
            }
        }

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations, and go edge-to-edge
        // This also sets up the initial system bar style based on the platform theme
        enableEdgeToEdge()


        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)

            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
                onDispose {}
            }

            CompositionLocalProvider(
//                LocalDarkThemePreferences provides darkTheme,
//                LocalAnalyticsHelper provides NoOpAnalyticsHelper.provides(),
//                LocalLoggerProvider provides NoOpLoggerProvider.provides(),
            ) {
                PdfReaderTheme(
                    darkTheme = darkTheme,
                    androidTheme = shouldUseAndroidTheme(uiState),
                    disableDynamicTheming = shouldDisableDynamicTheming(uiState),
                ) {

//                    val pdfState = rememberVerticalPdfReaderState(
//                        resource = ResourceType.Remote("https://myreport.altervista.org/Lorem_Ipsum.pdf"),
//                        isZoomEnable = true
//                    )
//
                    val pdfState = rememberVerticalPdfReaderState(
                        resource = ResourceType.Asset(R.raw.resume),
                        isZoomEnable = true
                    )

                    Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                        VerticalPDFReader(
                            state = pdfState,
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(color = Color.Gray)
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun shouldUseDarkTheme(
    uiState: MainUiState,
): Boolean = when (uiState) {
    Loading -> isSystemInDarkTheme()
    is Success -> when (uiState.userData.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}

@Composable
private fun shouldDisableDynamicTheming(
    uiState: MainUiState,
): Boolean = when (uiState) {
    Loading -> false
    is Success -> !uiState.userData.useDynamicColor
}

@Composable
private fun shouldUseAndroidTheme(
    uiState: MainUiState,
): Boolean = when (uiState) {
    Loading -> false
    is Success -> when (uiState.userData.themeBrand) {
        ThemeBrand.DEFAULT -> false
        ThemeBrand.ANDROID -> true
    }
}

private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)