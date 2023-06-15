package com.reece.pickingapp.models

import android.os.Parcelable
import com.apollographql.apollo.api.toInput
import com.reece.pickingapp.type.PickingTaskInput
import com.reece.pickingapp.utils.OrderState
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDTO(
    val orderId: String?,
    val generationId: Int?,
    val invoiceId: String?,
    val branchId: String?,
    val pickGroup: String?,
    val assignedUserId: String?,
    val billTo: Int?,
    val shipTo: Int?,
    val shipToName: String?,
    val pickCount: String?,
    val shipVia: String?,
    val isFromMultipleZones: Boolean?,
    val taskState: String?,
    val taskWeight: Double?
) : Parcelable {
    fun toPickingTaskInput(
        userName: String,
        orderState: String = OrderState.ASSIGNED
    ): PickingTaskInput {
        return PickingTaskInput(
            orderId.toInput(),
            generationId.toInput(),
            invoiceId.toInput(),
            branchId.toInput(),
            pickGroup.toInput(),
            userName.toInput(),
            billTo.toInput(),
            shipTo.toInput(),
            shipToName.toInput(),
            pickCount.toInput(),
            shipVia.toInput(),
            isFromMultipleZones.toInput(),
            taskWeight.toInput(),
            orderState.toInput()
        )
    }
}
