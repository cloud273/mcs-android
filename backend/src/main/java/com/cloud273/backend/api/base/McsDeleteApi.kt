package com.cloud273.backend.api.base

import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers

abstract class McsDeleteApi(private val api: String, token: String) : McsUserRequestApi(token) {

    private data class Output(val message: String)

    override fun api(): String {
        return api
    }

    override fun method(): HttpMethod {
        return HttpMethod.Delete
    }
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