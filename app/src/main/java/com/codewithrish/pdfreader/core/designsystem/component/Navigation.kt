package com.codewithrish.pdfreader.core.designsystem.component

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.ui.theme.PdfReaderTheme
import com.codewithrish.pdfreader.ui.theme.materialColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun RowScope.CwrNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = CwrNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = CwrNavigationDefaults.navigationContentColor(),
            selectedTextColor = CwrNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = CwrNavigationDefaults.navigationContentColor(),
            indicatorColor = CwrNavigationDefaults.navigationIndicatorColor(),
        ),
        interactionSource = remember { NoRippleInteractionSource() }, // Disable ripple
    )
}

@Composable
fun CwrNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        contentColor = CwrNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp,
        content = content,
    )
}

@Composable
fun CwrNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
) {
    NavigationRailItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationRailItemDefaults.colors(
            selectedIconColor = CwrNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = CwrNavigationDefaults.navigationContentColor(),
            selectedTextColor = CwrNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = CwrNavigationDefaults.navigationContentColor(),
            indicatorColor = CwrNavigationDefaults.navigationIndicatorColor(),
        ),
        interactionSource = remember { NoRippleInteractionSource() }, // Disable ripple
    )
}

@Composable
fun CwrNavigationRail(
    modifier: Modifier = Modifier,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    NavigationRail(
        modifier = modifier,
//        containerColor = Color.Transparent,
        contentColor = CwrNavigationDefaults.navigationContentColor(),
        header = header,
        content = content,
    )
}

class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction): Boolean = true
}

@Composable
fun CwrNavigationSuiteScaffold(
    navigationSuiteItems: CwrNavigationSuiteScope.() -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    content: @Composable () -> Unit,
) {
    val layoutType = NavigationSuiteScaffoldDefaults
        .calculateFromAdaptiveInfo(windowAdaptiveInfo)

    val navigationSuiteItemColors = NavigationSuiteItemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = CwrNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = CwrNavigationDefaults.navigationContentColor(),
            selectedTextColor = CwrNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = CwrNavigationDefaults.navigationContentColor(),
            indicatorColor = CwrNavigationDefaults.navigationIndicatorColor(),
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = CwrNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = CwrNavigationDefaults.navigationContentColor(),
            selectedTextColor = CwrNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = CwrNavigationDefaults.navigationContentColor(),
            indicatorColor = CwrNavigationDefaults.navigationIndicatorColor(),
        ),
        navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
            selectedIconColor = CwrNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = CwrNavigationDefaults.navigationContentColor(),
            selectedTextColor = CwrNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = CwrNavigationDefaults.navigationContentColor(),
        ),
    )
    val interactionSource = remember { NoRippleInteractionSource() }
     NavigationSuiteScaffold(
            navigationSuiteItems = {
                CwrNavigationSuiteScope(
                    navigationSuiteScope = this,
                    navigationSuiteItemColors = navigationSuiteItemColors,
                    interactionSource = interactionSource
                ).run(navigationSuiteItems)
            },
            layoutType = layoutType,
//            containerColor = materialColor().background,
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationBarContentColor = CwrNavigationDefaults.navigationContentColor(),
                navigationRailContainerColor = materialColor().background,
                navigationBarContainerColor = materialColor().background,
                navigationDrawerContainerColor = materialColor().background,
            ),
            modifier = modifier,
        ) {
            content()
        }
}

class CwrNavigationSuiteScope internal constructor(
    private val navigationSuiteScope: NavigationSuiteScope,
    private val navigationSuiteItemColors: NavigationSuiteItemColors,
    private val interactionSource: MutableInteractionSource = MutableInteractionSource(),
) {
    fun item(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        icon: @Composable () -> Unit,
        selectedIcon: @Composable () -> Unit = icon,
        label: @Composable (() -> Unit)? = null,
//        interactionSource: MutableInteractionSource = MutableInteractionSource(),
    )  = navigationSuiteScope.item(
        selected = selected,
        onClick = onClick,
        icon = {
            if (selected) {
                selectedIcon()
            } else {
                icon()
            }
        },
        label = label,
        colors = navigationSuiteItemColors,
        modifier = modifier,
        interactionSource = interactionSource
    )
}

object CwrNavigationDefaults {
//    @Composable
//    fun navigationContentColor() = materialColor().onSurfaceVariant
//
//    @Composable
//    fun navigationSelectedItemColor() = materialColor().onPrimaryContainer
//
//    @Composable
//    fun navigationIndicatorColor() = materialColor().primaryContainer.copy(alpha = 0.2f)

    @Composable
    fun navigationContentColor() = materialColor().primary

    @Composable
    fun navigationSelectedItemColor() = materialColor().primary

    @Composable
    fun navigationIndicatorColor() = materialColor().primaryContainer.copy(alpha = 0.0f)
}

@CwrPreviews
@Composable
fun CwrNavigationBarPreview() {
    val items = listOf("Home", "Bookmarks", "Tools")
    val icons = listOf(
        CwrIcons.Home,
        CwrIcons.Bookmark,
        CwrIcons.Tools,
    )
    val selectedIcons = listOf(
        CwrIcons.HomeSelected,
        CwrIcons.BookmarkSelected,
        CwrIcons.ToolsSelected,
    )

    PdfReaderTheme {
        CwrNavigationBar {
            items.forEachIndexed { index, item ->
                CwrNavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = item,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = selectedIcons[index],
                            contentDescription = item,
                        )
                    },
                    label = { CwrText(item) },
                    selected = index == 0,
                    onClick = { },
                )
            }
        }
    }
}

@CwrPreviews
@Composable
fun CwrNavigationRailPreview() {
    val items = listOf("Home", "Bookmarks", "Tools")
    val icons = listOf(
        CwrIcons.Home,
        CwrIcons.Bookmark,
        CwrIcons.Tools,
    )
    val selectedIcons = listOf(
        CwrIcons.HomeSelected,
        CwrIcons.BookmarkSelected,
        CwrIcons.ToolsSelected,
    )

    PdfReaderTheme {
        CwrNavigationRail {
            items.forEachIndexed { index, item ->
                CwrNavigationRailItem(
                    icon = {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = item,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = selectedIcons[index],
                            contentDescription = item,
                        )
                    },
                    label = { CwrText(item) },
                    selected = index == 0,
                    onClick = { },
                )
            }
        }
    }
}