package com.codewithrish.pdfreader.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons.Bookmark
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons.BookmarkSelected
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons.Home
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons.HomeSelected
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons.Tools
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons.ToolsSelected
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
        selectedIcon = HomeSelected,
        unselectedIcon = Home,
        iconTextId = R.string.feature_home,
        titleTextId = R.string.app_name,
        route = HomeRoute::class,
        baseRoute = HomeGraph::class,
    ),

    BOOKMARK(
        selectedIcon = BookmarkSelected,
        unselectedIcon = Bookmark,
        iconTextId = R.string.feature_bookmark,
        titleTextId = R.string.app_name,
        route = BookmarksRoute::class,
    ),

    TOOLS(
        selectedIcon = ToolsSelected,
        unselectedIcon = Tools,
        iconTextId = R.string.feature_tools,
        titleTextId = R.string.app_name,
        route = ToolsRoute::class,
        baseRoute = ToolsGraph::class,
    ),
}