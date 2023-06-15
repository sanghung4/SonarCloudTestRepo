package com.reece.pickingapp.viewmodel

import com.reece.pickingapp.models.OrderDTO

class OrderViewModel(
    val order: OrderDTO
) {
    val orderId: String? = order.orderId
    val shipToName: String? = order.shipToName
    val branchId: String? = order.branchId
    var assignedUserId: String? = order.assignedUserId
    val invoiceId: String? = order.invoiceId
    val shipVia: String? = order.shipVia

    fun getOrderDTO(): OrderDTO {
        return order
    }

    fun getToteName(): String {
        return "TOTE${orderId}"
    }

    fun getOrderIdValue(): String{
        return if (!orderId.isNullOrEmpty())
         "$orderId.001"
        else ""
    }

    fun getOrderText(): String?{
        return if(!orderId.isNullOrEmpty() && !invoiceId.isNullOrEmpty())
          "$orderId.$invoiceId"
        else null
    }
}