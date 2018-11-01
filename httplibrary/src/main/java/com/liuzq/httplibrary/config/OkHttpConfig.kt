package com.liuzq.httplibrary.config

import android.content.Context
import android.text.TextUtils
import com.liuzq.httplibrary.cookie.CookieJarImpl
import com.liuzq.httplibrary.cookie.store.CookieStore
import com.liuzq.httplibrary.http.SSLUtils
import com.liuzq.httplibrary.interceptor.HeaderInterceptor
import com.liuzq.httplibrary.interceptor.NetCacheInterceptor
import com.liuzq.httplibrary.interceptor.NoNetCacheInterceptor
import com.liuzq.httplibrary.interceptor.RxHttpLogger
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.InputStream
import java.util.concurrent.TimeUnit

/**
 * desc 统一OkHttp配置信息
 */
class OkHttpConfig private constructor() {

    init {
        okHttpClientBuilder = OkHttpClient.Builder()
    }

    companion object {
        private var defaultCachePath: String? = null
        private const val defaultCacheSize = (1024 * 1024 * 100).toLong()
        private const val defaultTimeout: Long = 10

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

    class Builder constructor(private val context: Context?) {
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
        private fun setCookieConfig() {
            if (null != cookieStore) {
                okHttpClientBuilder?.cookieJar(CookieJarImpl(cookieStore))
            }
        }

        /**
         * 配置缓存
         */
        private fun setCacheConfig() {
            val externalCacheDir: File = context?.externalCacheDir ?: return
            defaultCachePath = externalCacheDir.path + "/RxHttpCacheData"
            if (isCache) {
                val cache: Cache = if (!TextUtils.isEmpty(cachePath) && cacheMaxSize > 0)
                    Cache(File(cachePath), cacheMaxSize)
                else
                    Cache(File(defaultCachePath), defaultCacheSize)

                okHttpClientBuilder?.cache(cache)
                        ?.addInterceptor(NoNetCacheInterceptor())
                        ?.addNetworkInterceptor(NetCacheInterceptor())
            }
        }

        /**
         * 配置超时信息
         */
        private fun setTimeout() {
            okHttpClientBuilder?.readTimeout(if (readTimeout == 0L) defaultTimeout else readTimeout, TimeUnit.SECONDS)
            okHttpClientBuilder?.writeTimeout(if (writeTimeout == 0L) defaultTimeout else writeTimeout, TimeUnit.SECONDS)
            okHttpClientBuilder?.connectTimeout(if (connectTimeout == 0L) defaultTimeout else connectTimeout, TimeUnit.SECONDS)
            okHttpClientBuilder?.retryOnConnectionFailure(true)
        }

        /**
         * 配置证书
         */
        private fun setSslConfig() {
            val sslParams: SSLUtils.SSLParams? = if (null == certificates) {
                //信任所有证书,不安全有风险
                SSLUtils.getSslSocketFactory()
            } else {
                if (null != bksFile && !TextUtils.isEmpty(password)) {
                    //使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
                    SSLUtils.getSslSocketFactory(bksFile, password, *certificates!!)
                } else {
                    //使用预埋证书，校验服务端证书（自签名证书）
                    SSLUtils.getSslSocketFactory(*certificates!!)
                }
            }
            okHttpClientBuilder?.sslSocketFactory(sslParams?.sSLSocketFactory!!, sslParams.trustManager!!)
        }
    }
}