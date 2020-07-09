package com.test.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import com.google.gson.Gson
import com.test.repository.ApiAdapter
import com.test.repository.ApiRepository
import com.test.repository.ApiRepositoryImpl
import com.test.services.ProductService
import com.test.services.ProductServiceImpl
import com.squareup.picasso.Picasso
import com.test.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class ServicesModule(private val context: Application) {

    @Singleton
    @Provides
    fun preference(): SharedPreferences = context.getSharedPreferences("Shared", MODE_PRIVATE)

    @Provides
    @Singleton
    fun gson(): Gson = Gson()

    @Provides
    @Singleton
    fun netModule(): AutorizationInterceptor = AutorizationInterceptor()

    @Singleton
    @Provides
    fun userService(apiRepository: ApiRepository): ProductService {
        return ProductServiceImpl(apiRepository)
    }

    @Provides
    @Singleton
    fun bundle(): Bundle = Bundle()

    @Singleton
    @Provides
    fun picasso(): Picasso = Picasso.Builder(context).build()

    @Provides
    @Singleton
    fun loggingModule(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )

    @Provides
    @Singleton
    fun resources(): Resources = context.resources

    @Provides
    @Singleton
    fun okHttpBuilder(
        netModule: AutorizationInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ):
            OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor(netModule)
        .build()

    @Singleton
    @Provides
    fun apiRepository(okHttp: OkHttpClient): ApiRepository {
        val apiAdapter = Retrofit.Builder()
            .baseUrl("http://mobile-test.devebs.net:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttp)
            .build()
            .create<ApiAdapter>(ApiAdapter::class.java)

        return ApiRepositoryImpl(apiAdapter)
    }
}