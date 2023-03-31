package com.code7979.readerszone.di

import com.code7979.readerszone.data.remote.domain.BookApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private const val BASE_URL = "https://www.dbooks.org/api/"

    @Singleton
    @Provides
    fun getApiService(retrofit: Retrofit): BookApi = retrofit.create(BookApi::class.java)

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

}