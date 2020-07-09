package com.test.viewModels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.models.Product
import com.test.services.ProductService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface MainViewModel {
    var products: ObservableField<MutableList<Product>>
    var isLastPage: MutableLiveData<Boolean>
    var isLoading: MutableLiveData<Boolean>
    fun getProducts(offset: Int)
}

class MainViewModelImpl @Inject constructor(val productService: ProductService) :
    ViewModel(), MainViewModel {

    private var disposables: CompositeDisposable = CompositeDisposable()
    override var products = ObservableField<MutableList<Product>>()
    override var isLastPage = MutableLiveData<Boolean>()
    override var isLoading = MutableLiveData<Boolean>()


    override fun getProducts(offset: Int) {
        isLoading.value = true
        disposables += productService.getProducts(offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.value = false
                products.set(it)
            }, {
                isLoading.value = false
            })
    }


    init {
        isLoading.value = false
        isLastPage.value = false
        getProducts(0)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}