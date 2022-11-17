package com.setjy.practiceapp.di.module

import com.setjy.practiceapp.data.remote.api.UsersApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .addNetworkInterceptor(httpLoggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideInterceptor(@Named(NAMED_HEADER) header: String): Interceptor =
        Interceptor { chain ->
            val request: Request = chain.request()
            val authenticatedRequest: Request = request.newBuilder()
                .header(HEADER_NAME, header)
                .build()
            chain.proceed(authenticatedRequest)
        }

    @Provides
    @Singleton
    @Named(NAMED_HEADER)
    fun provideHeader() = Credentials.basic(username, API_KEY)

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Reusable
    fun provideUsersApi(retrofit: Retrofit): UsersApi = retrofit.create(UsersApi::class.java)

    @Provides
    @Reusable
    fun provideScheduler(): Scheduler = Schedulers.io()

    companion object {
        private const val BASE_URL = "https://setjy.zulipchat.com/api/v1/"
        private const val API_KEY = "GDnSt0MrYpAIiOwt4ILxdznzNcVyTeSC"
        private const val username = "task.tf2@gmail.com"
        private const val CONNECT_TIMEOUT = 10L
        private const val READ_TIMEOUT = 10L
        private const val WRITE_TIMEOUT = 10L
        private const val HEADER_NAME = "Authorization"
        private const val NAMED_HEADER = "header"
    }
}