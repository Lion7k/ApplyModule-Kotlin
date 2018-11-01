package com.liuzq.httplibrary.download

import com.liuzq.httplibrary.interceptor.Transformer
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * desc 下载单独建一个retrofit
 */
class DownloadRetrofit private constructor() {

    private var mRetrofit: Retrofit? = null

    init {
        mRetrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
    }

    companion object {
        private const val baseUrl = "https://api.github.com/"

        @Volatile
        private var INSTANCE: DownloadRetrofit? = null

        /*单利 双锁机制*/
        val instance: DownloadRetrofit
            get() {
                if (INSTANCE == null) {
                    synchronized(DownloadRetrofit::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = DownloadRetrofit()
                        }
                    }
                }
                return INSTANCE!!
            }

        fun downloadFile(fileUrl: String): Observable<ResponseBody>? {
            return DownloadRetrofit
                    .instance
                    .getRetrofit()
                    ?.create(DownloadApi::class.java)
                    ?.downloadFile(fileUrl)
                    ?.compose(Transformer.switchSchedulers())
        }
    }

    fun getRetrofit(): Retrofit? {
        return mRetrofit
    }
}