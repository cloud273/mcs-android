package com.cloud273.doctor.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.cloud273.doctor.R


class MDLoginActivity : AppCompatActivity() {
    private fun navController(): NavController {
        return findNavController(R.id.fragmentContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_md_login)

        val navController = navController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onBackPressed() {
        if (navController().currentDestination?.id != R.id.mdAccountLoginFragment) {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController().navigateUp() || super.onSupportNavigateUp()
    }
}
