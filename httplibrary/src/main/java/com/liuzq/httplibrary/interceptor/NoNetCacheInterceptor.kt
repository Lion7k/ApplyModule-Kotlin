package com.liuzq.httplibrary.interceptor

import com.liuzq.httplibrary.utils.NetUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * desc    : 网络缓存
 */
class NoNetCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val connected: Boolean = NetUtils.isNetworkConnected()
        //如果没有网络，则启用 FORCE_CACHE
        if (!connected) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()

            val response: Response = chain.proceed(request)

            //没网的时候如果也没缓存的话就走网络
            if (response.code() == 504) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build()
                return chain.proceed(request)
            }

            return response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=3600")
                    .removeHeader("Pragma")
                    .build()
        }

        //有网络的时候，这个拦截器不做处理，直接返回
        return chain.proceed(request)
    }
}