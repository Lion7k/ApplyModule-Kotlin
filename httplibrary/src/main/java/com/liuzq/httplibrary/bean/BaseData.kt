package com.liuzq.httplibrary.bean

/**
 * 返回数据基类
 */
open class BaseData<T> {
    /**
     * 错误码
     */
    var code: Int? = null
    /**
     * 错误描述
     */
    var msg: String? = null
    /**
     * 数据
     */
    var data: T? = null
}