package com.cloud273.backend.api.base

import com.cloud273.backend.model.common.McsBase
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers

abstract class McsUpdateApi(private val api: String, token: String, private val obj: McsBase) : McsUserRequestApi(token) {

    init {
        obj.validUpdate()
    }

    private data class Output(val message: String)

    override fun api(): String {
        return api
    }

    override fun body(): Any? {
        return obj
    }

    override fun method(): HttpMethod {
        return HttpMethod.Put
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