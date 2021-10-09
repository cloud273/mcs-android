package com.cloud273.backend.api.account

import com.cloud273.backend.api.base.McsRequestApi
import com.cloud273.backend.model.common.McsAccountType
import com.dungnguyen.qdcore.extension.deviceInformation
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers

class McsAccountLoginApi(private val accountType: McsAccountType, private val username: String, private val password: String, private val deviceToken: String?) : McsRequestApi() {

    private data class Output(val token: String)

    override fun api() : String {
        return "${accountType.value}/login"
    }

    override fun body(): Any? {
        val login = mapOf(
            "username" to username,
            "password" to password
        )
        val device = mutableMapOf <String, Any>(
            "info" to deviceInformation,
            "os" to "android",
            "topic" to application.packageName,
            "production" to true
        )
        if (deviceToken != null) {
            device["token"] = deviceToken
        }

        return mapOf(
            "login" to login,
            "device" to device
        )
    }

    override fun method(): HttpMethod {
        return HttpMethod.Post
    }

    // Error description
    // 401 Invalid password
    // 403 Inactivated account
    // 404 Not found
    // 423 Account is locked
    fun run(completion: (success: Boolean, token: String?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200 || code == 202) {
                val data = body?.toObject<Output>(Output::class.java)
                if (data != null) {
                    completion(true, data.token, code)
                } else {
                    completion(false, null, 500)
                }
            } else {
                completion(false, null, code)
            }
        }
    }

}