package com.liuzq.httplibrary.http

import com.liuzq.httplibrary.config.OkHttpConfig
import com.liuzq.httplibrary.gson.GsonAdapter
import com.liuzq.httplibrary.interceptor.RxHttpLogger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * RetrofitClient工具类
 */
class RetrofitClient private constructor() {
    private var mRetrofitBuilder: Retrofit.Builder? = null
    private var okHttpClient: OkHttpClient? = null

    init {
        initDefaultOkHttpClient()

        mRetrofitBuilder = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonAdapter.buildGson()))
    }

    companion object {
        @Volatile
        private var INSTANCE: RetrofitClient? = null

        /*单利 双锁机制*/
        val instance: RetrofitClient
            get() {
                if (INSTANCE == null) {
                    synchronized(RetrofitClient::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = RetrofitClient()
                        }
                    }
                }
                return INSTANCE!!
            }
    }

    private fun initDefaultOkHttpClient() {
        val builder = OkHttpClient.Builder()

        builder.readTimeout(10, TimeUnit.SECONDS)
        builder.writeTimeout(10, TimeUnit.SECONDS)
        builder.connectTimeout(10, TimeUnit.SECONDS)

        val sslParams: SSLUtils.SSLParams = SSLUtils.getSslSocketFactory()
        builder.sslSocketFactory(sslParams.sSLSocketFactory!!, sslParams.trustManager!!)

        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor(RxHttpLogger())
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)

        okHttpClient = builder.build()
    }

    fun getRetrofitBuilder(): Retrofit.Builder? {
        return mRetrofitBuilder
    }

    fun getRetrofit(): Retrofit? {
        return if (null == OkHttpConfig.getOkHttpClient())
            mRetrofitBuilder?.client(okHttpClient!!)?.build()
        else
            mRetrofitBuilder?.client(OkHttpConfig.getOkHttpClient()!!)?.build()
    }
}