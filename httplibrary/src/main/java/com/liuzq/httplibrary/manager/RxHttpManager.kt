package com.liuzq.httplibrary.manager

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.HashMap

/**
 * desc    : 管理请求--用于取消请求时候使用
 */
class RxHttpManager private constructor() : IRxHttpManager<Any> {

    private var mMaps: HashMap<Any, CompositeDisposable>? = null

    init {
        mMaps = HashMap()
    }

    companion object {

        @Volatile
        private var INSTANCE: RxHttpManager? = null

        val instance: RxHttpManager
            get() {
                if (INSTANCE == null) {
                    synchronized(RxHttpManager::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = RxHttpManager()
                        }
                    }
                }
                return INSTANCE!!
            }
    }

    override fun add(tag: Any?, disposable: Disposable) {
        if (null == tag) return

        //tag下的一组或一个请求，用来处理一个页面的所以请求或者某个请求
        //设置一个相同的tag就行就可以取消当前页面所有请求或者某个请求了
        val compositeDisposable: CompositeDisposable? = mMaps?.get(tag)
        if (compositeDisposable == null) {
            val compositeDisposableNew = CompositeDisposable()
            compositeDisposableNew.add(disposable)
            mMaps?.put(tag, compositeDisposableNew)
        } else {
            compositeDisposable.add(disposable)
        }
    }

    override fun remove(tag: Any?) {
        if (null == tag) return

        if (!mMaps?.isEmpty()!!) {
            mMaps?.remove(tag)
        }
    }

    override fun cancel(tag: Any?) {
        if (null == tag) return

        if (mMaps?.isEmpty()!!) return

        if (null == mMaps?.get(tag)) return

        if (!mMaps?.get(tag)?.isDisposed!!) {
            mMaps?.get(tag)?.dispose()
            mMaps?.remove(tag)
        }
    }

    override fun cancel(vararg tags: Any?) {
        if (tags.isEmpty()) return

        for (tag in tags) {
            cancel(tag)
        }
    }

    override fun cancelAll() {
        if (mMaps?.isEmpty()!!) return

        val it = mMaps?.entries?.iterator()
        while (it?.hasNext()!!) {
            val entry = it.next()
            val tag = entry.key
            cancel(tag)
        }
    }

}