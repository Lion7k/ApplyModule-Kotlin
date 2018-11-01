package com.liuzq.httplibrary.download

import com.liuzq.httplibrary.RxHttpUtils
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * desc 保存下载的文件
 */
class DownloadManager {

    /**
     * 保存文件
     *
     * @param response     ResponseBody
     * @param destFileName 文件名（包括文件后缀）
     * @return 返回
     * @throws IOException
     */
    @Throws(IOException::class)
    fun saveFile(response: ResponseBody, destFileName: String, progressListener: ProgressListener): File {
        val destFileDir = RxHttpUtils.getContext()?.getExternalFilesDir(null)?.path + File.separator
        val contentLength = response.contentLength()
        var ins: InputStream? = null
        val buf = ByteArray(2048)
        var len = 0
        var fos: FileOutputStream? = null

        try {
            ins = response.byteStream()
            var sum: Long = 0
            val dir = File(destFileDir)
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val file = File(dir, destFileName)
            fos = FileOutputStream(file)
            while (len != -1) {
                len = ins?.read(buf)!!
                sum += len.toLong()
                fos.write(buf, 0, len)
                val finalSum = sum
                progressListener.onResponseProgress(finalSum, contentLength, ((finalSum * 1.0F / contentLength) * 100).toInt(), finalSum == contentLength, file.absolutePath)
            }
            fos.flush()
            return file
        } finally {
            try {
                response.close()
                ins?.close()
            } catch (e: IOException) {
            }

            try {
                fos?.close()
            } catch (e: IOException) {
            }
        }
    }
}