package com.liuzq.httplibrary.interceptor

import android.app.Dialog
import com.liuzq.commlibrary.utils.LogUtils
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 控制操作线程的辅助类
 */
object Transformer {

    fun <T> switchSchedulers(): ObservableTransformer<T, T> {
        return switchSchedulers(null)
    }

    /**
     * 带参数  显示loading对话框
     *
     * @param dialog loading
     * @param <T>    泛型
     * @return 返回Observable
     */
    fun <T> switchSchedulers(dialog: Dialog?): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe { dialog?.show() }
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { dialog?.dismiss() }
        }
    }
}