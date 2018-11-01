package com.liuzq.httplibrary.base

import com.liuzq.httplibrary.exception.ApiException
import com.liuzq.httplibrary.interfaces.ISubscriber
import com.liuzq.httplibrary.manager.RxHttpManager
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *  基类BaseObserver
 */
abstract class BaseObserver<T> : Observer<T>, ISubscriber<T> {

    /**
     * 是否隐藏toast
     *
     * @return
     */
    open fun isHideToast(): Boolean {
        return false
    }

    /**
     * 标记网络请求的tag
     * tag下的一组或一个请求，用来处理一个页面的所以请求或者某个请求
     * 设置一个tag就行就可以取消当前页面所有请求或者某个请求了
     *
     * @return string
     */
    open fun setTag(): String? {
        return null
    }

    override fun onSubscribe(d: Disposable) {
        RxHttpManager.instance.add(setTag(), d)
        doOnSubscribe(d)
    }

    override fun onNext(t: T) {
        doOnNext(t)
    }

    override fun onError(e: Throwable) {
        val error: String = ApiException.handleException(e).getmMessage()
        setError(error)
    }

    override fun onComplete() {
        doOnCompleted()
    }

    private fun setError(error: String?) {
        doOnError(error)
    }
}