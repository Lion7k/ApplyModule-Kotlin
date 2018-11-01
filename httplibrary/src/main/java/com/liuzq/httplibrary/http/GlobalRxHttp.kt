package com.liuzq.httplibrary.http

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
        lateinit var retrofitServiceCache: HashMap<String, Any>
    }

    /**
     * 全局的 RetrofitBuilder
     *
     * @return
     */
    fun getGlobalRetrofitBuilder(): Retrofit.Builder {
        return RetrofitClient.instance.getR
    }
}