package com.codewithrish.pdfreader

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.tracing.trace
import com.codewithrish.pdfreader.MainUiState.Loading
import com.codewithrish.pdfreader.core.analytics.AnalyticsHelper
import com.codewithrish.pdfreader.core.analytics.LocalAnalyticsHelper
import com.codewithrish.pdfreader.core.data.util.TimeZoneMonitor
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.ui.LocalAppLocale
import com.codewithrish.pdfreader.core.ui.LocalTimeZone
import com.codewithrish.pdfreader.navigation.wrapper.animatedComposable
import com.codewithrish.pdfreader.ui.CwrApp
import com.codewithrish.pdfreader.ui.DashboardGraphRoute
import com.codewithrish.pdfreader.ui.permission.StoragePermissionManager
import com.codewithrish.pdfreader.ui.rememberCwrAppState
import com.codewithrish.pdfreader.ui.screen.document_viewer.documentScreen
import com.codewithrish.pdfreader.ui.screen.selection.selectDocumentScreen
import com.codewithrish.pdfreader.ui.screen.settings.navigateToLanguageSettings
import com.codewithrish.pdfreader.ui.screen.settings.navigateToThemeSettings
import com.codewithrish.pdfreader.ui.screen.settings.settingsGraph
import com.codewithrish.pdfreader.ui.screen.tools.ToolType
import com.codewithrish.pdfreader.ui.screen.tools.merge_pdf.mergePdfScreen
import com.codewithrish.pdfreader.ui.screen.tools.merge_pdf.navigateToMergePdf
import com.codewithrish.pdfreader.ui.screen.tools.split_pdf.navigateToSplitPdf
import com.codewithrish.pdfreader.ui.screen.tools.split_pdf.splitPdfScreen
import com.codewithrish.pdfreader.ui.theme.PdfReaderTheme
import com.codewithrish.pdfreader.ui.util.isSystemInDarkTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Locale
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

        // We keep this as a mutable state, so that we can track changes inside the composition.
        // This allows us to react to dark/light mode changes.
        var themeSettings by mutableStateOf(
            ThemeSettings(
                darkTheme = resources.configuration.isSystemInDarkTheme,
                androidTheme = Loading.shouldUseAndroidTheme,
                disableDynamicTheming = Loading.shouldDisableDynamicTheming,
            ),
        )

        var currentAppLanguage by mutableStateOf(Loading.currentAppLanguage)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    isSystemInDarkTheme(),
                    viewModel.uiState,
                ) { systemDark, uiState ->
                    ThemeSettings(
                        darkTheme = uiState.shouldUseDarkTheme(systemDark),
                        androidTheme = uiState.shouldUseAndroidTheme,
                        disableDynamicTheming = uiState.shouldDisableDynamicTheming,
                    ) to uiState.currentAppLanguage
                }
                    .onEach { (theme, language) ->
                        themeSettings = theme
                        currentAppLanguage = language
                    }
                    .map { it.first.darkTheme }
                    .distinctUntilChanged()
                    .collect { darkTheme ->
                        trace("cwrEdgeToEdge") {
                            enableEdgeToEdge(
                                statusBarStyle = SystemBarStyle.auto(
                                    lightScrim = android.graphics.Color.TRANSPARENT,
                                    darkScrim = android.graphics.Color.TRANSPARENT,
                                ) { darkTheme },
                                navigationBarStyle = SystemBarStyle.auto(
                                    lightScrim = lightScrim,
                                    darkScrim = darkScrim,
                                ) { darkTheme },
                            )
                        }
                    }
            }
        }

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.shouldKeepSplashScreen() }

        setContent {
            val appState = rememberCwrAppState(
                timeZoneMonitor = timeZoneMonitor
            )

            val currentTimeZone by appState.currentTimeZone.collectAsStateWithLifecycle()

            val rootNavController = rememberNavController()

            val context = LocalContext.current
            setAppLanguage(context, currentAppLanguage)

            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper,
                LocalTimeZone provides currentTimeZone,
                LocalAppLocale provides Locale(currentAppLanguage)
            ) {
                PdfReaderTheme(
                    darkTheme = themeSettings.darkTheme,
                    androidTheme = themeSettings.androidTheme,
                    disableDynamicTheming = themeSettings.disableDynamicTheming,
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
                                settingsGraph(
                                    goToThemeSettings = rootNavController::navigateToThemeSettings,
                                    goToLanguageSettings = rootNavController::navigateToLanguageSettings,
                                    goBack = rootNavController::navigateUp
                                )
                            }
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

/**
 * Class for the system theme settings.
 * This wrapping class allows us to combine all the changes and prevent unnecessary recompositions.
 */
data class ThemeSettings(
    val darkTheme: Boolean,
    val androidTheme: Boolean,
    val disableDynamicTheming: Boolean,
)


private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

fun setAppLanguage(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}