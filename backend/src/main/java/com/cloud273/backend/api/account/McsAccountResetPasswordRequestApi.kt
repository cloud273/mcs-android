package com.cloud273.backend.api.account

import com.cloud273.backend.api.base.McsRequestApi
import com.cloud273.backend.model.common.McsAccountType
import com.cloud273.backend.model.common.McsLanguage
import com.cloud273.backend.model.common.McsNotifyType
import com.dungnguyen.qdcore.extension.toObject
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers

class McsAccountResetPasswordRequestApi(private val accountType: McsAccountType, private val username: String, private val language: McsLanguage) : McsRequestApi() {

    private data class Output(@SerializedName("code") val type: McsNotifyType)

    override fun api() : String {
        return "${accountType.value}/reset-password-request"
    }

    override fun headers(): Map<String, String>? {
        val result = super.headers()!!.toMutableMap()
        result["language"] = language.value
        return result
    }

    override fun body(): Any? {
        val result = mutableMapOf<String, String>()
        result["username"] = username
        return result
    }

    override fun method(): HttpMethod {
        return HttpMethod.Patch
    }

    // Error description
    // 404 Not found
    fun run(completion: (success: Boolean, type: McsNotifyType?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toObject<Output>(Output::class.java)
                if (data != null) {
                    completion(true, data.type, code)
                } else {
                    completion(false, null, 500)
                }
            } else {
                completion(false, null, code)
            }
        }
    }

}