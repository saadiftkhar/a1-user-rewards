package com.freespinslink.user.views.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.freespinslink.user.R
import com.freespinslink.user.utils.SharedStorage
import com.freespinslink.user.utils.showToast
import com.freespinslink.user.views.fragment.PrivacyPolicyFragmentDirections

class StartupActivity : AppCompatActivity() {

    private lateinit var navigationController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        try {
            setupViews()
        } catch (e: Exception) {
            e.localizedMessage?.let { showToast(it) }
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
        navigationController = Navigation.findNavController(this, R.id.mainHostFragment)

        if (SharedStorage.isPrivacyChecked()) {
            navigationController
                .navigate(
                    PrivacyPolicyFragmentDirections.actionPrivacyPolicyFragmentToRewardsFragment()
                )
        }
    }


}