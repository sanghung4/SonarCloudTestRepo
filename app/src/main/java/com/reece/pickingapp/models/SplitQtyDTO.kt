package com.reece.pickingapp.models

import com.reece.pickingapp.viewmodel.ProductModel

data class SplitQtyDTO(
    var productsList: MutableList<ProductModel?> = mutableListOf()
)
