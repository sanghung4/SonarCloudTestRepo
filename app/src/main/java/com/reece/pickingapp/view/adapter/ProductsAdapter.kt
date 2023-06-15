package com.reece.pickingapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.PickItemCardBinding
import com.reece.pickingapp.viewmodel.ProductViewHolderViewModel
import com.reece.pickingapp.viewmodel.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsAdapter(private val lifecycleOwner: LifecycleOwner) :
    ListAdapter<ProductViewHolderViewModel, ProductViewHolder>(ProductDiffUtil()) {

    lateinit var productRecyclerView: RecyclerView
    private var TAG = "ProductsAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding: PickItemCardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.pick_item_card,
            parent,
            false
        )

        binding.lifecycleOwner = lifecycleOwner
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val viewModel = getItem(position)
        viewModel.adapterPosition = position
        holder.binding.viewModel = viewModel
        holder.binding.product = viewModel.product
        viewModel.setUp()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        productRecyclerView = recyclerView
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun recyclerViewScrollToTop() {
        productRecyclerView.post { productRecyclerView.scrollToPosition(0) }
    }

    fun scrollTo(adapterPosition: Int, offset: Int) {
        productRecyclerView.adapter?.let { adapter ->
            val layoutManager = productRecyclerView.layoutManager
            if (layoutManager is LinearLayoutManager) {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        layoutManager.scrollToPositionWithOffset(
                            adapterPosition,
                            0 - offset
                        )
                    }
                }
            }
        }
    }

    fun removeItem(obj: ProductModel) {
        var list = ArrayList<ProductViewHolderViewModel>()
        list.addAll(currentList)
        val item = list.find { it.product.productId == obj.productId }
        if (item != null) {
            list.remove(item)
            submitList(list)
        }
    }

    fun moveItemToLast(position: Int) {
        var list = ArrayList<ProductViewHolderViewModel>()
        list.addAll(currentList)
        if(list.isNotEmpty()) {
            list.add(list[position])
            list.remove(list[position])
            submitList(list)
        }
    }
}

//region ViewHolder

class ProductViewHolder(val binding: PickItemCardBinding) : RecyclerView.ViewHolder(binding.root)

//endregion

//region DiffUtil

class ProductDiffUtil : DiffUtil.ItemCallback<ProductViewHolderViewModel>() {
    override fun areItemsTheSame(
        oldItem: ProductViewHolderViewModel,
        newItem: ProductViewHolderViewModel
    ): Boolean {
        return oldItem.product.productId == newItem.product.productId
    }

    override fun areContentsTheSame(
        oldItem: ProductViewHolderViewModel,
        newItem: ProductViewHolderViewModel
    ): Boolean {
        return oldItem.product.equals(newItem.product)
    }
}

//endregion
