package com.liuzq.httplibrary.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*

class HeaderInterceptor constructor(private val headerMaps: Map<String, Any>?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request.Builder = chain.request().newBuilder()
        if (headerMaps != null && headerMaps.isNotEmpty()) {
            for ((key, value) in headerMaps) {
                request.addHeader(key, value.toString())
            }
        }
        return chain.proceed(request.build())
    }
}