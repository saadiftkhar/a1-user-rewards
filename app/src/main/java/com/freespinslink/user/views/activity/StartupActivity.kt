package com.freespinslink.user.views.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.facebook.ads.AdSettings
import com.freespinslink.user.R
import com.freespinslink.user.ads.unity.AdsConfig
import com.freespinslink.user.utils.AppConfig
import com.freespinslink.user.utils.SharedStorage
import com.freespinslink.user.views.fragment.PrivacyPolicyFragmentDirections
import com.ironsource.adapters.supersonicads.SupersonicConfig
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.integration.IntegrationHelper
import com.ironsource.mediationsdk.sdk.InitializationListener

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

        initializeAds()
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

    private fun initializeAds() {
        IronSource.shouldTrackNetworkState(this, true)
        if (AppConfig.isTestMode()) {
            IntegrationHelper.validateIntegration(this) // The integrationHelper is used to validate the integration. Remove the integrationHelper before going live!
            IronSource.setMetaData("is_test_suite", "enable")
        }

        SupersonicConfig.getConfigObj().clientSideCallbacks = true
        IronSource.setUserId(IronSource.getAdvertiserId(this))
        IronSource.setMetaData("Facebook_IS_CacheFlag", "IMAGE")
        IronSource.init(this, AdsConfig().appKey, object : InitializationListener {
            override fun onInitializationComplete() {
                if (AppConfig.isTestMode()) {
                    AdSettings.addTestDevice("bdbc42a8-fdf8-4f2b-ad33-46ea90f20fb0")
                    IronSource.launchTestSuite(this@StartupActivity)
                    Log.d(javaClass.simpleName, "onInitializationComplete: true")
                }
            }
        })

    }

}