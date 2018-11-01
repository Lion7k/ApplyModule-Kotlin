package com.liuzq.httplibrary.observer

import android.text.TextUtils
import com.liuzq.commlibrary.utils.ToastUtils
import com.liuzq.httplibrary.base.BaseDataObserver
import com.liuzq.httplibrary.bean.BaseData
import io.reactivex.disposables.Disposable

/**
 * 针对特定格式的时候设置的通用的Observer
 * 用户可以根据自己需求自定义自己的类继承BaseDataObserver<T>即可
 * 适用于
 * {
 * "code":200,
 * "msg":"成功"
 * "data":{
 * "userName":"test"
 * "token":"abcdefg123456789"
 * "uid":"1"}
 */
abstract class DataObserver<T> : BaseDataObserver<T>() {

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
    abstract fun onSuccess(data: T?)

    override fun doOnSubscribe(d: Disposable) {
    }

    override fun doOnError(errorMsg: String?) {
        if (!isHideToast() && !TextUtils.isEmpty(errorMsg)) {
            ToastUtils.show(errorMsg!!)
        }
        onError(errorMsg)
    }

    override fun doOnNext(baseData: BaseData<T>?) {
        onSuccess(baseData?.data)
        //可以根据需求对code统一处理
//        when (baseData?.code) {
//            200 -> {
//                onSuccess(baseData.data)
//            }
//            else -> {
//                onError(baseData?.msg)
//            }
//        }
    }

    override fun doOnCompleted() {

    }
}