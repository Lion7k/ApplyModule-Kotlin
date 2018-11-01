package com.liuzq.httplibrary.download

import com.liuzq.commlibrary.utils.ToastUtils
import com.liuzq.httplibrary.exception.ApiException
import io.reactivex.Observer
import okhttp3.ResponseBody

/**
 * desc 文件下载基础类
 */
interface BaseDownloadObserver : Observer<ResponseBody> {

    /**
     * 失败回调
     *
     * @param errorMsg 错误信息
     */
    fun doOnError(errorMsg: String?)

    override fun onError(e: Throwable) {
        val error = ApiException.handleException(e).getmMessage()
        setError(error)
    }

    private fun setError(errorMsg: String?) {
        ToastUtils.show(errorMsg!!)
        doOnError(errorMsg)
    }
}