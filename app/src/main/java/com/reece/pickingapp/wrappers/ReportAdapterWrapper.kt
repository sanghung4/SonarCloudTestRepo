package com.reece.pickingapp.wrappers

import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.view.adapter.ReportProductsAdapter
import com.reece.pickingapp.viewmodel.ProductModel

class ReportAdapterWrapper {
    fun createAdapter(activityS: ActivityService): ReportProductsAdapter {
        return ReportProductsAdapter(activityS)
    }
}