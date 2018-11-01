package com.liuzq.httplibrary.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*

class HeaderInterceptor constructor(headerMaps: Map<String, Any>?) : Interceptor {
    private var headerMaps: Map<String, Any>? = TreeMap()

    init {
        this.headerMaps = headerMaps as TreeMap<String, Any>
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request.Builder = chain.request().newBuilder()
        if (headerMaps != null && headerMaps?.size!! > 0) {
            for ((key, value) in headerMaps!!) {
                request.addHeader(key, value.toString())
            }
        }
        return chain.proceed(request.build())
    }
}