package com.freespinslink.user.views.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.freespinslink.user.R
import com.freespinslink.user.ads.applovinUnityMediation.OpenAppAdManager
import com.freespinslink.user.databinding.ActivitySplashBinding
import com.freespinslink.user.utils.startNewActivity
import java.util.Timer
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        OpenAppAdManager(this,
            onLoaded = {

            },
            onDisplayed = {

            },
            onFailed = {
                Timer().schedule(2000) {
                    startNewActivity(StartupActivity::class.java)
                }
            },
            onClose = {
                startNewActivity(StartupActivity::class.java)
            }
        )


    }
}