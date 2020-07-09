package com.test.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.R
import com.test.constants.Constants.TYPE_LOADING
import com.test.constants.Constants.TYPE_PRODUCTS
import com.test.databinding.LoadingItemBinding
import com.test.databinding.ProductItemBinding
import com.test.models.Product
import java.io.File


class ProductAdapter(
    private val context: Context, private val picasso: Picasso
) : RecyclerView.Adapter<ProductAdapter.BaseViewHolder<*>>() {

    var onTap: ProductAdapterCallback? = null
    private var type: Int = TYPE_PRODUCTS
    private val list: MutableList<Comparable<*>> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        when (viewType) {
            TYPE_PRODUCTS -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DataBindingUtil.inflate<ProductItemBinding>(
                    layoutInflater
                    , R.layout.product_item,
                    parent, false
                )
                return ProductHolderItem(binding, this.onTap)
            }
            TYPE_LOADING -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DataBindingUtil.inflate<LoadingItemBinding>(
                    layoutInflater
                    , R.layout.loading_item,
                    parent, false
                )
                return ProgressHolderItem(binding, this.onTap)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (type) {
            TYPE_PRODUCTS -> TYPE_PRODUCTS
            TYPE_LOADING -> TYPE_LOADING
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = list[position]
        when (holder) {
            is ProgressHolderItem -> holder.bind(element as Product)
            is ProductHolderItem -> holder.bind(element as Product)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun replaceRange(products: List<Comparable<*>>, type: Int) {
        val size = this.list.size
        this.list.addAll(products)
        val sizeNew = this.list.size
        this.type = type
        notifyItemRangeChanged(size, sizeNew)
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class ProgressHolderItem(
        private val binding: LoadingItemBinding,
        private val onTap: ProductAdapterCallback?
    ) : BaseViewHolder<Product>(binding.root) {
        override fun bind(item: Product) {

            this.binding.executePendingBindings()
        }
    }


    inner class ProductHolderItem constructor(
        private val binding: ProductItemBinding,
        private val onTap: ProductAdapterCallback?
    ) : BaseViewHolder<Product>(binding.root) {
        override fun bind(item: Product) {

            item.image?.let {
                picasso.load(it)
                    .into(binding.image)
            }

            binding.title.text = item.title

            itemView.setOnClickListener {
                onTap?.onItemClick(item)
            }

            this.binding.executePendingBindings()
        }

    }

    interface ProductAdapterCallback {
        fun onItemClick(product: Product)
    }
}