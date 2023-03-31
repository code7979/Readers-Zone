package com.code7979.readerszone.data.remote.domain

import com.code7979.readerszone.data.remote.domain.model.RemoteBookDetail
import com.code7979.readerszone.data.remote.domain.model.BookResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

interface BookApi {
    @GET("recent")
    fun getRecentBook(): Call<BookResponse>

    @GET("book/{id}")
    fun getBookById(@Path("id") id: String): Call<RemoteBookDetail>

    @GET("search/{query}")
    fun searchBook(@Path("query") query: String): Call<BookResponse>

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl: String): Response<ResponseBody>
}