package com.liuzq.httplibrary.observer

/**
 *  自定义Observer 处理string回调
 */
import android.text.TextUtils
import com.liuzq.commlibrary.utils.ToastUtils
import com.liuzq.httplibrary.base.BaseStringObserver
import io.reactivex.disposables.Disposable

abstract class StringObserver : BaseStringObserver() {

    /**
     * 失败回调
     *
     * @param errorMsg 错误信息
     */
    abstract fun onError(errorMsg: String?)

    /**
     * 成功回调
     *
     * @param data 结果
     */
    abstract fun onSuccess(data: String?)

    override fun doOnSubscribe(d: Disposable) {

    }

    override fun doOnError(errorMsg: String?) {
        if (!isHideToast() && !TextUtils.isEmpty(errorMsg)) {
            ToastUtils.show(errorMsg!!)
        }
        onError(errorMsg)
    }

    override fun doOnNext(string: String?) {
        onSuccess(string)
    }

    override fun doOnCompleted() {
    }
}