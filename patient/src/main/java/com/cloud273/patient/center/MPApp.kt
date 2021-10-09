package com.cloud273.patient.center

import android.app.Application
import com.cloud273.backend.api.base.McsRequestApi
import com.cloud273.localization.CLLocalization
import com.cloud273.mcs.center.McsAppConfiguration
import com.cloud273.mcs.center.instanceDatabase

class MPApp : Application() {

    init {
        instance = this
    }

    companion object {
        lateinit var instance: MPApp
    }

    fun initialize() {
        McsRequestApi.setApplication(this)
        CLLocalization.initialize(this, listOf("vi", "en"))
        McsAppConfiguration.instance = McsAppConfiguration(this)
        MPDatabase.instance = MPDatabase(this)
        instanceDatabase = MPDatabase.instance
    }

}