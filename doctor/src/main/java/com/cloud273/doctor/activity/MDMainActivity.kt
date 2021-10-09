package com.cloud273.doctor.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.cloud273.backend.api.account.McsAccountDeviceSetApi
import com.cloud273.backend.api.doctor.account.McsDoctorClinicInfoApi
import com.cloud273.doctor.R
import com.cloud273.doctor.center.MDDatabase
import com.cloud273.mcs.center.accountDidClearNotification
import com.cloud273.mcs.center.accountDidSetNotification
import com.cloud273.mcs.center.appointmentDidUpdatedNotification
import com.cloud273.backend.model.common.McsAccountType
import com.cloud273.doctor.center.MDApp
import com.cloud273.localization.localized
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_md_main.*

class MDMainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private var isStopped = false
    private var moveToSelectedTab = false
    private var selectedTab: Int = R.id.mdListAppointmentFragment

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent!!.action!!) {
                accountDidClearNotification -> showLogin()
                accountDidSetNotification -> {
                    pushDeviceTokenIfNeed()
                    updateTab(R.id.mdListAppointmentFragment)
                }
                appointmentDidUpdatedNotification -> updateTab(R.id.mdListAppointmentFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MDApp.instance.initialize()
        setContentView(R.layout.activity_md_main)

        navController = findNavController(R.id.fragmentContainer)
        val menuList = setOf(R.id.mdListAppointmentFragment, R.id.mdSettingFragment, R.id.mdHistoryListAppointmentFragment);
        appBarConfiguration = AppBarConfiguration(menuList)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigation.setupWithNavController(navController)
        bottomNavigation.menu.also {
            for (i in menuList.indices) {
                it.getItem(i).title = it.getItem(i).title.toString().localized
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(accountDidClearNotification))
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(accountDidSetNotification))
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(appointmentDidUpdatedNotification))
        verifyAccount()
    }

    private fun verifyAccount() {
        val token = MDDatabase.instance.existedToken
        if (token == null) {
            showLogin()
        } else {
            McsDoctorClinicInfoApi(token).run { success, doctor, clinic, code ->
                if (success) {
                    MDDatabase.instance.setAccount(doctor!!, clinic!!)
                } else {
                    println(code);
                }
            }
        }
    }

    private fun pushDeviceTokenIfNeed() {
        MDDatabase.instance.token?.also { token ->
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                if (it.isSuccessful) {
                    val deviceToken = it.result!!.token
                    McsAccountDeviceSetApi(McsAccountType.doctor, token, deviceToken).run { _, _, _ ->

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
            navController.navigate(selectedTab)
        }
    }

    private fun showLogin() {
//        return
        val intent = Intent(this, MDLoginActivity::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
