package com.codewithrish.pdfreader.core.designsystem.icon

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dehaze
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Grid3x3
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ViewComfyAlt
import androidx.compose.material.icons.filled.ViewCozy
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Compress
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Grid3x3
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewComfyAlt
import androidx.compose.material.icons.outlined.ViewCozy
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Grid3x3
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.R

object CwrIcons {
    // NavBar Icons
    val Home = Icons.Outlined.Home
    val HomeSelected = Icons.Filled.Home
    val Bookmark = Icons.Outlined.FavoriteBorder
    val BookmarkSelected = Icons.Filled.Favorite
    val Tools = Icons.Outlined.ViewCozy
    val ToolsSelected = Icons.Filled.ViewCozy

    // Toolbar Icons
    val BackArrow = Icons.AutoMirrored.Filled.ArrowBack
    val Settings = Icons.Outlined.Settings
    val Search = Icons.Outlined.Search

    val Share = Icons.Default.Share
    val CheckCircle = Icons.Default.CheckCircle
    val Dehaze = Icons.Default.Dehaze
    val ViewMode = Icons.Default.Book
    val LockPdf = Icons.Outlined.Lock
    val Compress = Icons.Outlined.Compress
    val Print = Icons.Default.Print
    val Eye = R.drawable.ic_eye

    // Bookmark
    val BookmarkAdd = R.drawable.ic_bookmark_add
    val BookmarkAdded = R.drawable.ic_bookmark_added
    val Icon3Dots = R.drawable.ic_3_dots

}

@Composable
fun CwrIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = modifier.size(128.dp)
    )
}