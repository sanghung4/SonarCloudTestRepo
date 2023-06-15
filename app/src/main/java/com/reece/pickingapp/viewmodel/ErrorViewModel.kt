package com.reece.pickingapp.viewmodel

import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.models.ProductDTO
import com.reece.pickingapp.utils.emptyString

class ErrorViewModel(
    val product: ErrorLogDTO
) {
    val name=product.errorName
}