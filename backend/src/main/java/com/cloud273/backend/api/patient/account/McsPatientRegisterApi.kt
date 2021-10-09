package com.cloud273.backend.api.patient.account

import com.cloud273.backend.api.base.McsRequestApi
import com.cloud273.backend.model.common.McsLanguage
import com.cloud273.backend.model.common.McsNotifyType
import com.dungnguyen.qdcore.extension.toObject
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers

class McsPatientRegisterApi(private val username: String, private val password: String, private val language: McsLanguage) : McsRequestApi() {

    private data class Output(@SerializedName("code") val type: McsNotifyType)

    override fun api() : String {
        return "patient/register"
    }

    override fun headers(): Map<String, String>? {
        val result = super.headers()!!.toMutableMap()
        result["language"] = language.value
        return result
    }

    override fun body(): Any? {
        return mapOf(
            "username" to username,
            "password" to password
        )
    }

    override fun method(): HttpMethod {
        return HttpMethod.Post
    }

    // Error description
    // 409 Existed account
    fun run(completion: (success: Boolean, type: McsNotifyType?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 201) {
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