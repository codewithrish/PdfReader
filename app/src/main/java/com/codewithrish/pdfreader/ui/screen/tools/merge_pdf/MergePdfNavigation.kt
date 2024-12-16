package com.codewithrish.pdfreader.ui.screen.tools.merge_pdf

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.codewithrish.pdfreader.navigation.wrapper.BottomUpAnimationTransition
import com.codewithrish.pdfreader.navigation.wrapper.animatedComposable
import kotlinx.serialization.Serializable

@Serializable data object MergePdfRoute

fun NavController.navigateToMergePdf() = navigate(route = MergePdfRoute)

fun NavGraphBuilder.mergePdfScreen() {
    animatedComposable<MergePdfRoute>(
        enterTransition = BottomUpAnimationTransition.enterTransition,
        exitTransition = BottomUpAnimationTransition.exitTransition,
        popEnterTransition = BottomUpAnimationTransition.popEnterTransition,
        popExitTransition = BottomUpAnimationTransition.popExitTransition,
    ) {
        MergePdfScreen()
    }
}