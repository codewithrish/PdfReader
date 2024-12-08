package com.codewithrish.pdfreader.navigation

import com.codewithrish.pdfreader.R
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.ui.screen.bookmark.BookmarksRoute
import com.codewithrish.pdfreader.ui.screen.home.HomeGraph
import com.codewithrish.pdfreader.ui.screen.home.HomeRoute
import com.codewithrish.pdfreader.ui.screen.tools.ToolsGraph
import com.codewithrish.pdfreader.ui.screen.tools.ToolsRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    HOME(
        selectedIcon = CwrIcons.Upcoming,
        unselectedIcon = CwrIcons.UpcomingBorder,
        iconTextId = R.string.feature_home,
        titleTextId = R.string.app_name,
        route = HomeRoute::class,
        baseRoute = HomeGraph::class,
    ),

    BOOKMARK(
        selectedIcon = CwrIcons.Bookmarks,
        unselectedIcon = CwrIcons.BookmarksBorder,
        iconTextId = R.string.feature_bookmark,
        titleTextId = R.string.app_name,
        route = BookmarksRoute::class,
    ),

    TOOLS(
        selectedIcon = CwrIcons.Grid3x3,
        unselectedIcon = CwrIcons.Grid3x3,
        iconTextId = R.string.feature_tools,
        titleTextId = R.string.app_name,
        route = ToolsRoute::class,
        baseRoute = ToolsGraph::class,
    ),
}