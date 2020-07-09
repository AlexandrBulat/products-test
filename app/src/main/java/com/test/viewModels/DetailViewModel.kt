package com.test.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.models.Product
import com.test.services.ProductService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import javax.inject.Inject

interface DetailViewModel {
    var product: MutableLiveData<Product>
    var onResult: ((product: Product) -> Unit)?
    fun getProduct(id:Int)
}

class DetailViewModelImpl @Inject constructor(val productService: ProductService) :
    ViewModel(), DetailViewModel {

    private var disposables: CompositeDisposable = CompositeDisposable()
    override var product = MutableLiveData<Product>()
    override var onResult: ((product: Product) -> Unit)? = null

    override fun getProduct(id: Int) {
        disposables += productService.getProduct(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                product.value = it
                onResult?.invoke(it)
            }, {
            })
    }


    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}