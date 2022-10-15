package com.freespinslink.user.views.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.freespinslink.user.R
import com.freespinslink.user.utils.SharedStorage

class StartupActivity : AppCompatActivity() {

    private lateinit var navigationController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        setupViews()

    }

    private fun setupViews() {
        navigationController = Navigation.findNavController(this, R.id.mainHostFragment)
        val navGraph = navigationController.navInflater.inflate(R.navigation.main_navigation)

        if (SharedStorage.isPrivacyChecked())
            navGraph.setStartDestination(R.id.rewardsFragment)
        else
            navGraph.setStartDestination(R.id.privacyPolicyFragment)
        navigationController.graph = navGraph

    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }
}