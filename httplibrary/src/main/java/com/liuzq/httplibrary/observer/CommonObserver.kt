package com.liuzq.httplibrary.observer

import android.text.TextUtils
import com.liuzq.commlibrary.utils.ToastUtils
import com.liuzq.httplibrary.base.BaseObserver
import io.reactivex.disposables.Disposable

abstract class CommonObserver<T> : BaseObserver<T>() {

    /**
     * 失败回调
     *
     * @param errorMsg
     */
    abstract fun onError(errorMsg: String)

    /**
     * 成功回调
     *
     * @param t
     */
    abstract fun onSuccess(t: T?)

    override fun doOnSubscribe(d: Disposable) {
    }

    override fun doOnError(errorMsg: String?) {
        if (!isHideToast() && !TextUtils.isEmpty(errorMsg)) {
            ToastUtils.show(errorMsg!!)
        }
        onError(errorMsg!!)
    }

    override fun doOnNext(t: T?) {
        onSuccess(t)
    }

    override fun doOnCompleted() {
    }
}