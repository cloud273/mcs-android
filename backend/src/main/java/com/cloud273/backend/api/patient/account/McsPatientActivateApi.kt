package com.cloud273.backend.api.patient.account

import com.cloud273.backend.api.base.McsRequestApi
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers


class McsPatientActivateApi(private val username: String, private val code: String) : McsRequestApi() {

    private data class Output(val message: String)

    override fun api() : String {
        return "patient/activate"
    }

    override fun body(): Any? {
        return mapOf(
            "username" to username,
            "code" to code
        )
    }

    override fun method(): HttpMethod {
        return HttpMethod.Patch
    }

    // Error description
    // 403 Invalid code
    // 404 Not found
    // 406 Expired code
    fun run(completion: (success: Boolean, message: String?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toObject<Output>(Output::class.java)
                if (data != null) {
                    completion(true, data.message, code)
                } else {
                    completion(false, null, 500)
                }
            } else {
                completion(false, null, code)
            }
        }
    }

}