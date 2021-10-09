package com.cloud273.doctor.center

import android.app.Application
import com.cloud273.mcs.center.McsBaseDatabase
import com.cloud273.backend.model.doctor.McsClinic
import com.cloud273.backend.model.doctor.McsDoctor

class MDDatabase(application: Application) : McsBaseDatabase(application) {

    companion object {
        lateinit var instance: MDDatabase
    }

    var clinic: McsClinic? = null

    fun setAccount(account : McsDoctor, clinic: McsClinic, token : String) {
        this.clinic = clinic
        super.setAccount(account, token)
    }

    fun setAccount(account : McsDoctor, clinic: McsClinic) {
        this.clinic = clinic
        super.setAccount(account)
    }

    fun updateAccount(account : McsDoctor, clinic: McsClinic) {
        this.clinic = clinic
        super.updateAccount(account)
    }

    override fun clear() {
        clinic = null
        super.clear()
    }
}