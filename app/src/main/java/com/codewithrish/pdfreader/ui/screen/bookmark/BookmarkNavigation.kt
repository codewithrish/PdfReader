package com.codewithrish.pdfreader.ui.screen.bookmark

import androidx.compose.material3.Surface
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable object BookmarksRoute

fun NavController.navigateToBookmarks(navOptions: NavOptions) =
    navigate(route = BookmarksRoute, navOptions)

fun NavGraphBuilder.bookmarksScreen(

) {
    composable<BookmarksRoute>() {
        Surface {
            BookmarksScreen()
        }
    }
}