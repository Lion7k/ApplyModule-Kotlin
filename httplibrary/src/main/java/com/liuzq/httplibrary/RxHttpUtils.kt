package com.liuzq.httplibrary

import android.app.Application
import android.content.Context

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

        lateinit var context: Application

        fun getContext(): Context {
            checkInitialize()
            return context
        }

        fun config() {
            checkInitialize()
        }

        private fun checkInitialize() {
            context != null ?: throw ExceptionInInitializerError("请先在全局Application中调用 RxHttpUtils.getInstance().init(this) 初始化！")
        }

        fun <K> createApi(cls: Class<K>): K {

        }
    }

    fun init(app: Application): RxHttpUtils {
        context = app
        return this
    }


}
