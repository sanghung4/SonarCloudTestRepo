package com.reece.pickingapp.viewmodel

import android.os.Parcelable
import com.reece.pickingapp.models.AlternateLocationsDTO
import com.reece.pickingapp.models.ProductDTO
import com.reece.pickingapp.type.SerialLineInput
import com.reece.pickingapp.utils.emptyString
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    val product: ProductDTO
): Parcelable {
    val orderId: String? = product.orderId
    var productId: String = product.productId
    val description: String = product.description
    val location: String = product.location
    val quantity: Int = product.quantity
    val isSerial: Boolean = product.isSerial
    val warehouseID: String = product.warehouseID
    var productImageUrl: String? = emptyString
    var qtyPicked: String? = emptyString
    var isBackOrder: Boolean = false
    var customerName: String? = emptyString
    var alternateLocationDTO: MutableList<AlternateLocationsDTO> = mutableListOf()
    var defaultLocationSerialLineInput: List<SerialLineInput> = mutableListOf()

    fun getProductDTO(): ProductDTO {
        return product
    }

    fun setTote() {
        product.tote = "TOTE${orderId}"
    }

    fun setStartPickTime(time: String) {
        product.startPickTime = time
    }

    override fun toString(): String {
        return "ProductModel(product=$product, orderId=$orderId, productId='$productId', description='$description', location='$location', quantity=$quantity, isSerial=$isSerial, warehouseID='$warehouseID', productImageUrl=$productImageUrl, qtyPicked=$qtyPicked, isBackOrder=$isBackOrder, alternateLocationDTO=$alternateLocationDTO)"
    }


}