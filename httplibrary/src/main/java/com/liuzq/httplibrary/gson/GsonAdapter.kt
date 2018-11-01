package com.liuzq.httplibrary.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonAdapter {

    fun buildGson(): Gson {
        val gson = GsonBuilder()
                .registerTypeAdapter(Int::class.java, IntegerDefault0Adapter())
                .registerTypeAdapter(Double::class.java, DoubleDefault0Adapter())
                .registerTypeAdapter(Long::class.java, LongDefault0Adapter())
                .create()
        return gson
    }
}