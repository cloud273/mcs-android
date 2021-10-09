package com.cloud273.backend.api.account

import com.cloud273.backend.api.base.McsRequestApi
import com.cloud273.backend.model.common.McsAccountType
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers

class McsAccountResetPasswordApi(private val accountType: McsAccountType, private val username: String, private val password: String, private val code: String) : McsRequestApi() {

    private data class Output(val message: String)

    override fun api() : String {
        return "${accountType.value}/reset-password"
    }

    override fun body(): Any? {
        return mapOf(
            "username" to username,
            "password" to password,
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