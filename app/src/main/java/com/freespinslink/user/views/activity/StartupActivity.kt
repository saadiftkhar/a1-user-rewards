package com.freespinslink.user.views.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.applovin.sdk.AppLovinSdk
import com.freespinslink.user.R
import com.freespinslink.user.ads.applovinUnityMediation.AdsConfig
import com.freespinslink.user.utils.SharedStorage

class StartupActivity : AppCompatActivity() {

    private lateinit var navigationController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        if (AdsConfig.isDebuggerShown) {
            AppLovinSdk.getInstance(this).showMediationDebugger()
        }

        try {
            setupViews()
        } catch (e: Exception) {
            e.localizedMessage?.let {
                Log.d(javaClass.simpleName, "onCreate: $it")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (SharedStorage.isPrivacyChecked())
            super.onBackPressed()
        else finish()
    }

    private fun setupViews() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionResult.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        navigationController = Navigation.findNavController(this, R.id.mainHostFragment)

        if (SharedStorage.isPrivacyChecked()) {
            navigationController.navigate(R.id.action_privacyPolicyFragment_to_rewardsFragment)
        }
    }

    private val notificationPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                // notification permission granted
            } else {
                // Handle the case when one or more permissions were not granted
            }

            Log.d("NOTIFICATION_PERMISSION", "$result")

        }


}