package com.test.repository

import com.test.models.Product
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiAdapter {
    @GET("/products")
    fun getProducts(@Query("offset") offset: Int,
                    @Query("limit") limit: Int): Flowable<MutableList<Product>>

    @GET("/product")
    fun getProduct(@Query("id") id: Int): Flowable<Product>
}