package com.reece.pickingapp.viewmodel

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reece.pickingapp.R
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.emptyString

class ReportProductViewHolderViewModel(
    val product: ProductModel,
    val lifecycleOwner: LifecycleOwner
) : ViewModel() {
    val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }
    private var productModelReport = MutableLiveData<ProductModel>()
    private lateinit var activityService: ActivityService

    //UI
    val txtShipQty: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val txtPickedQty: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val txtDescription: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val txtDescPNNumber: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val txtDescLocation: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val altLocationDesc: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val altLocationQty: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val altLocationDescVisibility: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val altLocationQtyVisibility: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val isBackOrder: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun setUp(activityService: ActivityService) {
        productModelReport.value = product
        this.activityService = activityService
        isBackOrder.value = View.GONE
        setProductUI()
        setAltLocation()
    }

     fun setProductUI() {
        txtShipQty.value = product.quantity.toString()
        txtPickedQty.value = product.qtyPicked.toString()
        txtDescription.value = product.description
        txtDescPNNumber.value = product.productId
        txtDescLocation.value = setDefaultLocationSerialLineInput()
    }

    fun setDefaultLocationSerialLineInput ()  : String {
        val nextLine = "\n"
        var thisProductTable = product.location + nextLine
        if (product.defaultLocationSerialLineInput.isNotEmpty()){
            thisProductTable += "Serial Number(s)"
            thisProductTable += nextLine
        }
        product.defaultLocationSerialLineInput.forEach { thisSerial ->
            thisProductTable += thisSerial.serial + nextLine
        }
        return thisProductTable
    }

    private fun setAltLocation() {
        val nextLine = "\n"
        if (product.alternateLocationDTO.isNotEmpty()) {
            //concat qty
            var altQtyString = emptyString
            var altString = emptyString
            var myPosition = 1
            //concat al locations
            product.alternateLocationDTO.forEach { locationDTO ->
                //validate if the product is serial
                altQtyString += locationDTO.qty
                altString += (activityService.getString(
                    R.string.label_location,
                    myPosition.toString()
                ))
                altString += " "
                altString += (locationDTO.location)
                altString += nextLine
                altQtyString += nextLine
                if (!locationDTO.serialNumbers.isNullOrEmpty()) {
                    //get location
                    altQtyString += nextLine
                    altString += nextLine
                    altString += (activityService.getString(R.string.label_serial_number))
                    locationDTO.serialNumbers.forEach { _serialNumber ->
                        altString += nextLine
                        altQtyString += nextLine
                        altString += _serialNumber
                    }
                    altString += nextLine
                    altQtyString += nextLine
                    altString += nextLine
                    altQtyString += nextLine
                }
                myPosition++
            }
            //set the string
            altLocationDesc.value = altString
            altLocationQty.value = altQtyString
        }
        //validate if is back order
        if (product.isBackOrder)
            isBackOrder.value = View.VISIBLE
    }
}