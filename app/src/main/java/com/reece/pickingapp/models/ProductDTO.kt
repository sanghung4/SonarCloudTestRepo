package com.reece.pickingapp.models

import android.os.Parcelable
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.toInput
import com.reece.pickingapp.type.CompletePickInput
import com.reece.pickingapp.utils.emptyString
import kotlinx.parcelize.Parcelize


@Parcelize
data class ProductDTO(
    var productId: String,
    val description: String,
    val quantity: Int,
    val uom: String,
    val locationType: String,
    val location: String,
    val lot: String,
    val splitId: String?,
    val orderId: String?,
    val generationId: Int?,
    val lineId: Int,
    val shipVia: String,
    var tote: String?,
    val userId: String,
    val branchId: String,
    val cutDetail: String?,
    val cutGroup:String?,
    val isParallelCut: Boolean?,
    val warehouseID: String,
    val isLot: String?,
    val isSerial: Boolean,
    val pickGroup: String?,
    val isOverrideProduct: Boolean?,
    var startPickTime: String?,
    val ignoreLockToteCheck: Boolean?
): Parcelable {
    fun toCompletePickInput(): CompletePickInput {
        return CompletePickInput(
            productId,
            description,
            quantity,
            uom,
            locationType,
            location,
            lot,
            splitId.toInput(),
            setOrderId().toInput(),
            generationId.toInput(),
            lineId,
            shipVia,
            tote.toInput(),
            userId,
            branchId,
            cutDetail.toInput(),
            cutGroup.toInput(),
            isParallelCut.toInput(),
            warehouseID,
            isLot.toInput(),
            isSerial,
            pickGroup.toInput(),
            isOverrideProduct ?: true,
            startPickTime ?: emptyString,
            ignoreLockToteCheck?: true
        )
    }

    fun setOrderId(): String? {
        if (!orderId.isNullOrEmpty()){
            return "$orderId.001"
        }
        return orderId
    }
}