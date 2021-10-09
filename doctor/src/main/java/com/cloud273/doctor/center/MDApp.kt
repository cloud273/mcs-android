package com.cloud273.doctor.center

import android.app.Application
import com.cloud273.backend.api.base.McsRequestApi
import com.cloud273.localization.CLLocalization
import com.cloud273.mcs.center.McsAppConfiguration
import com.cloud273.mcs.center.instanceDatabase

class MDApp : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    companion object {
        lateinit var instance: MDApp
    }

    fun initialize() {
        McsRequestApi.setApplication(this)
        CLLocalization.initialize(this, listOf("vi", "en"))
        McsAppConfiguration.instance = McsAppConfiguration(this)
        MDDatabase.instance = MDDatabase(this)
        instanceDatabase = MDDatabase.instance
    }

}