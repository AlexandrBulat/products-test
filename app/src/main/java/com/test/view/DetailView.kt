package com.test.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import com.test.R
import com.test.databinding.DetailViewBinding
import com.test.viewModels.DetailViewModelImpl
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class DetailView : BaseDialogFragment() {

    lateinit var viewBinding: DetailViewBinding
    private lateinit var disposables: CompositeDisposable

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DetailViewModelImpl

    @Inject
    lateinit var picasso: Picasso

    override fun onAttach(activity: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(activity)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DetailViewModelImpl::class.java)
    }

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?
    ): View? {

        viewBinding = DataBindingUtil.inflate(
            inflater, R.layout.detail_view, container, false
        )
        viewBinding.viewModel = viewModel
        viewBinding.lifecycleOwner = this
        disposables = CompositeDisposable()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (arguments?.getInt(KEY_ID))?.let { id ->
            viewModel.getProduct(id)
        }

        viewModel.onResult ={
            picasso.load(it.image)
                .into(viewBinding.image)
        }

        viewBinding.close.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }

    companion object {
        const val TAG = "DetailView"
        const val KEY_ID = "KEY_ID"

        fun newInstance(id: Int): DetailView {
            val detailView = DetailView()
            val args = Bundle()
            args.putInt(KEY_ID, id)
            detailView.arguments = args
            return detailView
        }
    }
}