package com.liuzq.commlibrary.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.lang.Exception
import java.util.*

object AppUtils {

    /**
     * 获取手机版本号
     *
     * @param context
     * @return 返回版本号
     */
    fun getAppVersion(context: Context): String {
        val pi: PackageInfo
        var versionNum: String
        try {
            val pm: PackageManager = context.packageManager
            pi = pm.getPackageInfo(context.packageName, PackageManager.GET_CONFIGURATIONS)
            versionNum = pi.versionName
        } catch (e: Exception) {
            versionNum = "0"
        }
        return versionNum
    }

    /**
     * 获取手机唯一标识码UUID
     *
     * @param context
     * @return 返回UUID
     * <p>
     * 记得添加相应权限
     * android.permission.READ_PHONE_STATE
     */
    fun getUUID(context: Context): String {
        var uuid = PreferencesUtils.getString("PHONE_UUID", "")
        if (TextUtils.isEmpty(uuid)) {
            try {
                val telephonyManager: TelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                @SuppressLint("MissingPermission", "HardwareIds") val tmDevice = telephonyManager.deviceId
                @SuppressLint("MissingPermission", "HardwareIds") val tmSerial = telephonyManager.simSerialNumber
                @SuppressLint("HardwareIds") val androidId = android.provider.Settings.Secure.getString(context.contentResolver, android.provider.Settings.Secure.ANDROID_ID)
                val deviceUuid = UUID(androidId.hashCode().toLong(), (tmDevice.hashCode().toLong() shl 32) or tmSerial.hashCode().toLong())
                val uniqueId = deviceUuid.toString()
                uuid = uniqueId
                PreferencesUtils.putString("PHONE_UUID", uuid)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return uuid
    }
}