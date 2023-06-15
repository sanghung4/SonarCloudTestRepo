package com.reece.pickingapp.wrappers

import androidx.lifecycle.LifecycleOwner
import com.reece.pickingapp.view.adapter.ProductsAdapter

class ProductsAdapterWrapper {
    fun createAdapter(viewLifecycleOwner: LifecycleOwner): ProductsAdapter {
        return ProductsAdapter(viewLifecycleOwner)
    }
}