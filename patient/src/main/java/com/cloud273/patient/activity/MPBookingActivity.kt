package com.cloud273.patient.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.cloud273.localization.localized
import com.cloud273.localization.startMonitorLanguageChanged
import com.cloud273.localization.stopMonitorLanguageChanged
import com.cloud273.mcs.center.*
import com.cloud273.patient.R
import com.cloud273.patient.model.MPAppointment
import com.dungnguyen.qdcore.support.SupportCenter

class MPBookingActivity : AppCompatActivity() {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent!!.action!!) {
                accountDidClearNotification -> finish()
            }
        }
    }

    companion object {
        const val appointmentKey = "appointment"
    }

    lateinit var appointment: MPAppointment

    private fun navController(): NavController {
        return findNavController(R.id.fragmentContainer)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMonitorLanguageChanged()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startMonitorLanguageChanged()
        appointment = SupportCenter.instance.pop(intent.getStringExtra(appointmentKey)!!)
        setContentView(R.layout.activity_mp_booking)

        val navController = navController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(accountDidClearNotification))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cancel_menu, menu)
        menu!!.getItem(0).title = menu.getItem(0).title.toString().localized
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.cancel) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController().navigateUp() || super.onSupportNavigateUp()
    }

}
