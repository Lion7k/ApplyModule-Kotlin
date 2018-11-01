package com.liuzq.httplibrary.cookie

import com.liuzq.httplibrary.cookie.store.CookieStore
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * 描    述：CookieJar的实现类，默认管理了用户自己维护的cookie
 */
class CookieJarImpl(cookieStore: CookieStore?) : CookieJar {

    private var cookieStore: CookieStore? = null

    init {
        if (cookieStore == null) {
            throw IllegalArgumentException("cookieStore can not be null!")
        }
        this.cookieStore = cookieStore
    }

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        cookieStore?.saveCookie(url, cookies)
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        return cookieStore?.loadCookie(url) as MutableList<Cookie>
    }

    fun getCookieStore(): CookieStore? {
        return cookieStore
    }
}