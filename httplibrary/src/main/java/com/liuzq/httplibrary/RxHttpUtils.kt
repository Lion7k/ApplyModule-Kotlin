package com.liuzq.httplibrary

import android.app.Application
import android.content.Context
import com.liuzq.httplibrary.download.DownloadRetrofit
import com.liuzq.httplibrary.http.GlobalRxHttp
import io.reactivex.Observable
import okhttp3.ResponseBody

class RxHttpUtils private constructor() {

    //伴生对象单利模式，类似java双锁机制
    companion object {
        /*单例*/
        @Volatile
        private var INSTANCE: RxHttpUtils? = null
        /*获取单例*/
        val instance: RxHttpUtils
            get() {
                if (INSTANCE == null) {
                    synchronized(RxHttpUtils::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = RxHttpUtils()
                        }
                    }
                }
                return INSTANCE!!
            }

        private var context: Application? = null

        fun getContext(): Context? {
            checkInitialize()
            return context
        }

        private fun checkInitialize() {
            if (context == null) {
                throw ExceptionInInitializerError("请先在全局Application中调用 RxHttpUtils.getInstance().init(this) 初始化！")
            }
        }


        fun <K> createApi(cls: Class<K>): K? {
            return GlobalRxHttp.createGApi(cls)
        }

        /**
         * 下载文件
         *
         * @param fileUrl 地址
         * @return ResponseBody
         */
        fun downloadFile(fileUrl: String): Observable<ResponseBody>? {
            return DownloadRetrofit.downloadFile(fileUrl)
        }

    }

    fun init(app: Application): RxHttpUtils {
        context = app
        return this
    }

    fun config(): GlobalRxHttp {
        checkInitialize()
        return GlobalRxHttp.instance
    }
}
