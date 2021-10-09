package com.cloud273.mcs.center

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.cloud273.backend.api.common.McsConfigApi
import com.cloud273.backend.model.common.McsCity
import com.cloud273.backend.model.common.McsCountry
import com.cloud273.backend.model.common.McsSpecialty
import com.cloud273.backend.model.common.McsState
import com.dungnguyen.qdcore.extension.toArray
import com.dungnguyen.qdcore.extension.toJsonString
import com.dungnguyen.qdcore.support.RawTextSupport
import java.lang.reflect.Type

class McsAppConfiguration(private val application: Application) {

    companion object {
        lateinit var instance: McsAppConfiguration
    }

    private val _center: SharedPreferences = application.getSharedPreferences(application.packageName + ".application.configuration", Context.MODE_PRIVATE)

    private val countryKey = "country"
    private val specialtyKey = "specialty"

    private var _countries: List<McsCountry>
    private var _specialties: List<McsSpecialty>

    private var creatableEndKey = "creatableEnd"
    private var acceptableEndKey = "acceptableEnd"
    private var cancalableEndKey = "cancalableEnd"
    private var rejectableEndKey = "rejectableEnd"
    private var beginableFromKey = "beginableFrom"
    private var beginableEndKey = "beginableEnd"
    private var finishableEndKey = "finishableEnd"


    init {
        _countries = load(countryKey, McsCountry::class.java)
        _specialties = load(specialtyKey, McsSpecialty::class.java)
        update {

        }
    }

    private fun <T> load(key: String, type: Type) : List<T> {
        val text = _center.getString(key, RawTextSupport.loadRawText(application, key))!!
        return text.toArray(type)!!
    }

    private fun update(completion: (success: Boolean) -> Unit) {
        McsConfigApi().run { success, countries, specialties, _, _, _, creatableEnd, acceptableEnd, cancelableEnd, rejectableEnd, beginableFrom, beginableEnd, finishableEnd ->
            if (success) {
                _center.edit().apply {
                    putInt(creatableEndKey, creatableEnd!!)
                    putInt(acceptableEndKey, acceptableEnd!!)
                    putInt(cancalableEndKey, cancelableEnd!!)
                    putInt(rejectableEndKey, rejectableEnd!!)
                    putInt(beginableFromKey, beginableFrom!!)
                    putInt(beginableEndKey, beginableEnd!!)
                    putInt(finishableEndKey, finishableEnd!!)
                    putString(countryKey, countries!!.toJsonString()!!)
                    putString(specialtyKey, specialties!!.toJsonString()!!)
                }.apply()

            }
            completion(success)
        }
    }

    val creatableEnd: Int
        get() = _center.getInt(acceptableEndKey, defaultCreatableEnd)

    val acceptableEnd: Int
        get() = _center.getInt(acceptableEndKey, defaultAcceptableEnd)

    val cancelableEnd: Int
        get() = _center.getInt(cancalableEndKey, defaultCancelableEnd)

    val rejectableEnd: Int
        get() = _center.getInt(rejectableEndKey, defaultRejectableEnd)

    val beginableFrom: Int
        get() = _center.getInt(beginableEndKey, defaultBeginableFrom)

    val beginableEnd: Int
        get() = _center.getInt(beginableEndKey, defaultBeginableEnd)

    val finishableEnd: Int
        get() = _center.getInt(finishableEndKey, defaultFinishableEnd)

    val specialties: List<McsSpecialty>
        get() = _specialties

    fun specialty(code: String): McsSpecialty? {
        return _specialties.first {
            it.code == code
        }
    }

    val countries: List<McsCountry>
        get() = _countries

    fun country(code: String): McsCountry? {
        return _countries.first {
            it.code == code
        }
    }

    fun states(countryCode: String): List<McsState>? {
        for (country in _countries) {
            if (country.code == countryCode) {
                return country.states
            }
        }
        return null
    }

    fun state(code: String): McsState? {
        for (country in _countries) {
            for (state in country.states) {
                if (state.code == code) {
                    return state
                }
            }
        }
        return null
    }

    fun cities(stateCode: String): List<McsCity>? {
        return state(stateCode)?.cities
    }

    fun city(code: String): McsCity? {
        for (country in _countries) {
            for (state in country.states) {
                for (city in state.cities) {
                    if (city.code == code) {
                        return city
                    }
                }
            }
        }
        return null
    }

}