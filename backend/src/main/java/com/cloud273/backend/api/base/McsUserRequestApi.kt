package com.cloud273.backend.api.base

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlin.coroutines.CoroutineContext

open class McsUserRequestApi(private val token: String): McsRequestApi() {

    override fun headers(): Map<String, String>? {
        val result = super.headers()!!.toMutableMap()
        result["token"] = token
        return result
    }

    override fun commonFetch(context: CoroutineContext, completion: (code: Int, message: String?, body: String?) -> Unit) {
        fetch(context) { code, message, body ->
            completion(code, message, body)
            if (code == 403) {
                LocalBroadcastManager.getInstance(application.applicationContext).sendBroadcast(
                    Intent(expiredTokenNotification)
                )
            }
        }
    }
}