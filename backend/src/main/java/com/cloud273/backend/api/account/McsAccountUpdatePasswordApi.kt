package com.cloud273.backend.api.account

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.model.common.McsAccountType
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers

class McsAccountUpdatePasswordApi (private val accountType: McsAccountType, token: String, private val password: String, private val newPassword: String): McsUserRequestApi(token) {

    private data class Output(val token: String)

    override fun api() : String {
        return "${accountType.value}/update-password"
    }

    override fun body(): Any? {
        return mapOf(
            "password" to password,
            "newPassword" to newPassword
        )
    }

    override fun method(): HttpMethod {
        return HttpMethod.Patch
    }

    // Error description
    // 403 Invalid/Expired token
    // 404 Invalid password
    fun run(completion: (success: Boolean, token: String?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
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