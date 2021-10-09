package com.cloud273.patient.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cloud273.backend.api.account.McsAccountDeviceSetApi
import com.cloud273.backend.api.patient.account.McsPatientDetailApi
import com.cloud273.backend.model.common.McsAccountType
import com.cloud273.localization.localized
import com.cloud273.mcs.center.*
import com.cloud273.patient.R
import com.cloud273.patient.center.MPApp
import com.cloud273.patient.center.MPDatabase
import com.cloud273.localization.startMonitorLanguageChanged
import com.cloud273.localization.stopMonitorLanguageChanged
import com.dungnguyen.qdcore.extension.showAlert
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_mp_main.*

class MPMainActivity : AppCompatActivity() {

    private var isStopped = false

    private var moveToSelectedTab = false
    private var selectedTab: Int = R.id.bookingNavigation

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent!!.action!!) {
                accountDidClearNotification -> showLogin()
                accountDidSetNotification -> {
                    pushDeviceTokenIfNeed()
                    updateTab(R.id.bookingNavigation)
                }
                moveToAppointmentListNotification -> updateTab(R.id.historyNavigation)
                moveToBookingNotification -> updateTab(R.id.bookingNavigation)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMonitorLanguageChanged()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    private fun navController(): NavController {
        return findNavController(R.id.fragmentContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startMonitorLanguageChanged()
        MPApp.instance.initialize()
        setContentView(R.layout.activity_mp_main)

        val navController = navController()
        val menuList = setOf(R.id.bookingPackageTypeFragment, R.id.listAppointmentFragment, R.id.healthFragment, R.id.settingFragment)
        val appBarConfiguration = AppBarConfiguration(menuList)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigation.setupWithNavController(navController)
        bottomNavigation.menu.also {
            for (i in menuList.indices) {
                it.getItem(i).title = it.getItem(i).title.toString().localized
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(accountDidClearNotification))
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(accountDidSetNotification))
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(moveToAppointmentListNotification))
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(moveToBookingNotification))
        verifyAccount()
    }

    private fun verifyAccount() {
        val token = MPDatabase.instance.existedToken
        if (token == null) {
            showLogin()
        } else {
            McsPatientDetailApi(token).run { success, patient, code ->
                if (success) {
                    MPDatabase.instance.setAccount(patient!!)
                } else {
                    if (code != 403) {
                        showAlert("Error".localized, "Backend_connect_fail_message".localized, "Retry".localized) {
                            verifyAccount()
                        }
                    }
                }
            }
        }
    }

    private fun pushDeviceTokenIfNeed() {
        MPDatabase.instance.token?.also { token ->
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                if (it.isSuccessful) {
                    val deviceToken = it.result!!.token
                    McsAccountDeviceSetApi(McsAccountType.patient, token, deviceToken).run { _, _, _ ->

                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isStopped = false
        reloadView()
    }

    override fun onPause() {
        super.onPause()
        isStopped = true

    }

    private fun updateTab(tab: Int) {
        moveToSelectedTab = true
        selectedTab = tab
        if (!isStopped) {
            reloadView()
        }
    }

    private fun reloadView() {
        if (moveToSelectedTab) {
            moveToSelectedTab = false
            if (bottomNavigation.selectedItemId != selectedTab) {
                bottomNavigation.selectedItemId = selectedTab
            }
        }
    }

    private fun showLogin() {
        val intent = Intent(this, MPLoginActivity::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController().navigateUp() || super.onSupportNavigateUp()
    }

}
