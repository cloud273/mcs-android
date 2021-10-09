package com.cloud273.backend.api.common

import com.cloud273.backend.api.base.McsRequestApi
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers

class McsUploadImageApi(private val image: ByteArray): McsRequestApi()  {

    private data class Output(val image: String)

    override fun api(): String {
        return "upload/image"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Post
    }

    override fun formData(): Map<String, FormData>? {
        return mapOf(
            "image" to FormData("image.png", image, "image/png")
        )
    }

    fun run(completion: (success: Boolean, image: String?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toObject<Output>(Output::class.java)
                if (data != null) {
                    completion(true, data.image, code)
                } else {
                    completion(false, null, 500)
                }
            } else {
                completion(false, null, code)
            }
        }
    }

}