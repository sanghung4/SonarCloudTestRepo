package com.reece.pickingapp.utils.extensions

import com.reece.pickingapp.PickTasksQuery
import com.reece.pickingapp.models.ProductDTO
import com.reece.pickingapp.utils.emptyString

fun PickTasksQuery.UserPick.toProductDTO() = ProductDTO(
    productId,
    description,
    quantity,
    uom,
    locationType,
    location,
    lot,
    splitId,
    orderId,
    generationId,
    lineId,
    shipVia,
    tote,
    userId,
    branchId,
    cutDetail,
    cutGroup,
    isParallelCut,
    warehouseID,
    isLot,
    isSerial,
    pickGroup,
    true,
    emptyString,
    true
)
