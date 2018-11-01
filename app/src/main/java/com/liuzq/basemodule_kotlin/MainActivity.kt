package com.liuzq.basemodule_kotlin

import android.app.AlertDialog
import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.liuzq.basemodule_kotlin.api.ApiService
import com.liuzq.basemodule_kotlin.bean.BookBean
import com.liuzq.commlibrary.utils.LogUtils
import com.liuzq.commlibrary.utils.ToastUtils
import com.liuzq.httplibrary.RxHttpUtils
import com.liuzq.httplibrary.download.DownloadObserver
import com.liuzq.httplibrary.interceptor.Transformer
import com.liuzq.httplibrary.observer.CommonObserver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private var loading_dialog: Dialog? = null

    internal var url = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk"
    internal val fileName = "alipay.apk"
    internal var filePath = Environment.getExternalStorageDirectory().path + "/" + "meinv.jpg"  //需要在手机Root目录下存放一张图片


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loading_dialog = AlertDialog.Builder(this).setMessage("loading...").create()

        tv.setOnClickListener {
            singleDownload()
        }
    }

    /**
     * 全局请求方式
     */
    private fun globalRequest() {
        RxHttpUtils.createApi(ApiService::class.java)
                ?.getBook()
                ?.compose(Transformer.switchSchedulers(loading_dialog))
                ?.subscribe(object : CommonObserver<BookBean>() {
                    //默认false   隐藏onError的提示
                    override fun isHideToast(): Boolean {
                        return false
                    }

                    override fun setTag(): String? {
                        return "tag1"
                    }

                    override fun onError(errorMsg: String) {
                    }

                    override fun onSuccess(t: BookBean?) {
                        LogUtils.e(TAG, t?.summary)
                    }
                })
    }

    /**
     * 全局下载
     */
    private fun globalDownload() {
        RxHttpUtils
                .createApi(ApiService::class.java)
                ?.downloadFile(url)
                ?.compose(Transformer.switchSchedulers())
                ?.subscribe(object : DownloadObserver(fileName) {
                    //可以去下下载
                    override fun setTag(): String? {
                        return "download"
                    }

                    override fun onError(errorMsg: String?) {
                        ToastUtils.show(errorMsg!!)
                    }

                    override fun onSuccess(bytesRead: Long, contentLength: Long, progress: Float, done: Boolean, filePath: String) {
                        LogUtils.e("下载中：$progress% == 下载文件路径：$filePath")
                    }
                })
    }

    /**
     * 单个下载
     */
    private fun singleDownload() {
        RxHttpUtils
                .downloadFile(url)
                ?.subscribe(object : DownloadObserver(fileName) {
                    //可以去下下载
                    override fun setTag(): String? {
                        return "download"
                    }

                    override fun onError(errorMsg: String?) {
                        ToastUtils.show(errorMsg!!)
                    }

                    override fun onSuccess(bytesRead: Long, contentLength: Long, progress: Float, done: Boolean, filePath: String) {
                        LogUtils.e("下载中：$progress% == 下载文件路径：$filePath")
                    }
                })
    }
}
