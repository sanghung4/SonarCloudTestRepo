package com.reece.pickingapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.ItemOrderBinding
import com.reece.pickingapp.viewmodel.OrderViewHolderViewModel


class OrdersAdapter : ListAdapter<OrderViewHolderViewModel, OrderViewHolder>(OrderDiffUtil()) {

    lateinit var ordersRecyclerView: RecyclerView
    private var hasShippingDetails: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding: ItemOrderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_order,
            parent,
            false
        )

        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val viewModel = getItem(position)
        holder.binding.viewModel = viewModel
        holder.binding.order = viewModel.orderViewModel
        if (!hasShippingDetails) {
            viewModel.setUp() {
                hasShippingDetails = true
                notifyItemChanged(position)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        ordersRecyclerView = recyclerView
    }

    fun recyclerViewScrollToTop() {
        ordersRecyclerView.scrollToPosition(0)
    }
}

class OrderViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

class OrderDiffUtil : DiffUtil.ItemCallback<OrderViewHolderViewModel>() {
    override fun areItemsTheSame(
        oldItem: OrderViewHolderViewModel,
        newItem: OrderViewHolderViewModel
    ): Boolean {
        return oldItem.orderViewModel.orderId == newItem.orderViewModel.orderId
    }

    override fun areContentsTheSame(
        oldItem: OrderViewHolderViewModel,
        newItem: OrderViewHolderViewModel
    ): Boolean {
        return oldItem.orderViewModel.orderId == newItem.orderViewModel.orderId
    }
}