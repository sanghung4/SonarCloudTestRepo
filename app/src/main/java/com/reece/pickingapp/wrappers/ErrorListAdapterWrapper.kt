package com.reece.pickingapp.wrappers

import androidx.lifecycle.LifecycleOwner
import com.reece.pickingapp.view.adapter.ErrorListAdapter
import com.reece.pickingapp.view.adapter.ProductsAdapter

class ErrorListAdapterWrapper {
    fun createAdapter(viewLifecycleOwner: LifecycleOwner): ErrorListAdapter {
        return ErrorListAdapter(viewLifecycleOwner)
    }
}