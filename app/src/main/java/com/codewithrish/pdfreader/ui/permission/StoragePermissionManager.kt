package com.codewithrish.pdfreader.ui.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.ui.theme.materialColor

@Composable
fun StoragePermissionManager(
    onPermissionGranted: @Composable () -> Unit
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
                versionCode >= Build.VERSION_CODES.R ->
                    checkIfExternalStorageManager()
                else -> false
            }
        )
    }

    // Handle permission logic for older versions
    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        isPermissionGranted = isGranted
    }

    // Handle permission logic for Android 10
    val requestAndroid10PermissionsLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            isPermissionGranted = permissions.all { it.value }
        }

    // Handle permission logic for Android 11+
    val android11PlusSettingsLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            isPermissionGranted = checkIfExternalStorageManager()
        }

    // Handle permission request
    fun handlePermissionRequest() {
        when {
            versionCode >= Build.VERSION_CODES.R -> {
                if (checkIfExternalStorageManager()) {
                    isPermissionGranted = true
                } else {
                    val intentAction = getManageAppAllFilesAccessPermissionAction() ?: Settings.ACTION_SETTINGS
                    val intent = Intent(intentAction).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    android11PlusSettingsLauncher.launch(intent)
                }
            }
            versionCode < Build.VERSION_CODES.Q -> {
                if (checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    isPermissionGranted = true
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            else -> {
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
        }
    }

    if (!isPermissionGranted) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(materialColor().background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Image(
                    painter = painterResource(R.drawable.img_permission_placeholder),
                    contentDescription = null,
                    modifier = Modifier.size(240.dp)
                )
                CwrText(
                    text = buildAnnotatedString {
                        append("For your access to document & media files, ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("\"Open PDF\"")
                        }
                        append(" needs storage Permission")
                    }.toString(),
                    textAlign = TextAlign.Center,
                    color = materialColor().onBackground,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Button(onClick = { handlePermissionRequest() }) {
                    CwrText("Allow Storage permissions")
                }
            }
        }
    } else {
        onPermissionGranted()
    }
}

// Function to check if permission is already granted
fun checkPermission(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED
}

// Function to check if the app has "Manage External Storage" permission
fun checkIfExternalStorageManager(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()
}

fun getManageAppAllFilesAccessPermissionAction(): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        try {
            Settings::class.java.getDeclaredField("ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION").get(null) as? String
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        null
    }
}

