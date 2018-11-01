package com.liuzq.httplibrary.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * 网络请求工具类---使用的是全局配置的变量
 */
class GlobalRxHttp private constructor() {

    init {
        retrofitServiceCache = HashMap()
    }

    companion object {
        /*单利*/
        @Volatile
        private var INSTANCE: GlobalRxHttp? = null
        /*获取单例*/
        val instance: GlobalRxHttp
            get() {
                if (INSTANCE == null) {
                    synchronized(GlobalRxHttp::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = GlobalRxHttp()
                        }
                    }
                }
                return INSTANCE!!
            }

        /**
         * 缓存retrofit针对同一个ApiService不会重复创建retrofit对象
         */
        private var retrofitServiceCache: HashMap<String, Any>? = null

        fun <K> createGApi(cls: Class<K>): K? {
            if (retrofitServiceCache == null) {
                retrofitServiceCache = HashMap()
            }

            var retrofitService: K? = retrofitServiceCache?.get(cls.canonicalName) as K
            if (retrofitService == null) {
                retrofitService = getGlobalRetrofit()?.create(cls)
                retrofitServiceCache?.put(cls.canonicalName, retrofitService!!)
            }
            return retrofitService
        }

        private fun getGlobalRetrofit(): Retrofit? {
            return RetrofitClient.instance.getRetrofit()
        }
    }

    /**
     * 设置baseUrl
     *
     * @param baseUrl
     * @return
     */
    fun setBaseUrl(baseUrl: String): GlobalRxHttp {
        getGlobalRetrofitBuilder()?.baseUrl(baseUrl)
        return this
    }

    /**
     * 设置自己的client
     *
     * @param okClient
     * @return
     */
    fun setOkClient(okClient: OkHttpClient): GlobalRxHttp {
        getGlobalRetrofitBuilder()?.client(okClient)
        return this
    }

    /**
     * 全局的 RetrofitBuilder
     *
     * @return
     */
    private fun getGlobalRetrofitBuilder(): Retrofit.Builder? {
        return RetrofitClient.instance.getRetrofitBuilder()
    }
}