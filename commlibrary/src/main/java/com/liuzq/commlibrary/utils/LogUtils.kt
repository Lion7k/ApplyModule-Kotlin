package com.liuzq.commlibrary.utils

import android.os.Debug
import android.os.Environment
import android.util.Log
import com.liuzq.commlibrary.BuildConfig
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception

object LogUtils {
    private const val MIN_STACK_OFFSET = 3// starts at this class after two native calls
    private const val MAX_STACK_TRACE_SIZE = 131071 //128 KB - 1
    private const val METHOD_COUNT = 0 // show method count in trace
    private var isDebug = BuildConfig.LOG_DEBUG// 是否调试模式
    private var debugTag = BuildConfig.VERSION_NAME// LogCat的标记

    fun setIsDebug(isDebug: Boolean) {
        LogUtils.isDebug = isDebug
    }

    fun isDebug(): Boolean {
        return isDebug
    }

    fun setDebugTag(debugTag: String) {
        LogUtils.debugTag = debugTag
    }

    fun getDebugTag(): String {
        return debugTag
    }

    /**
     * Verbose.
     *
     * @param message the message
     */
    fun v(message: String?) {
        v("", message)
    }

    /**
     * Verbose.
     *
     * @param any  the any
     * @param message the message
     */
    fun v(any: Any, message: String?) {
        v(any.javaClass.simpleName.toString(), message)
    }

    fun v(tag: String, msg: String?) {
        val tempTag: String
        val tempMsg: String
        if (isDebug) {
            tempTag = debugTag + (if (tag.isBlank()) "" else "-") + tag
            tempMsg = msg + getTraceElement()
            Log.v(tempTag, tempMsg)
        }
    }

    /**
     * Debug.
     *
     * @param message the message
     */
    fun d(message: String?) {
        d("", message)
    }

    /**
     * Debug.
     *
     * @param any  the any
     * @param message the message
     */
    fun d(any: Any, message: String?) {
        d(any.javaClass.simpleName, message)
    }

    /**
     * 记录“debug”级别的信息
     *
     * @param tag the tag
     * @param msg the msg
     */
    fun d(tag: String, msg: String?) {
        val tempTag: String
        val tempMsg: String
        if (isDebug) {
            tempTag = debugTag + (if (tag.isBlank()) "" else "-") + tag
            tempMsg = msg + getTraceElement()
            Log.d(tempTag, tempMsg)
        }
    }

    /**
     * Warn.
     *
     * @param e the e
     */
    fun w(e: Throwable) {
        w(toStackTraceString(e))
    }

    /**
     * Warn.
     *
     * @param message the message
     */
    fun w(message: String?) {
        w("", message)
    }

    /**
     * Warn.
     *
     * @param any  the any
     * @param message the message
     */
    fun w(any: Any, message: String?) {
        w(any.javaClass.simpleName, message)
    }

    /**
     * Warn.
     *
     * @param any the any
     * @param e      the e
     */
    fun w(any: Any, e: Throwable) {
        w(any.javaClass.simpleName, toStackTraceString(e))
    }

    /**
     * 记录“warn”级别的信息
     *
     * @param tag the tag
     * @param msg the msg
     */
    fun w(tag: String, msg: String?) {
        val tempTag: String
        val tempMsg: String
        if (isDebug) {
            tempTag = debugTag + (if (tag.isBlank()) "" else "-") + tag
            tempMsg = msg + getTraceElement()
            Log.w(tempTag, tempMsg)
        }
    }

    /**
     * Error.
     *
     * @param e the e
     */
    fun e(e: Throwable) {
        LogUtils.e(toStackTraceString(e))
    }

    /**
     * Error.
     *
     * @param message the message
     */
    fun e(message: String?) {
        e("", message)
    }

    /**
     * Error.
     *
     * @param any  the any
     * @param message the message
     */
    fun e(any: Any, message: String?) {
        e(any.javaClass.simpleName, message)
    }

