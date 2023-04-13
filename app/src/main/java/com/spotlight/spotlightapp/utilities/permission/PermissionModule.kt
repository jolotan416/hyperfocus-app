package com.spotlight.spotlightapp.utilities.permission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.spotlight.spotlightapp.utilities.viewutils.CustomAlertDialog

class PermissionModule(
    private val context: Context, private val permission: String,
    private val permissionHolder: PermissionHolder,
    private val onPermissionGrantedCallback: () -> Unit) {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    /**
     * Initialize module to create ActivityResultLauncher in appropriate lifecycle for Activity/Fragment
     */
    fun initializeModule() {
        requestPermissionLauncher = permissionHolder.getRequestPermissionLauncher(
            onPermissionGrantedCallback)
    }

    fun requestPermissionWithRationale(
        permissionRationaleAlertDialogViewData: CustomAlertDialog.ViewData) {
        when {
            ContextCompat.checkSelfPermission(
                context, permission) == PackageManager.PERMISSION_GRANTED -> {
                onPermissionGrantedCallback()
            }
            permissionHolder.shouldShowPermissionRationale() -> {
                CustomAlertDialog(context, permissionRationaleAlertDialogViewData).show()
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    fun redirectToPermissionSettingsPage() {
        context.startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)))
    }

    interface PermissionHolder {
        fun getRequestPermissionLauncher(
            onPermissionGrantedCallback: () -> Unit): ActivityResultLauncher<String>

        fun shouldShowPermissionRationale(): Boolean
    }
}

fun Fragment.permissionModule(
    permission: String, onPermissionGrantedCallback: () -> Unit): Lazy<PermissionModule> = lazy {
    val permissionHolder = object : PermissionModule.PermissionHolder {
        override fun getRequestPermissionLauncher(
            onPermissionGrantedCallback: () -> Unit): ActivityResultLauncher<String> =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()) { isPermissionGranted ->
                if (isPermissionGranted) {
                    onPermissionGrantedCallback()
                }
            }

        override fun shouldShowPermissionRationale(): Boolean =
            shouldShowRequestPermissionRationale(permission)
    }
    PermissionModule(
        requireContext(), permission, permissionHolder, onPermissionGrantedCallback)
}