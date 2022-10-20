package com.setjy.practiceapp.data.network

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkService {

    private const val BASE_URL = "https://setjy.zulipchat.com/api/v1/"

    private const val API_KEY = "GDnSt0MrYpAIiOwt4ILxdznzNcVyTeSC"

    private const val API_KEY_TEST = "71BnlNL4vMzhZYc4Ur6bKXYJklRMY122"

    private const val username = "task.tf2@gmail.com"

    private const val usernameTest = "setjy.work@gmail.com"

    private val header = Credentials.basic(username, API_KEY)
//test profile below
//    private val header = Credentials.basic(usernameTest, API_KEY_TEST)

    private val interceptor: Interceptor = Interceptor { chain ->
        val request: Request = chain.request()
        val authenticatedRequest: Request = request.newBuilder()
            .header("Authorization", header)
            .build()
        chain.proceed(authenticatedRequest)
    }

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .addNetworkInterceptor(httpLoggingInterceptor)
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    val zulipService: ZulipApi = retrofit.create(ZulipApi::class.java)
}