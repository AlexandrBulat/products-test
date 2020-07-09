package com.test.repository

import com.test.constants.Constants.LIMIT
import com.test.models.Product
import io.reactivex.Flowable

interface ApiRepository {
    fun getProducts(offset: Int): Flowable<MutableList<Product>>
}

class ApiRepositoryImpl(private val apiAdapter: ApiAdapter) : ApiRepository {
    override fun getProducts(offset: Int): Flowable<MutableList<Product>> {
        return apiAdapter.getProducts(offset, LIMIT)
    }
}