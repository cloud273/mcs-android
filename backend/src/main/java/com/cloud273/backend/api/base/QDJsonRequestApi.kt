package com.cloud273.backend.api.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

abstract class QDJsonRequestApi {

    class FormData (val filename: String, val data: ByteArray, val mime: String)

    enum class HttpMethod (val value: String) {
        Post ("Post"),
        Get ("Get"),
        Put("Put"),
        Patch("Patch"),
        Delete("Delete")
    }

    @JvmSuppressWildcards
    private interface Api {

        @GET
        fun get(@Url url: String,
                     @HeaderMap headers: Map<String, Any>?): Call<ResponseBody>

        @POST
        fun post(@Url url: String,
                 @HeaderMap headers: Map<String, Any>?,
                 @Body body: Any?): Call<ResponseBody>

        @POST
        @Multipart
        fun postMultipart(@Url url: String,
                          @HeaderMap headers: Map<String, Any>?,
                          @PartMap parameters: Map<String, RequestBody>,
                          @Part parts: List<MultipartBody.Part>): Call<ResponseBody>

        @PUT
        fun put(@Url url: String,
                @HeaderMap headers: Map<String, Any>?,
                @Body body: Any?): Call<ResponseBody>

        @PATCH
        fun patch(@Url url: String,
                  @HeaderMap headers: Map<String, String>?,
                  @Body body: Any?): Call<ResponseBody>

        @DELETE
        fun delete(@Url url: String,
                @HeaderMap headers: Map<String, Any>?): Call<ResponseBody>

    }

    private var api: Api

    init {
        api = Retrofit.Builder()
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .baseUrl(baseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    protected open fun baseUrl() : String {
        return ""
    }

    protected open fun logEnable(): Boolean {
        return true
    }

    protected open fun api() : String {
        return ""
    }

    protected open fun method() : HttpMethod {
        return HttpMethod.Post
    }

    protected open fun headers() : Map <String, String>? {
        return null
    }

    protected open fun timeout() : Int {
        return 30
    }

    protected open fun body() : Any? {
        return null
    }

    protected open fun formData() : Map <String, FormData>? {
        return null
    }

    private fun log(code: Int, message: String?, body: String?) {
        val log = "url = ${baseUrl() + api()}" +
                "\nheaders = ${headers()}" +
                "\nbody = ${body()}" +
                "\nformData = ${formData()}" +
                "\nmethod = ${method()}" +
                "\ncode = $code" +
                "\nmessage = $message" +
                "\nbody = $body"
        println(log)
    }

    private fun logException(t: Throwable?) {
        var log = "url = ${baseUrl() + api()}\nheaders = ${headers()}\nbody = ${body()}\nformData = ${formData()}\nmethod = ${method()}"
        log += "\nerror = $t"
        println(log)
    }

    fun fetch(context: CoroutineContext = Dispatchers.Main, completion: (code: Int, message: String?, body: String?) -> Unit) {
        val call:  Call<ResponseBody>
        val url = baseUrl() + api()
        val headers = headers()
        val parameters = body()
        val formData = formData()
        when (method()) {
            HttpMethod.Post -> {
                call = if (formData == null || formData.isEmpty()) {
                    api.post(url, headers, parameters)
                } else {
                    val params = mutableMapOf<String, RequestBody>()
                    if (parameters != null) {
                        val map = parameters as Map<String, String>
                        for ((key, obj) in map) {
                            params[key] = RequestBody.create(MultipartBody.FORM, obj)
                        }
                    }
                    val parts = mutableListOf<MultipartBody.Part>()
                    for ((key, obj) in formData) {
                        parts.add(MultipartBody.Part.createFormData(key, obj.filename, RequestBody.create(MediaType.parse(obj.mime), obj.data)))
                    }
                    api.postMultipart(url, headers, params, parts)
                }
            }
            HttpMethod.Put -> {
                call = api.put(url, headers, parameters)
            }
            HttpMethod.Patch -> {
                call = api.patch(url, headers, parameters)
            }
            HttpMethod.Delete -> {
                call = api.delete(url, headers)
            }
            else -> {
                call = api.get(url, headers)
            }
        }
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                val code = response!!.code()
                val message = response.message()
                val body = response.body()?.string()
                if (logEnable()) {
                    log(code, message, body)
                }
                GlobalScope.launch(context) {
                    completion(code, message, body)
                }
            }
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                if (logEnable()) {
                    logException(t)
                }
                GlobalScope.launch(context) {
                    completion(-1, t?.message,null)
                }
            }
        })
    }

}