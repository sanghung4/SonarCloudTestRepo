package com.reece.pickingapp.wrappers

import androidx.lifecycle.LifecycleOwner
import com.reece.pickingapp.view.adapter.PickFromOtherLocationsAdapter
import com.reece.pickingapp.view.adapter.ProductsAdapter

class PickFromOtherLocationsAdapterWrapper {
    fun createAdapter(viewLifecycleOwner: LifecycleOwner, onChangeQtyLabel: () -> Unit): PickFromOtherLocationsAdapter {
        return PickFromOtherLocationsAdapter(viewLifecycleOwner, onChangeQtyLabel)
    }
}