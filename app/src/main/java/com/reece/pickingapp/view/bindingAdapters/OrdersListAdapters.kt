package com.reece.pickingapp.view.bindingAdapters

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputEditText
import com.reece.pickingapp.view.state.ResponseState
import com.reece.pickingapp.viewmodel.OrdersListFragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


object OrdersListAdapters {
    @OptIn(ExperimentalCoroutinesApi::class)
    @BindingAdapter("searchQueryListeners")
    @JvmStatic
    fun searchQueryListeners(search: TextInputEditText, viewModel: OrdersListFragmentViewModel?) {
        search.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
            }

            override fun beforeTextChanged(
                arg0: CharSequence, arg1: Int, arg2: Int,
                arg3: Int
            ) {
            }

            override fun afterTextChanged(arg0: Editable) {
                viewModel?.filterResults(arg0.toString())
            }
        })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @BindingAdapter("performFilter", "ordersResponseState")
    @JvmStatic
    fun performFilter(
        search: TextInputEditText,
        viewModel: OrdersListFragmentViewModel?,
        state: ResponseState<*>?
    ) {
        when (state) {
            is ResponseState.Success -> viewModel?.filterResults(search.text.toString())
            else -> {}
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @BindingAdapter("refreshListener")
    @JvmStatic
    fun refreshListener(
        refreshLayout: SwipeRefreshLayout,
        viewModel: OrdersListFragmentViewModel?
    ) {
        refreshLayout.setOnRefreshListener {
            viewModel?.queryOrdersList()
            refreshLayout.isRefreshing = false
        }
    }
}