package com.cloud273.backend.api.base

import com.cloud273.backend.model.common.McsBase
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers

abstract class McsPartialUpdateApi(private val api: String, token: String, private val obj: McsBase) : McsUserRequestApi(token) {

    init {
        obj.validPartialUpdate()
    }

    private data class Output(val message: String)

    override fun api(): String {
        return api
    }

    override fun body(): Any? {
        return obj
    }

    override fun method(): HttpMethod {
        return HttpMethod.Patch
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