package com.liuzq.httplibrary.download

import com.liuzq.httplibrary.manager.RxHttpManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.IOException

/**
 * desc 文件下载
 */
abstract class DownloadObserver(private val fileName: String) : BaseDownloadObserver {

    /**
     * 失败回调
     *
     * @param errorMsg errorMsg
     */
    abstract fun onError(errorMsg: String?)

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

    /**
     * 成功回调
     *
     * @param filePath filePath
     */
    /**
     * 成功回调
     *
     * @param bytesRead     已经下载文件的大小
     * @param contentLength 文件的大小
     * @param progress      当前进度
     * @param done          是否下载完成
     * @param filePath      文件路径
     */
    abstract fun onSuccess(bytesRead: Long, contentLength: Long, progress: Float, done: Boolean, filePath: String)

    override fun doOnError(errorMsg: String?) {
        onError(errorMsg)
    }

    override fun onSubscribe(d: Disposable) {
        RxHttpManager.instance.add(setTag(), d)
    }

    override fun onNext(t: ResponseBody) {
        Observable.just<ResponseBody>(t)
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<ResponseBody> {

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: ResponseBody) {
                        try {
                            DownloadManager()
                                    .saveFile(t, fileName, object : ProgressListener {
                                        override fun onResponseProgress(bytesRead: Long, contentLength: Long, progress: Int, done: Boolean, filePath: String) {
                                            Observable
                                                    .just(progress)
                                                    .distinctUntilChanged()
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe {
                                                        onSuccess(bytesRead, contentLength, progress.toFloat(), done, filePath)
                                                    }
                                        }
                                    })
                        } catch (e: IOException) {
                            Observable
                                    .just<String>(e.message)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe { t -> doOnError(t) }
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {
                    }
                })
    }

    override fun onComplete() {

    }
}