package com.liuzq.httplibrary.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.liuzq.httplibrary.RxHttpUtils

object NetUtils {

    /**
     * 判断是否有网络
     *
     * @return 返回值
     */
    fun isNetworkConnected(): Boolean {
        val context = RxHttpUtils.getContext()
        if (context != null) {
            val mConnectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo: NetworkInfo? = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) return mNetworkInfo.isAvailable
        }
        return false
    }
}