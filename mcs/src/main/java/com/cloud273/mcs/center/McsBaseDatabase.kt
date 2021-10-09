package com.cloud273.mcs.center

import android.app.Application
import android.content.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cloud273.backend.api.base.McsRequestApi
import com.cloud273.backend.api.account.McsAccountDeviceSetApi
import com.cloud273.backend.model.common.McsAccount
import com.cloud273.backend.model.common.McsAccountType
import com.cloud273.backend.model.common.McsLanguage
import com.cloud273.backend.model.doctor.McsDoctor
import com.cloud273.localization.CLLocalization

lateinit var instanceDatabase: McsBaseDatabase

abstract class McsBaseDatabase(protected val application: Application) {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            clear()
        }
    }

    private val _tKey : String = "_token"
    private val _center: SharedPreferences = application.getSharedPreferences(application.packageName + ".database", Context.MODE_PRIVATE)
    private var _account : McsAccount? = null
    private var _token : String? = null

    init {
        _token = _center.getString(_tKey, null)
        LocalBroadcastManager.getInstance(application).registerReceiver(receiver, IntentFilter(McsRequestApi.expiredTokenNotification))
    }

    var language: McsLanguage
        get() = McsLanguage.valueOf(CLLocalization.language)
        set(value) {
            CLLocalization.setLanguage(value.value)
        }

    val accountType: McsAccountType
        get() {
            return if (account != null && (account as? McsDoctor) != null) {
                McsAccountType.doctor
            } else {
                McsAccountType.patient
            }
        }
    val account: McsAccount?
        get() = _account

    val token: String?
        get() {
            return if (_account != null) {
                _token
            } else {
                null
            }
        }

    val existedToken: String?
        get() = _token

    fun updateAccount(token: String) {
        _token = token
        _center.edit().putString(_tKey, token).apply()
    }

    open fun updateAccount(account: McsAccount) {
        if (_token != null) {
            this._account = account
            LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountInfoDidChangeNotification))
        }
    }

    fun setAccount(account: McsAccount) {
        if (_token != null) {
            _account = account
            LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountDidSetNotification))
            _center.edit().putString(_tKey, token).apply()
        }
    }

    fun setAccount(account: McsAccount, token: String)  {
        _token = token
        _account = account
        LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountDidSetNotification))
        _center.edit().putString(_tKey, token).apply()
    }

    open fun clear() {
        clearDeviceTokenIfNeed()
        val shouldBroadcast = (_token != null)
        _token = null
        _account = null
        _center.edit().remove(_tKey).apply()
        if (shouldBroadcast) {
            LocalBroadcastManager.getInstance(application).sendBroadcast(Intent(accountDidClearNotification))
        }
    }

    private fun clearDeviceTokenIfNeed() {
        token?.also {
            McsAccountDeviceSetApi(accountType, it, null).run { _, _, _ ->

            }
        }
    }

}