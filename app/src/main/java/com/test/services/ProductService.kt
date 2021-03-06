package com.test.services

import com.test.models.Product
import com.test.repository.ApiRepository
import io.reactivex.Flowable

interface ProductService {
    fun getProducts(offset: Int): Flowable<MutableList<Product>>
    fun getProduct(id: Int): Flowable<Product>
}

class ProductServiceImpl(
    private val apiRepository: ApiRepository
) : ProductService {

    override fun getProducts(offset: Int): Flowable<MutableList<Product>> {
        return apiRepository.getProducts(offset)
    }

    override fun getProduct(id: Int): Flowable<Product> {
        return apiRepository.getProduct(id)
    }
}