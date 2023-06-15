package com.reece.pickingapp.utils.extensions

import com.apollographql.apollo.api.toInput
import com.reece.pickingapp.PickingOrdersQuery
import com.reece.pickingapp.models.OrderDTO
import com.reece.pickingapp.type.PickingTaskInput
import com.reece.pickingapp.utils.OrderState

fun PickingOrdersQuery.PickingOrder.toPickingTaskInput(username: String, orderState: String = OrderState.ASSIGNED) = PickingTaskInput(
    orderId = orderId.toInput(),
    generationId = generationId.toInput(),
    invoiceId = invoiceId.toInput(),
    branchId = branchId.toInput(),
    pickGroup = pickGroup.toInput(),
    assignedUserId = username.toInput(),
    billTo = billTo.toInput(),
    shipTo = shipTo.toInput(),
    shipToName = shipToName.toInput(),
    pickCount = pickCount.toInput(),
    shipVia = shipVia.toInput(),
    isFromMultipleZones = isFromMultipleZones.toInput(),
    taskWeight = taskWeight.toInput(),
    taskState = orderState.toInput()
)

fun PickingOrdersQuery.PickingOrder.toOrderDTO() = OrderDTO(
    orderId = orderId.toString(),
    generationId = generationId as Int,
    invoiceId = invoiceId.toString(),
    branchId = branchId.toString(),
    pickGroup = pickGroup.toString(),
    assignedUserId = assignedUserId.toString(),
    billTo = billTo as Int,
    shipTo = shipTo as Int,
    shipToName = shipToName.toString(),
    pickCount = pickCount.toString(),
    shipVia = shipVia.toString(),
    isFromMultipleZones = isFromMultipleZones as Boolean,
    taskWeight = taskWeight as Double,
    taskState = taskState.toString()
)