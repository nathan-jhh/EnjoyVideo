package com.techme.jetpack.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ApiService {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://8.136.122.222/jetpack/")
        /*.baseUrl("http://192.168.31.201:8082/jetpack/")*/
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory())
        .build()

    fun getService(): IApiInterface {
        return retrofit.create(IApiInterface::class.java)
    }
}