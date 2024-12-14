package com.codewithrish.pdfreader.ui.screen.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codewithrish.pdfreader.core.common.dialog.CwrPermissionRationaleDialog
import com.codewithrish.pdfreader.core.model.room.toDocument
import com.codewithrish.pdfreader.core.ui.TrackScreenViewEvent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import timber.log.Timber
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    // Observe lifecycle events
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    Timber.tag("HomeScreen").d("ON_RESUME event triggered")
                    onEvent(HomeUiEvent.OnDocumentsLoadInDb)
                }
                Lifecycle.Event.ON_PAUSE -> {
                    Timber.tag("HomeScreen").d("ON_PAUSE event triggered")
                }
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold (
        topBar = { HomeTopAppBar(
            modifier = Modifier.fillMaxWidth()
        ) },
        content = {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center
            ) {
                val context = LocalContext.current
                val versionCode = Build.VERSION.SDK_INT

                // State to track if permission is granted
                var isPermissionGranted by remember {
                    mutableStateOf(
                        when {
                            versionCode < Build.VERSION_CODES.Q ->
                                checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                            versionCode == Build.VERSION_CODES.Q ->
                                checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                                        checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            else ->
                                Environment.isExternalStorageManager()
                        }
                    )
                }
                // State to check if permission should show rationale
                var showRationaleDialog by remember { mutableStateOf(false) }

                // Single permission launcher for older Android versions
                val requestPermissionLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                        isPermissionGranted = isGranted
                    }

                // Multiple permissions launcher for Android 10
                val requestAndroid10PermissionsLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                        isPermissionGranted = permissions.all { it.value }
                    }

                // Permission management for Android 11+
                val android11PlusSettingsLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        isPermissionGranted = Environment.isExternalStorageManager()
                    }

                // Check permission on button click
                fun handlePermissionRequest() {
                    when {
                        versionCode < Build.VERSION_CODES.Q -> {
                            if (checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                isPermissionGranted = true
                            } else {
                                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        }
                        versionCode == Build.VERSION_CODES.Q -> {
                            if (checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                isPermissionGranted = true
                            } else {
                                requestAndroid10PermissionsLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    )
                                )
                            }
                        }
                        else -> {
                            if (Environment.isExternalStorageManager()) {
                                isPermissionGranted = true
                            } else {
                                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                                android11PlusSettingsLauncher.launch(intent)
                            }
                        }
                    }
                }

                // UI
                if (isPermissionGranted) {
                    state.documents.collectAsStateWithLifecycle(emptyList()).value.let { documents ->
                        documents.forEach { document ->
                            val file = File(document.path)
                            Timber.tag("HomeScreen").d("File exists: ${document.path} ${file.exists()}")
                            if (!file.exists()) {
                                onEvent(HomeUiEvent.DeleteDocument(document.toDocument()))
                            }
                        }.also {
                            DocumentList(
                                documents = documents.map { it.toDocument() }.filter { it.mimeType == DocumentType.PDF.name },
                                onEvent = onEvent,
                                modifier = Modifier
                            )
                        }
                    }
                } else {
                    Button(onClick = { handlePermissionRequest() }) {
                        Text("Request Storage Permission")
                    }
                }

                // Show rationale dialog if needed
                if (showRationaleDialog) {
                    CwrPermissionRationaleDialog(
                        title = "Permission Needed",
                        description = "This app needs access to your external storage to continue.",
                        confirmButtonText = "Open Settings",
                        cancelButtonText = "Cancel",
                        dismissDialog = { showRationaleDialog = false },
                    ) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }
                }
            }
        }
    )
    TrackScreenViewEvent(screenName = "Home Screen")
}

// Function to check if permission is already granted
fun checkPermission(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED
}

@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(56.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            imageVector = Icons.Default.Search,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        Text(
            text = "Pdf Reader",
            style = MaterialTheme.typography.titleLarge
        )
        Image(
            imageVector = Icons.Default.Settings,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
    }
}