    /**
     * Error.
     *
     * @param any the any
     * @param e      the e
     */
    fun e(any: Any, e: Throwable) {
        e(any.javaClass.simpleName, toStackTraceString(e))
    }

    /**
     * 记录“error”级别的信息
     *
     * @param tag the tag
     * @param msg the msg
     */
    fun e(tag: String, msg: String?) {
        val tempTag: String
        val tempMsg: String
        if (isDebug) {
            tempTag = debugTag + (if (tag.isBlank()) "" else "-") + tag
            tempMsg = msg + getTraceElement()
            Log.e(tempTag, tempMsg)
        }
    }

    /**
     * 在某个方法中调用生成.trace文件。然后拿到电脑上用DDMS工具打开分析
     *
     * @see #stopMethodTracing()
     */
    fun startMethodTracing() {
        if (isDebug) {
            Debug.startMethodTracing(Environment.getExternalStorageDirectory().absolutePath + File.separator + debugTag + ".trace")
        }
    }

    /**
     * Stop method tracing.
     */
    fun stopMethodTracing() {
        if (isDebug) {
            Debug.stopMethodTracing()
        }
    }

    /**
     * To stack trace string string.
     * <p>
     * 此方法参见：https://github.com/Ereza/CustomActivityOnCrash
     *
     * @param throwable the throwable
     * @return the string
     */
    private fun toStackTraceString(throwable: Throwable): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        throwable.printStackTrace(pw)
        var stackTraceString = sw.toString()
        //Reduce data to 128KB so we don't get a TransactionTooLargeException when sending the intent.
        //The limit is 1MB on Android but some devices seem to have it lower.
        //See: http://developer.android.com/reference/android/os/TransactionTooLargeException.html
        //And: http://stackoverflow.com/questions/11451393/what-to-do-on-transactiontoolargeexception#comment46697371_12809171
        if (stackTraceString.length > MAX_STACK_TRACE_SIZE) {
            val disclaimer = " [stack trace too large]"
            stackTraceString = stackTraceString.substring(0, MAX_STACK_TRACE_SIZE - disclaimer.length) + disclaimer
        }
        pw.close()
        return stackTraceString
    }

    /**
     * 可显示调用方法所在的文件行号，在AndroidStudio的logcat处可点击定位。
     * 此方法参考：https://github.com/orhanobut/logger
     */
    private fun getTraceElement(): String {
        try {
            var methodCount = METHOD_COUNT
            val trace = Thread.currentThread().stackTrace
            val stackOffset = _getStackOffset(trace)
            //corresponding method count with the current stack may exceeds the stack trace. Trims the count
            if (methodCount + stackOffset > trace.size) {
                methodCount = trace.size - stackOffset - 1
            }
            var level = "    "
            val builder = StringBuilder()
            for (i in methodCount downTo 1) {
                val stackIndex = i + stackOffset
                if (stackIndex >= trace.size) {
                    continue
                }
                builder.append("\n")
                        .append(level)
                        .append(_getSimpleClassName(trace[stackIndex].className))
                        .append(".")
                        .append(trace[stackIndex].methodName)
                        .append(" ")
                        .append("(")
                        .append(trace[stackIndex].fileName)
                        .append(":")
                        .append(trace[stackIndex].lineNumber)
                        .append(")")
                level += "    "
            }
            return builder.toString()
        } catch (e: Exception) {
            Log.w(debugTag, e)
            return ""
        }
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private fun _getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = MIN_STACK_OFFSET
        while (i < trace.size) {
            val e = trace[i]
            val name: String = e.className
            if (name != LogUtils::class.java.name) {
                return --i
            }
            i++
        }
        return -1
    }

    private fun _getSimpleClassName(name: String): String {
        val lastIndex = name.lastIndexOf(".")
        return name.substring(lastIndex + 1)
    }
}