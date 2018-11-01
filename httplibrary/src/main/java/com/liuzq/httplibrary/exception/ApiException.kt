package com.liuzq.httplibrary.exception

import com.google.gson.JsonParseException
import com.google.gson.JsonSerializer
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.io.NotSerializableException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

class ApiException(throwable: Throwable, code: Int) : Exception(throwable) {

    private var mCode: Int? = code
    private var mMessage: String? = throwable.message

    fun getCode(): Int {
        return mCode!!
    }

    fun getmMessage(): String {
        return mMessage!!
    }

    companion object {
        fun handleException(e: Throwable): ApiException {
            val ex: ApiException
            when (e) {
                is HttpException -> {
                    ex = ApiException(e, e.code())
                    ex.mMessage = e.message
                }
                is SocketTimeoutException -> {
                    ex = ApiException(e, ERROR.TIMEOUT_ERROR)
                    ex.mMessage = "网络连接超时，请检查您的网络状态，稍后重试！"
                }
                is ConnectException -> {
                    ex = ApiException(e, ERROR.TIMEOUT_ERROR)
                    ex.mMessage = "网络连接异常，请检查您的网络状态，稍后重试！"
                }
                is ConnectTimeoutException -> {
                    ex = ApiException(e, ERROR.TIMEOUT_ERROR)
                    ex.mMessage = "网络连接超时，请检查您的网络状态，稍后重试！"
                }
                is UnknownHostException -> {
                    ex = ApiException(e, ERROR.TIMEOUT_ERROR)
                    ex.mMessage = "网络连接异常，请检查您的网络状态，稍后重试！"
                }
                is NullPointerException -> {
                    ex = ApiException(e, ERROR.NULL_POINTER_EXCEPTION)
                    ex.mMessage = "空指针异常"
                }
                is javax.net.ssl.SSLHandshakeException -> {
                    ex = ApiException(e, ERROR.SSL_ERROR)
                    ex.mMessage = "证书验证失败"
                }
                is ClassCastException -> {
                    ex = ApiException(e, ERROR.CAST_ERROR)
                    ex.mMessage = "类型转换错误"
                }
                is JsonParseException,
                is JSONException,
                is JsonSerializer<*>,
                is NotSerializableException,
                is ParseException -> {
                    ex = ApiException(e, ERROR.PARSE_ERROR)
                    ex.mMessage = "解析错误"
                }
                is IllegalStateException -> {
                    ex = ApiException(e, ERROR.ILLEGAL_STATE_ERROR)
                    ex.mMessage = e.message
                }
                else -> {
                    ex = ApiException(e, ERROR.UNKNOWN)
                }
            }
            return ex
        }
    }

    /**
     * 约定异常
     */
    class ERROR {
        companion object {
            /**
             * 未知错误
             */
            const val UNKNOWN = 1000

            /**
             * 连接超时
             */
            const val TIMEOUT_ERROR = 1001
            /**
             * 空指针错误
             */
            const val NULL_POINTER_EXCEPTION = 1002

            /**
             * 证书出错
             */
            const val SSL_ERROR = 1003

            /**
             * 类转换错误
             */
            const val CAST_ERROR = 1004

            /**
             * 解析错误
             */
            const val PARSE_ERROR = 1005

            /**
             * 非法数据异常
             */
            const val ILLEGAL_STATE_ERROR = 1006
        }
    }
}