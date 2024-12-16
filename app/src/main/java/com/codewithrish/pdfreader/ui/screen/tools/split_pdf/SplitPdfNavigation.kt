package com.codewithrish.pdfreader.ui.screen.tools.split_pdf

import androidx.compose.material3.Surface
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.codewithrish.pdfreader.navigation.wrapper.BottomUpAnimationTransition
import com.codewithrish.pdfreader.navigation.wrapper.animatedComposable
import kotlinx.serialization.Serializable

@Serializable object SplitPdfRoute

fun NavController.navigateToSplitPdf() = navigate(route = SplitPdfRoute)

fun NavGraphBuilder.splitPdfScreen() {
    animatedComposable<SplitPdfRoute>(
        enterTransition = BottomUpAnimationTransition.enterTransition,
        exitTransition = BottomUpAnimationTransition.exitTransition,
        popEnterTransition = BottomUpAnimationTransition.popEnterTransition,
        popExitTransition = BottomUpAnimationTransition.popExitTransition,
    ) {
        Surface {
            SplitPdfScreen()
        }
    }
}