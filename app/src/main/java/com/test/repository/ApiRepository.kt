package com.test.repository

import com.test.constants.Constants.LIMIT
import com.test.models.Product
import io.reactivex.Flowable

interface ApiRepository {
    fun getProducts(offset: Int): Flowable<MutableList<Product>>
    fun getProduct(id: Int): Flowable<Product>
}

class ApiRepositoryImpl(private val apiAdapter: ApiAdapter) : ApiRepository {
    override fun getProducts(offset: Int): Flowable<MutableList<Product>> {
        return apiAdapter.getProducts(offset, LIMIT)
    }

    override fun getProduct(id: Int): Flowable<Product> {
        return apiAdapter.getProduct(id)
    }
}