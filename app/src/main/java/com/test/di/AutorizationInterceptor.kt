package com.test.di

import okhttp3.Interceptor
import okhttp3.Response
import com.test.services.ProductService

class AutorizationInterceptor : Interceptor {
     lateinit var productService: ProductService
     override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
         var newRequest = request.newBuilder()

        return chain.proceed(newRequest.build())
    }
}

