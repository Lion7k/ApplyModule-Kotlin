package com.liuzq.basemodule_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.liuzq.commlibrary.utils.LogUtils
import com.liuzq.commlibrary.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
