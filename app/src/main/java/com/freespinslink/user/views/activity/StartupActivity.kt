package com.freespinslink.user.views.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.freespinslink.user.R
import com.freespinslink.user.ads.unity.AdsConfig
import com.freespinslink.user.utils.AdsDebugConstants
import com.freespinslink.user.utils.SharedStorage
import com.freespinslink.user.views.fragment.PrivacyPolicyFragmentDirections
import com.ironsource.adapters.supersonicads.SupersonicConfig
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.impressionData.ImpressionData
import com.ironsource.mediationsdk.impressionData.ImpressionDataListener
import com.ironsource.mediationsdk.integration.IntegrationHelper
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.InitializationListener
import com.ironsource.mediationsdk.sdk.InterstitialListener

class StartupActivity : AppCompatActivity() {

    private lateinit var navigationController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        try {
            setupViews()
        } catch (e: Exception) {
            e.localizedMessage?.let {
                Log.d(javaClass.simpleName, "onCreate: $it")
            }
        }

        requestNotificationPermission()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (SharedStorage.isPrivacyChecked())
            super.onBackPressed()
        else finish()
    }

    override fun onResume() {
        super.onResume()
        IronSource.onResume(this) // without this ads will not work
    }

    override fun onPause() {
        super.onPause()
        IronSource.onPause(this)
    }

    private fun setupViews() {
        navigationController = Navigation.findNavController(this, R.id.mainHostFragment)

        if (SharedStorage.isPrivacyChecked()) {
            navigationController
                .navigate(
                    PrivacyPolicyFragmentDirections.actionPrivacyPolicyFragmentToRewardsFragment()
                )
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

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionResult.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

}