package com.liuzq.httplibrary.config

import android.content.Context
import com.liuzq.httplibrary.cookie.CookieStore
import com.liuzq.httplibrary.interceptor.HeaderInterceptor
import com.liuzq.httplibrary.interceptor.RxHttpLogger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.InputStream

/**
 * desc 统一OkHttp配置信息
 */
class OkHttpConfig private constructor() {

    init {
        okHttpClientBuilder = OkHttpClient.Builder()
    }

    companion object {
        private var defaultCachePath: String? = null
        private val defaultCacheSize = (1024 * 1024 * 100).toLong()
        private val defaultTimeout: Long = 10

        private var okHttpClientBuilder: OkHttpClient.Builder? = null
        private var okHttpClient: OkHttpClient? = null

        @Volatile
        private var INSTANCE: OkHttpConfig? = null

        /*单利 双锁机制*/
        private val instance: OkHttpConfig
            get() {
                if (INSTANCE == null) {
                    synchronized(OkHttpConfig::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = OkHttpConfig()
                        }
                    }
                }
                return INSTANCE!!
            }

        fun getOkHttpClient(): OkHttpClient? {
            return okHttpClient
        }
    }

    class Builder constructor(context: Context) {
        private var headerMaps: Map<String, Any>? = null
        private var isDebug: Boolean = false
        private var isCache: Boolean = false
        private var cachePath: String? = null
        private var cacheMaxSize: Long = 0
        private var cookieStore: CookieStore? = null
        private var readTimeout: Long = 0
        private var writeTimeout: Long = 0
        private var connectTimeout: Long = 0
        private var bksFile: InputStream? = null
        private var password: String? = null
        private var certificates: Array<InputStream>? = null
        private var interceptors: Array<Interceptor>? = null

        fun setHeaders(headerMaps: Map<String, Any>?): Builder {
            this.headerMaps = headerMaps
            return this
        }

        fun setDebug(isDebug: Boolean): Builder {
            this.isDebug = isDebug
            return this
        }

        fun setCache(isCache: Boolean): Builder {
            this.isCache = isCache
            return this
        }

        fun setCachePath(cachePath: String): Builder {
            this.cachePath = cachePath
            return this
        }

        fun setCacheMaxSize(cacheMaxSize: Long): Builder {
            this.cacheMaxSize = cacheMaxSize
            return this
        }

        fun setCookieType(cookieStore: CookieStore): Builder {
            this.cookieStore = cookieStore
            return this
        }

        fun setReadTimeout(readTimeout: Long): Builder {
            this.readTimeout = readTimeout
            return this
        }

        fun setWriteTimeout(writeTimeout: Long): Builder {
            this.writeTimeout = writeTimeout
            return this
        }

        fun setConnectTimeout(connectTimeout: Long): Builder {
            this.connectTimeout = connectTimeout
            return this
        }

        fun setAddInterceptor(vararg interceptors: Interceptor): Builder {
            this.interceptors = interceptors.asList().toTypedArray()
            return this
        }

        fun setSslSocketFactory(vararg certificates: InputStream): Builder {
            this.certificates = certificates.asList().toTypedArray()
            return this
        }

        fun setSslSocketFactory(bksFile: InputStream, password: String, vararg certificates: InputStream): Builder {
            this.bksFile = bksFile
            this.password = password
            this.certificates = certificates.asList().toTypedArray()
            return this
        }

        fun build(): OkHttpClient? {

            OkHttpConfig.instance

            setCookieConfig()
            setCacheConfig()
            setHeadersConfig()
            setSslConfig()
            addInterceptors()
            setTimeout()
            setDebugConfig()

            okHttpClient = okHttpClientBuilder?.build()
            return okHttpClient
        }

        private fun addInterceptors() {
            if (null != interceptors) {
                for (interceptor in interceptors!!) {
                    okHttpClientBuilder?.addInterceptor(interceptor)
                }
            }
        }


        /**
         * 配置开发环境
         */
        private fun setDebugConfig() {
            if (isDebug) {
                val logInterceptor = HttpLoggingInterceptor(RxHttpLogger())
                logInterceptor.level = HttpLoggingInterceptor.Level.BODY
                okHttpClientBuilder?.addInterceptor(logInterceptor)
            }
        }

        /**
         * 配置headers
         */
        private fun setHeadersConfig() {
            okHttpClientBuilder?.addInterceptor(HeaderInterceptor(headerMaps))
        }

        /**
         * 配饰cookie保存到sp文件中
         */
        private fun setCookieConfig(){
            if (null != cookieStore){
                okHttpClientBuilder?.cookieJar()
            }
        }
    }
}