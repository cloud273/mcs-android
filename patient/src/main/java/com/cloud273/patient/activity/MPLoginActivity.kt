package com.cloud273.patient.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.cloud273.localization.startMonitorLanguageChanged
import com.cloud273.localization.stopMonitorLanguageChanged
import com.cloud273.patient.R

class MPLoginActivity : AppCompatActivity() {

    private fun navController(): NavController {
        return findNavController(R.id.fragmentContainer)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMonitorLanguageChanged()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startMonitorLanguageChanged()
        setContentView(R.layout.activity_mp_login)

        val navController = navController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onBackPressed() {
        if (navController().currentDestination?.id != R.id.loginFragment) {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController().navigateUp() || super.onSupportNavigateUp()
    }

}
