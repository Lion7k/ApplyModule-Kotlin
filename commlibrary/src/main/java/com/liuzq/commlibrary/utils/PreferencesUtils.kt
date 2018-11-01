package com.liuzq.commlibrary.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Build

@SuppressLint("NewApi")
object PreferencesUtils {
    /** SharedPreferences variant  */
    private var mPrefs: SharedPreferences? = null
    /** constant #LENGTH  */
    private val LENGTH_SUFFIX = "#LENGTH"
    /** constant [  */
    private val LEFT_MOUNT = "["
    /** constant ]  */
    private val RIGHT_MOUNT = "]"

    /**
     * Initialize the Prefs helper class to keep a reference to the SharedPreference for this application the
     * SharedPreference will use the package name of the application as the Key.
     *
     * @param context the Application context.
     */
    fun initPrefs(context: Context) {

        if (mPrefs == null) {
            val key = context.packageName ?: throw NullPointerException("Prefs key may not be null")
            mPrefs = context.getSharedPreferences(key, Context.MODE_MULTI_PROCESS)
        }
    }

    /**
     * 重新创建Preference对象，在跨进程掉用的时候需要重新初始化
     * 在小米上有问题，废弃
     *
     * @param context the Application context.
     */
    @Deprecated("")
    fun reInit(context: Context?) {
        val key: String = context?.packageName
                ?: throw NullPointerException("Prefs key may not be null")
        context.getSharedPreferences(key, Context.MODE_MULTI_PROCESS)
    }

    /**
     * Returns an instance of the shared preference for this app.
     *
     * @return an Instance of the SharedPreference
     */
    fun getPreferences(): SharedPreferences {
        return mPrefs
                ?: throw RuntimeException("please call iniPrefs(context) in the Application class onCreate.")
    }

    /**
     * @return Returns a map containing a list of pairs key/value representing the preferences.
     * @see SharedPreferences#getAll()
     */
    fun getAll(): Map<String, *> {
        return getPreferences().all
    }

    /**
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a
     *         preference with this name that is not an int.
     * @see SharedPreferences#getInt(String, int)
     */
    fun getInt(key: String, defValue: Int): Int {
        return getPreferences().getInt(key, defValue)
    }

    /**
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a
     *         preference with this name that is not a boolean.
     * @see SharedPreferences#getBoolean(String, boolean)
     */
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return getPreferences().getBoolean(key, defValue)
    }

    /**
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a
     *         preference with this name that is not a long.
     * @see SharedPreferences#getLong(String, long)
     */
    fun getLong(key: String, defValue: Long): Long {
        return getPreferences().getLong(key, defValue)
    }

    /**
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a
     *         preference with this name that is not a float.
     * @see SharedPreferences#getFloat(String, float)
     */
    fun getFloat(key: String, defValue: Float): Float {
        return getPreferences().getFloat(key, defValue)
    }

    /**
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a
     *         preference with this name that is not a String.
     * @see SharedPreferences#getString(String, String)
     */
    fun getString(key: String, defValue: String): String {
        return getPreferences().getString(key, defValue)
    }

    /**
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defValues. Throws ClassCastException if there is a
     *         preference with this name that is not a Set.
     * @see SharedPreferences#getStringSet(String, Set)
     */
    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun getStringSet(key: String, defValue: Set<String>): Set<String> {
        val prefs = getPreferences()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return prefs.getStringSet(key, defValue)
        } else {
            if (prefs.contains(key + LENGTH_SUFFIX)) {
                val set: HashSet<String> = HashSet()
                // Workaround for pre-HC's lack of StringSets
                val stringSetLength = prefs.getInt(key + LENGTH_SUFFIX, -1)
                if (stringSetLength >= 0) {
                    for (i in 0 until stringSetLength) {
                        prefs.getString(key + LEFT_MOUNT + i + RIGHT_MOUNT, null)
                    }
                }
                return set
            }
        }
        return defValue
    }

    /**
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putLong(String, long)
     */
    @SuppressLint("ObsoleteSdkInt", "ApplySharedPref")
    fun putLong(key: String, value: Long) {
        val editor = getPreferences().edit()
        editor.putLong(key, value)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    /**
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putInt(String, int)
     */
    @SuppressLint("ObsoleteSdkInt", "ApplySharedPref")
    fun putLong(key: String, value: Int) {
        val editor = getPreferences().edit()
        editor.putInt(key, value)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    /**
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     *
     * @see Editor#putFloat(String, float)
     */
    @SuppressLint("ObsoleteSdkInt", "ApplySharedPref")
    fun putFloat(key: String, value: Float) {
        val editor = getPreferences().edit()
        editor.putFloat(key, value)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    /**
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     *
     * @see Editor#putBoolean(String, boolean)
     */
    @SuppressLint("ObsoleteSdkInt", "ApplySharedPref")
    fun putBoolean(key: String, value: Boolean) {
        val editor = getPreferences().edit()
        editor.putBoolean(key, value)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    /**
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     *
     * @see Editor#putString(String, String)
     */
    @SuppressLint("ObsoleteSdkInt", "ApplySharedPref")
    fun putString(key: String, value: String) {
        val editor = getPreferences().edit()
        editor.putString(key, value)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    /**
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putStringSet(String, Set)
     */
    @SuppressLint("ObsoleteSdkInt", "ApplySharedPref")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun putStringSet(key: String, value: Set<String>) {
        val editor = getPreferences().edit()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            editor.putStringSet(key, value)
        } else {
            // Workaround for pre-HC's lack of StringSets
            var stringSetLength = 0
            if (mPrefs!!.contains(key + LENGTH_SUFFIX)) {
                // First read what the value was
                stringSetLength = mPrefs!!.getInt(key + LENGTH_SUFFIX, -1)
            }
            editor.putInt(key + LENGTH_SUFFIX, value.size)
            var i = 0
            for (aValue in value) {
                editor.putString(key + LEFT_MOUNT + i + RIGHT_MOUNT, aValue)
                i++
            }
            while (i < stringSetLength) {
                // Remove any remaining values
                editor.remove(key + LEFT_MOUNT + i + RIGHT_MOUNT)
                i++
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    /**
     * @param key The name of the preference to remove.
     * @see Editor#remove(String)
     */
    @SuppressLint("ObsoleteSdkInt", "ApplySharedPref")
    fun remove(key: String) {
        val prefs = getPreferences()
        val editor = prefs.edit()
        if (prefs.contains(key + LENGTH_SUFFIX)) {
            // Workaround for pre-HC's lack of StringSets
            val stringSetLength = prefs.getInt(key + LENGTH_SUFFIX, -1)
            if (stringSetLength >= 0) {
                editor.remove(key + LENGTH_SUFFIX)
                for (i in 0 until stringSetLength) {
                    editor.remove(key + LEFT_MOUNT + i + RIGHT_MOUNT)
                }
            }
        }
        editor.remove(key)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    /**
     * @param key The name of the preference to check.
     * @see SharedPreferences#contains(String)
     * @return boolean true flase
     */
    fun contains(key: String): Boolean {
        return getPreferences().contains(key)
    }
}