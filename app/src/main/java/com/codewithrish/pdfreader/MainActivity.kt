package com.codewithrish.pdfreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.codewithrish.pdfreader.MainUiState.Loading
import com.codewithrish.pdfreader.MainUiState.Success
import com.codewithrish.pdfreader.core.analytics.AnalyticsHelper
import com.codewithrish.pdfreader.core.analytics.LocalAnalyticsHelper
import com.codewithrish.pdfreader.core.data.util.TimeZoneMonitor
import com.codewithrish.pdfreader.core.model.DarkThemeConfig
import com.codewithrish.pdfreader.core.model.ThemeBrand
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.ui.LocalTimeZone
import com.codewithrish.pdfreader.navigation.wrapper.animatedComposable
import com.codewithrish.pdfreader.ui.CwrApp
import com.codewithrish.pdfreader.ui.DashboardGraphRoute
import com.codewithrish.pdfreader.ui.permission.StoragePermissionManager
import com.codewithrish.pdfreader.ui.rememberCwrAppState
import com.codewithrish.pdfreader.ui.screen.document_viewer.documentScreen
import com.codewithrish.pdfreader.ui.screen.selection.selectDocumentScreen
import com.codewithrish.pdfreader.ui.screen.settings.settingsGraph
import com.codewithrish.pdfreader.ui.screen.tools.ToolType
import com.codewithrish.pdfreader.ui.screen.tools.merge_pdf.mergePdfScreen
import com.codewithrish.pdfreader.ui.screen.tools.merge_pdf.navigateToMergePdf
import com.codewithrish.pdfreader.ui.screen.tools.split_pdf.navigateToSplitPdf
import com.codewithrish.pdfreader.ui.screen.tools.split_pdf.splitPdfScreen
import com.codewithrish.pdfreader.ui.theme.PdfReaderTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [MainViewModel]
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var timeZoneMonitor: TimeZoneMonitor

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

        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                Loading -> true
                is Success -> false
            }
        }

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

            val appState = rememberCwrAppState(
                timeZoneMonitor = timeZoneMonitor
            )

            val currentTimeZone by appState.currentTimeZone.collectAsStateWithLifecycle()

            val rootNavController = rememberNavController()

            CompositionLocalProvider(
//                LocalDarkThemePreferences provides darkTheme,
//                LocalAnalyticsHelper provides NoOpAnalyticsHelper.provides(),
//                LocalLoggerProvider provides NoOpLoggerProvider.provides(),
                LocalAnalyticsHelper provides analyticsHelper,
                LocalTimeZone provides currentTimeZone,
            ) {
                PdfReaderTheme(
                    darkTheme = darkTheme,
                    androidTheme = shouldUseAndroidTheme(uiState),
                    disableDynamicTheming = shouldDisableDynamicTheming(uiState),
                ) {
                    StoragePermissionManager(
                        onPermissionGranted = {
                            NavHost(
                                navController = rootNavController,
                                startDestination = DashboardGraphRoute
                            ) {
                                animatedComposable<DashboardGraphRoute>() {
                                    CwrApp(rootNavController, appState)
                                }
//                                documentScreen(goBack = rootNavController::navigateUp)
//                                settingsGraph(goBack = rootNavController::navigateUp)

                                documentScreen(goBack = rootNavController::navigateUp)
                                // Select File
                                selectDocumentScreen(
                                    goBack = rootNavController::navigateUp,
                                    goToToolScreen = rootNavController::navigateToTool
                                )
                                // Tools Screen
                                splitPdfScreen(goBack = rootNavController::navigateUp)
                                mergePdfScreen(goBack = rootNavController::navigateUp)
                                // Settings
                                settingsGraph(goBack = rootNavController::navigateUp)
                            }
//                            CwrApp(appState)
                        }
                    )
                }
            }
        }
    }
}

fun NavController.navigateToTool(toolType: ToolType, document: Document? = null, selectedDocumentIds: List<Long>? = null) {
    when (toolType) {
        ToolType.SPLIT_PDF -> {
            document?.let {
                navigateToSplitPdf(document.id)
            }
        }
        ToolType.MERGE_PDF -> {
            selectedDocumentIds?.let {
                navigateToMergePdf(selectedDocumentIds)
            }
        }
        ToolType.IMAGE_TO_PDF -> {}
        ToolType.DEFAULT -> {}
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