package com.liuzq.httplibrary.interceptor

import com.liuzq.commlibrary.utils.LogUtils
import com.liuzq.httplibrary.utils.JsonUtil
import okhttp3.logging.HttpLoggingInterceptor

/**
 * desc 日志打印格式化处理
 */
class RxHttpLogger : HttpLoggingInterceptor.Logger {
    private val mMessage = StringBuffer()

    override fun log(message: String) {
        // 请求或者响应开始
        if (message.startsWith("--> POST")) {
            mMessage.setLength(0)
            mMessage.append(" ")
            mMessage.append("\r\n")
        }
        if (message.startsWith("--> GET")) {
            mMessage.setLength(0)
            mMessage.append(" ")
            mMessage.append("\r\n")
        }
        var msg = ""
        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
        if ((message.startsWith("{") && message.endsWith("}"))
                || (message.startsWith("[") && message.endsWith("]"))) {
            msg = JsonUtil.formatJson(message)
        }

        mMessage.append(msg.contains("\n"))
        // 请求或者响应结束，打印整条日志
        if (msg.startsWith("<-- END HTTP")) {
            LogUtils.e("RxHttpUtils", mMessage.toString())
        }
    }
}