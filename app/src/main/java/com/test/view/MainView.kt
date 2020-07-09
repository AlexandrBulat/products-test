package com.test.view

import android.content.Context
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.R
import com.test.constants.Constants.TYPE_PRODUCTS
import com.test.databinding.MainViewBinding
import com.test.models.Product
import com.test.utils.toFlowable
import com.test.viewModels.MainViewModelImpl
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class MainView : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MainViewModelImpl
    @Inject
    lateinit var picasso: Picasso

    private lateinit var disposables: CompositeDisposable
    lateinit var viewBinding: MainViewBinding
    lateinit var linearLayoutManager: LinearLayoutManager
    private var productAdapter: ProductAdapter? = null


    override fun onAttach(activity: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(activity)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MainViewModelImpl::class.java)
    }

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?
    ): View? {

        viewBinding = DataBindingUtil.inflate(
            inflater, R.layout.main_view, container, false
        )

        disposables = CompositeDisposable()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager =  LinearLayoutManager(context)
        disposables += viewModel.products.toFlowable().subscribe { list ->
            viewModel.isLoading.value = false
            productAdapter?.replaceRange(list, TYPE_PRODUCTS)
        }
        viewBinding.products.layoutManager = linearLayoutManager
        productAdapter = context?.let { ProductAdapter(it, picasso) }
        viewBinding.products.addItemDecoration(
            SpacesItemDecoration(
                resources.getDimension(
                    R.dimen.block_item_space
                ).toInt()
            )
        )
        viewBinding.products.adapter = productAdapter

        viewBinding.products.addOnScrollListener(object :PaginationScrollListener(linearLayoutManager){
            override fun isLastPage(): Boolean {
               return  viewModel.isLastPage.value!!
            }

            override fun isLoading(): Boolean {
                return viewModel.isLoading.value!!
            }

            override fun loadMoreItems(offset:Int) {
                viewModel.getProducts(offset)
            }

        })

        productAdapter?.onTap = object : ProductAdapter.ProductAdapterCallback {
            override fun onItemClick(product: Product) {
                handleDetails(product)
            }

        }
    }

    private fun handleDetails(product: Product) {
        val saveView: DetailView = DetailView.newInstance(product.id!!)
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.let {
            saveView.show(it, DetailView.TAG)
        }
    }

    class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State
        ) {
            val itemPosition = parent.getChildAdapterPosition(view);
            val itemCount = state.itemCount

            if (itemPosition == 0) {
                outRect.top = space
            } else {
                outRect.top = space / 2
            }
            if (itemCount > 0 && itemPosition == itemCount - 1) {
                outRect.bottom = space
            } else {
                outRect.bottom = space / 2
            }

            outRect.left = space
            outRect.right = space
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    companion object {
        const val TAG = "MainView"
        fun newInstance(): MainView {
            return MainView()
        }
    }
}