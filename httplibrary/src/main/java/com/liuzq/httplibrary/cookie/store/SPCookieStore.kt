package com.liuzq.httplibrary.cookie.store

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.liuzq.httplibrary.cookie.SerializableCookie
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.util.ArrayList
import java.util.concurrent.ConcurrentHashMap

class SPCookieStore constructor(context: Context) : CookieStore {

    companion object {
        private const val COOKIE_PREFS = "rx_http_utils_cookie"           //cookie使用prefs保存
        private const val COOKIE_NAME_PREFIX = "cookie_"         //cookie持久化的统一前缀
    }

    /**
     * 数据结构如下
     * Url.host -> cookieToken1,cookieToken2,cookieToken3
     * cookie_cookieToken1 -> cookie1
     * cookie_cookieToken2 -> cookie2
     * cookie_cookieToken3 -> cookie3
     */
    private var cookies: MutableMap<String, ConcurrentHashMap<String, Cookie>>? = null
    private var cookiePrefs: SharedPreferences? = null

    init {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE)
        cookies = HashMap()

        //将持久化的cookies缓存到内存中,数据结构为 Map<Url.host, Map<CookieToken, Cookie>>
        val prefsMap: Map<String, *>? = cookiePrefs?.all
        if (prefsMap != null) {
            for ((key, value) in prefsMap) {
                if (value != null && key.startsWith(COOKIE_NAME_PREFIX)) {
                    //获取url对应的所有cookie的key,用","分割
                    val cookieNames = TextUtils.split(value as String, ",")
                    for (name in cookieNames) {
                        //根据对应cookie的Key,从xml中获取cookie的真实值
                        val encodedCookie = cookiePrefs?.getString(COOKIE_NAME_PREFIX + name, null)
                        if (encodedCookie != null) {
                            val decodedCookie: Cookie? = SerializableCookie.decodeCookie(encodedCookie)
                            if (decodedCookie != null) {
                                if (!cookies?.containsKey(key)!!) {
                                    cookies?.put(key, ConcurrentHashMap())
                                }
                                cookies?.get(key)?.put(name, decodedCookie)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getCookieToken(cookie: Cookie): String {
        return cookie.name() + "@" + cookie.domain()
    }

    /**
     * 当前cookie是否过期
     */
    private fun isCookieExpired(cookie: Cookie): Boolean {
        return cookie.expiresAt() < System.currentTimeMillis()
    }

    /**
     * 将url的所有Cookie保存在本地
     */
    @Synchronized
    override fun saveCookie(url: HttpUrl, cookie: List<Cookie>) {
        for (singleCookie in cookie) {
            saveCookie(url, singleCookie)
        }
    }

    @Synchronized
    override fun saveCookie(url: HttpUrl, cookie: Cookie) {
        if (!cookies?.containsKey(url.host())!!) {
            cookies?.put(url.host(), ConcurrentHashMap())
        }

        //当前cookie是否过期
        if (isCookieExpired(cookie)) {
            removeCookie(url, cookie)
        } else {
            saveCookie(url, cookie, getCookieToken(cookie))
        }
    }

    /**
     * 保存cookie，并将cookies持久化到本地
     */
    private fun saveCookie(url: HttpUrl, cookie: Cookie, cookieToken: String) {
        //内存缓存
        cookies?.get(url.host())?.put(cookieToken, cookie)
        //文件缓存
        val prefsWriter = cookiePrefs?.edit()
        prefsWriter?.putString(url.host(), TextUtils.join(",", cookies?.get(url.host())?.keys))
        prefsWriter?.putString(COOKIE_NAME_PREFIX + cookieToken, SerializableCookie.encodeCookie(url.host(), cookie))
        prefsWriter?.apply()
    }

    /**
     * 根据当前url获取所有需要的cookie,只返回没有过期的cookie
     */
    @Synchronized
    override fun loadCookie(url: HttpUrl): List<Cookie> {
        val ret = ArrayList<Cookie>()
        if (!cookies?.containsKey(url.host())!!) {
            return ret
        }
        val urlCookies: Collection<Cookie>? = cookies?.get(url.host())?.values
        if (urlCookies != null) {
            for (cookie in urlCookies) {
                if (isCookieExpired(cookie)) {
                    removeCookie(url, cookie)
                } else {
                    ret.add(cookie)
                }
            }
        }
        return ret
    }

    /**
     * 获取所有的cookie
     */
    @Synchronized
    override fun getAllCookie(): List<Cookie> {
        val ret = ArrayList<Cookie>()
        for (key in cookies?.keys!!) {
            ret.addAll(cookies?.get(key)?.values!!)
        }
        return ret
    }

    @Synchronized
    override fun getCookie(url: HttpUrl): List<Cookie> {
        val ret = ArrayList<Cookie>()
        val mapCookie = cookies?.get(url.host())
        if (mapCookie != null) ret.addAll(mapCookie.values)
        return ret
    }

    /**
     * 根据url移除当前的cookie
     */
    @Synchronized
    override fun removeCookie(url: HttpUrl, cookie: Cookie): Boolean {
        if (!cookies?.containsKey(url.host())!!) return false
        val cookieToken: String = getCookieToken(cookie)
        if (!cookies?.get(url.host())?.containsKey(cookieToken)!!) return false

        //内存移除
        cookies?.get(url.host())?.remove(cookieToken)
        //文件移除
        val prefsWriter = cookiePrefs?.edit()
        if (cookiePrefs?.contains(COOKIE_NAME_PREFIX + cookieToken)!!) {
            prefsWriter?.remove(COOKIE_NAME_PREFIX + cookieToken)
        }
        prefsWriter?.putString(url.host(), TextUtils.join(",", cookies?.get(url.host())?.keys))
        prefsWriter?.apply()
        return true
    }

    @Synchronized
    override fun removeCookie(url: HttpUrl): Boolean {
        if (!cookies?.containsKey(url.host())!!) return false

        //内存移除
        val urlCookie: ConcurrentHashMap<String, Cookie>? = cookies?.remove(url.host())
        //文件移除
        val cookieTokens = urlCookie?.keys
        val prefsWriter = cookiePrefs?.edit()
        for (cookieToken in cookieTokens!!) {
            if (cookiePrefs?.contains(COOKIE_NAME_PREFIX + cookieToken)!!) {
                prefsWriter?.remove(COOKIE_NAME_PREFIX + cookieToken)
            }
        }
        prefsWriter?.remove(url.host())
        prefsWriter?.apply()

        return true

    }

    override fun removeAllCookie(): Boolean {
        //内存移除
        cookies?.clear()
        //文件移除
        val prefsWriter = cookiePrefs?.edit()
        prefsWriter?.clear()
        prefsWriter?.apply()
        return true
    }
}