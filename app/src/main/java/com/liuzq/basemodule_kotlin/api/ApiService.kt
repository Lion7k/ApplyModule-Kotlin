package com.liuzq.basemodule_kotlin.api

import com.liuzq.basemodule_kotlin.bean.BookBean
import com.liuzq.basemodule_kotlin.bean.Top250Bean
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {
    @GET("v2/book/1220562")
    fun getBook(): Observable<BookBean>

    @GET("v2/movie/top250")
    fun getTop250(@Query("count") count: Int): Observable<Top250Bean>

    @GET("v2/book/1220562")
    fun getBookString(): Observable<String>

    //以下是post请求的两种方式demo示例

//    /**
//     * post提交json数据 demo
//     * @param map 键值对
//     * @return
//     */
//    @POST("xxx")
//    Observable<BaseData<T>> getData(@Body Map map);
//
//    /**
//     * post提交表单 demo
//     * @param name
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("xxx")
//    Observable<BaseData<T>> getData(@Field("name") String name);
//

    /**
     * 大文件官方建议用 @Streaming 来进行注解，不然会出现IO异常，小文件可以忽略不注入
     *
     * @param fileUrl 地址
     * @return ResponseBody
     */
    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<ResponseBody>

    /**
     * 上传多个文件
     *
     * @param uploadUrl 地址
     * @param files     文件
     * @return ResponseBody
     */
    @Multipart
    @POST
    fun uploadFiles(@Url uploadUrl: String,
                    @Part files: List<MultipartBody.Part>): Observable<ResponseBody>
}