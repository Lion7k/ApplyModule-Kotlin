package com.liuzq.commlibrary.utils

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.IdRes
import android.view.View
import android.widget.Toast

@SuppressLint("StaticFieldLeak")
object ToastUtils {
    var it: Toast? = null
    private var context: Context? = null

    fun init(_context: Context) {
        context = _context
        val v: View = Toast.makeText(_context, "", Toast.LENGTH_SHORT).view
        init(_context, v)
    }

    private fun init(_context: Context, view: View) {
        it = Toast(_context)
        it?.view = view
    }

    fun setBackgroundView(view: View) {
        it?.view = view
    }

    private fun show(text: CharSequence, duration: Int) {
        if (it == null) {
            LogUtils.e("ToastUtils", "ToastUtils is not initialized, please call init once before you call this method")
            return
        }
        it?.setText(text)
        it?.duration = duration
        it?.show()
    }

    private fun show(resid: Int, duration: Int) {
        if (it == null) {
            LogUtils.e("ToastUtils", "ToastUtils is not initialized, please call init once before you call this method")
            return
        }
        it?.setText(resid)
        it?.duration = duration
        it?.show()
    }

    fun show(text: CharSequence) {
        show(text, Toast.LENGTH_SHORT)
    }

    fun show(resid: Int) {
        show(resid, Toast.LENGTH_SHORT)
    }
}