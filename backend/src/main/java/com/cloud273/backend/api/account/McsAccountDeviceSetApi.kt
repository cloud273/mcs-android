package com.cloud273.backend.api.account

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.model.common.McsAccountType
import com.dungnguyen.qdcore.extension.deviceInformation
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers

class McsAccountDeviceSetApi (private val accountType: McsAccountType, token: String, private val deviceToken: String?): McsUserRequestApi(token) {

    private data class Output(val id: Int)

    override fun api() : String {
        return "${accountType.value}/device"
    }

    override fun body(): Any? {
        val result: MutableMap<String, Any> = mutableMapOf(
            "info" to deviceInformation,
            "os" to "android",
            "topic" to application.packageName,
            "production" to true
        )
        if (deviceToken != null) {
            result["token"] = deviceToken
        }
        return result
    }

    override fun method(): HttpMethod {
        return HttpMethod.Post
    }

    // Error description
    // 404 Not found
    fun run(completion: (success: Boolean, id: Int?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toObject<Output>(Output::class.java)
                if (data != null) {
                    completion(true, data.id, code)
                } else {
                    completion(false, null, 500)
                }
            } else {
                completion(false, null, code)
            }
        }
    }

}