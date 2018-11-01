package com.liuzq.httplibrary.cookie

import android.content.ContentValues
import android.database.Cursor
import okhttp3.Cookie
import java.io.*
import java.util.*
import kotlin.experimental.and

class SerializableCookie(var host: String, @Transient private var cookie: Cookie) : Serializable {

    var name: String? = null
    var domain: String? = null
    @Transient
    private var clientCookie: Cookie? = null

    companion object {
        private const val serialVersionUID = 6374381323722046732L
        const val HOST = "host"
        const val NAME = "name"
        const val DOMAIN = "domain"
        const val COOKIE = "cookie"

        /**
         * cookies 序列化成 string
         *
         * @param cookie 要序列化
         * @return 序列化之后的string
         */
        fun encodeCookie(host: String, cookie: Cookie?): String? {
            if (cookie == null) return null
            val cookieBytes = cookieToBytes(host, cookie)
            return byteArrayToHexString(cookieBytes!!)
        }

        fun cookieToBytes(host: String, cookie: Cookie): ByteArray? {
            val serializableCookie = SerializableCookie(host, cookie)
            val os = ByteArrayOutputStream()
            try {
                val outputStream = ObjectOutputStream(os)
                outputStream.writeObject(serializableCookie)
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

            return os.toByteArray()
        }

        /**
         * 将字符串反序列化成cookies
         *
         * @param cookieString cookies string
         * @return cookie object
         */
        fun decodeCookie(cookieString: String): Cookie? {
            val bytes = hexStringToByteArray(cookieString)
            return bytesToCookie(bytes)
        }

        fun bytesToCookie(bytes: ByteArray): Cookie? {
            val byteArrayInputStream = ByteArrayInputStream(bytes)
            var cookie: Cookie? = null
            try {
                val objectInputStream = ObjectInputStream(byteArrayInputStream)
                cookie = (objectInputStream.readObject() as SerializableCookie).getCookie()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return cookie
        }

        /**
         * 二进制数组转十六进制字符串
         *
         * @param bytes byte array to be converted
         * @return string containing hex values
         */
        private fun byteArrayToHexString(bytes: ByteArray): String {
            val sb = StringBuilder(bytes.size * 2)
            for (element in bytes) {
                val v: Int = (element and 0xff.toByte()).toInt()
                if (v < 16) {
                    sb.append('0')
                }
                sb.append(Integer.toHexString(v))
            }
            return sb.toString().toUpperCase(Locale.US)
        }

        /**
         * 十六进制字符串转二进制数组
         *
         * @param hexString string of hex-encoded values
         * @return decoded byte array
         */
        private fun hexStringToByteArray(hexString: String): ByteArray {
            val len = hexString.length
            val data = ByteArray(len / 2)
            var i = 0
            while (i < len) {
                data[i / 2] = ((Character.digit(hexString[i], 16) shl 4) + Character.digit(hexString[i + 1], 16)).toByte()
                i += 2
            }
            return data
        }
    }

    init {
        this.name = cookie.name()
        this.domain = cookie.domain()
    }

    fun getCookie(): Cookie? {
        var bestCookie: Cookie? = null
        if (clientCookie != null) {
            bestCookie = clientCookie
        }
        return bestCookie
    }

    private fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
        out.writeObject(cookie.name())
        out.writeObject(cookie.value())
        out.writeLong(cookie.expiresAt())
        out.writeObject(cookie.domain())
        out.writeObject(cookie.path())
        out.writeBoolean(cookie.secure())
        out.writeBoolean(cookie.httpOnly())
        out.writeBoolean(cookie.hostOnly())
        out.writeBoolean(cookie.persistent())
    }

    private fun readObject(oin: ObjectInputStream) {
        oin.defaultReadObject()
        val name = oin.readObject() as String
        val value = oin.readObject() as String
        val expiresAt = oin.readLong()
        val domain = oin.readObject() as String
        val path = oin.readObject() as String
        val secure = oin.readBoolean()
        val httpOnly = oin.readBoolean()
        val hostOnly = oin.readBoolean()
        val persistent = oin.readBoolean()
        var builder = Cookie.Builder()
        builder = builder.name(name)
        builder = builder.value(value)
        builder = builder.expiresAt(expiresAt)
        builder = if (hostOnly) builder.hostOnlyDomain(domain) else builder.domain(domain)
        builder = builder.path(path)
        builder = if (secure) builder.secure() else builder
        builder = if (httpOnly) builder.httpOnly() else builder
        clientCookie = builder.build()
    }

    fun parseCursorToBean(cursor: Cursor): SerializableCookie {
        val host = cursor.getString(cursor.getColumnIndex(HOST))
        val cookieBytes = cursor.getBlob(cursor.getColumnIndex(COOKIE))
        val cookie = bytesToCookie(cookieBytes)
        return SerializableCookie(host, cookie!!)
    }

    fun getContentValues(serializableCookie: SerializableCookie): ContentValues {
        val values = ContentValues()
        values.put(SerializableCookie.HOST, serializableCookie.host)
        values.put(SerializableCookie.NAME, serializableCookie.name)
        values.put(SerializableCookie.DOMAIN, serializableCookie.domain)
        values.put(SerializableCookie.COOKIE, cookieToBytes(serializableCookie.host, serializableCookie.getCookie()!!))
        return values
    }

    /**
     * host, name, domain 标识一个cookie是否唯一
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as SerializableCookie?

        if (host != that!!.host) return false
        if (if (name != null) name != that.name else that.name != null) return false
        return if (domain != null) domain == that.domain else that.domain == null
    }

    override fun hashCode(): Int {
        var result = host.hashCode()
        result = 31 * result + if (name != null) name!!.hashCode() else 0
        result = 31 * result + if (domain != null) domain!!.hashCode() else 0
        return result
    }
}