package com.liuzq.httplibrary.interceptor

import com.liuzq.httplibrary.utils.NetUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * desc 网络通畅时缓存
 */
class NetCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val connected: Boolean = NetUtils.isNetworkConnected()
        if (connected) {
            //如果有网络，缓存60s
            val response: Response = chain.proceed(request)
            val maxTime = 60
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=$maxTime")
                    .build()
        }
        //如果没有网络，不做处理，直接返回
        return chain.proceed(request)
    }
}