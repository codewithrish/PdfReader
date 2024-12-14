package com.codewithrish.pdfreader.ui.permission

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import android.provider.Settings
import android.net.Uri
import android.Manifest

class PermissionHandler(private val context: Context) {

    fun isPermissionGranted(): Boolean {
        val versionCode = Build.VERSION.SDK_INT
        return when {
            versionCode < Build.VERSION_CODES.Q -> {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            }
            versionCode == Build.VERSION_CODES.Q -> {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            }
            else -> Environment.isExternalStorageManager()
        }
    }

    fun createPermissionIntent(): Intent {
        val versionCode = Build.VERSION.SDK_INT
        return when {
            versionCode < Build.VERSION_CODES.Q -> {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
            }
            versionCode == Build.VERSION_CODES.Q -> {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
            }
            else -> {
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
            }
        }
    }
}
