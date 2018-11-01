package com.liuzq.httplibrary.cookie.store

import okhttp3.Cookie
import okhttp3.HttpUrl

/**
 *  描    述：CookieStore 的公共接口
 */
interface CookieStore {
    /** 保存url对应所有cookie  */
    fun saveCookie(url: HttpUrl, cookie: List<Cookie>)

    /** 保存url对应所有cookie  */
    fun saveCookie(url: HttpUrl, cookie: Cookie)

    /** 加载url所有的cookie  */
    fun loadCookie(url: HttpUrl): List<Cookie>

    /** 获取当前所有保存的cookie  */
    fun getAllCookie(): List<Cookie>

    /** 获取当前url对应的所有的cookie  */
    fun getCookie(url: HttpUrl): List<Cookie>

    /** 根据url和cookie移除对应的cookie  */
    fun removeCookie(url: HttpUrl, cookie: Cookie): Boolean

    /** 根据url移除所有的cookie  */
    fun removeCookie(url: HttpUrl): Boolean

    /** 移除所有的cookie  */
    fun removeAllCookie(): Boolean
}