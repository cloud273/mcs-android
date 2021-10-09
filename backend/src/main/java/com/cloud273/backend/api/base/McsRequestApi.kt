package com.cloud273.backend.api.base

import android.app.Application
import com.cloud273.backend.apiUrl
import com.dungnguyen.qdcore.extension.versionCode
import kotlin.coroutines.CoroutineContext

open class McsRequestApi : QDJsonRequestApi() {

    companion object {
        const val expiredTokenNotification = "expiredTokenNotification"
        private lateinit var app: Application
        val application: Application
            get() = app
        fun setApplication(application: Application) {
            this.app = application
        }
    }

    override fun baseUrl() : String {
        return apiUrl
    }

    override fun logEnable(): Boolean {
        return true
    }

    override fun headers() : Map <String, String>? {
        val result = mutableMapOf<String, String>()
        result["app"] = application.packageName + "|" + application.versionCode
        result["os"] = "android"
        return result
    }

    override fun timeout() : Int {
        return 30
    }

    open fun commonFetch(context: CoroutineContext, completion: (code: Int, message: String?, body: String?) -> Unit) {
        fetch(context, completion)
    }

}