package com.cloud273.patient.center

import android.app.Application
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cloud273.backend.model.common.McsAccount
import com.cloud273.backend.model.common.McsAddress
import com.cloud273.backend.model.common.McsLanguage
import com.cloud273.backend.model.common.McsProfile
import com.cloud273.backend.model.patient.McsAllergy
import com.cloud273.backend.model.patient.McsMedication
import com.cloud273.backend.model.patient.McsPatient
import com.cloud273.backend.model.patient.McsSurgery
import com.cloud273.mcs.center.McsBaseDatabase
import com.cloud273.mcs.center.accountGenderDidUpdatedNotification
import com.cloud273.mcs.center.accountHealthInfoDidChangeNotification
import com.cloud273.mcs.center.accountInfoDidChangeNotification
import com.cloud273.mcs.model.medications

class MPDatabase(application: Application) : McsBaseDatabase(application) {

    companion object {
        lateinit var instance: MPDatabase
    }

    override fun updateAccount(account: McsAccount) {
        if (token != null && this.account != null) {
            val old = this.account as McsPatient
            val patient = account as McsPatient
            patient.allergies = old.allergies
            patient.surgeries = old.surgeries
            patient._medications = old._medications
            super.updateAccount(patient)
        }
    }

    fun updateAccount(allergies: List<McsAllergy>, surgeries: List<McsSurgery>, medications: List<McsMedication>) {
        if (token != null && this.account != null) {
            val patient = this.account as McsPatient
            patient.allergies = allergies
            patient.surgeries = surgeries
            patient._medications = medications
            LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountHealthInfoDidChangeNotification))
        }
    }

    fun updateAccount(profile: McsProfile, address: McsAddress) {
        if (token != null && this.account != null) {
            val patient = this.account as McsPatient
            val genderChanged = patient.profile!!.gender != profile.gender
            patient.profile = profile
            patient.address = address
            if (genderChanged) {
                LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountGenderDidUpdatedNotification))
            }
            LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountInfoDidChangeNotification))
        }
    }

    fun updateAccount(imageName : String?, language: McsLanguage?) {
        if (token != null && this.account != null) {
            val patient = this.account as McsPatient
            patient.imageName = imageName
            patient.language = language
            LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountInfoDidChangeNotification))
        }
    }

    fun add(allergy: McsAllergy) {
        if (token != null && this.account != null) {
            val patient = this.account as McsPatient
            val allergies = if (patient.allergies != null) patient.allergies!!.toMutableList() else mutableListOf()
            val obj = allergies.find {
                it.id!! == allergy.id!!
            }
            if (obj == null) {
                allergies.add(allergy)
                patient.allergies = allergies
                LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountHealthInfoDidChangeNotification))
            }
        }
    }

    fun update(allergy: McsAllergy) {
        if (token != null && this.account != null) {
            val patient = this.account as McsPatient
            if (patient.allergies != null) {
                val index = patient.allergies!!.indexOfFirst {
                    it.id!! == allergy.id!!
                }
                if (index >= 0) {
                    val allergies = patient.allergies!!.toMutableList()
                    allergies[index] = allergy
                    patient.allergies = allergies
                    LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountHealthInfoDidChangeNotification))
                }
            }

        }
    }

    fun deleteAllergy(id: Int) {
        if (token != null && this.account != null) {
            val patient = this.account as McsPatient
            if (patient.allergies != null) {
                val allergies = patient.allergies!!.filter {
                    it.id!! != id
                }
                if (allergies.size != patient.allergies!!.size) {
                    patient.allergies = allergies
                    LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountHealthInfoDidChangeNotification))
                }
            }

        }
    }

    fun add(surgery: McsSurgery) {
        if (token != null && this.account != null) {
            val patient = this.account as McsPatient
            val surgeries = if (patient.surgeries != null) patient.surgeries!!.toMutableList() else mutableListOf()
            val obj = surgeries.find {
                it.id!! == surgery.id!!
            }
            if (obj == null) {
                surgeries.add(surgery)
                patient.surgeries = surgeries
                LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountHealthInfoDidChangeNotification))
            }
        }
    }

    fun update(surgery: McsSurgery) {
        if (token != null && this.account != null) {
            val patient = this.account as McsPatient
            if (patient.surgeries != null) {
                val index = patient.surgeries!!.indexOfFirst {
                    it.id!! == surgery.id!!
                }
                if (index >= 0) {
                    val surgeries = patient.surgeries!!.toMutableList()
                    surgeries[index] = surgery
                    patient.surgeries = surgeries
                    LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountHealthInfoDidChangeNotification))
                }
            }

        }
    }

    fun deleteSurgery(id: Int) {
        if (token != null && this.account != null) {
            val patient = this.account as McsPatient
            if (patient.surgeries != null) {
                val surgeries = patient.surgeries!!.filter {
                    it.id!! != id
                }
                if (surgeries.size != patient.surgeries!!.size) {
                    patient.surgeries = surgeries
                    LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountHealthInfoDidChangeNotification))
                }
            }

        }
    }

    fun update(medication: McsMedication) {
        if (token != null && this.account != null) {
            val patient = this.account as McsPatient
            if (patient._medications != null) {
                val index = patient._medications!!.indexOfFirst {
                    it.id!! == medication.id!!
                }
                if (index >= 0) {
                    val medications = patient._medications!!.toMutableList()
                    medications[index] = medication
                    patient._medications = medications
                    LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountHealthInfoDidChangeNotification))
                }
            }

        }
    }

}