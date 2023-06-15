package com.reece.pickingapp.utils.extensions

import com.reece.pickingapp.AssignPickTaskMutation
import com.reece.pickingapp.models.OrderDTO

fun AssignPickTaskMutation.AssignPickTask.toOrderDTO() = OrderDTO(
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