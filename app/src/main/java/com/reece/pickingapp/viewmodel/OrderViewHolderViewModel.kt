package com.reece.pickingapp.viewmodel

import android.text.Spanned
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.exception.ApolloException
import com.reece.pickingapp.R
import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.type.ShippingDetailsKourierInput
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.SHIPPING_INSTRUCTION_MAX_LINE
import com.reece.pickingapp.utils.SHIPPING_INSTRUCTION_MIN_LINE
import com.reece.pickingapp.utils.StringConverter
import kotlinx.coroutines.launch

class OrderViewHolderViewModel(
    private val activityService: ActivityService,
    private val stringConverter: StringConverter,
    val orderViewModel: OrderViewModel,
    private val onItemClicked: ((orderViewModel: OrderViewModel) -> Unit),
    val repository: PickingRepository,

) : ViewModel() {

    private lateinit var notifyItemChanged: () -> Unit
    var shippingDetails = MutableLiveData("")
    var shippingDetailsVisibility = MutableLiveData(false)
    var showMoreOrLessVisibility = MutableLiveData(false)
    var shippingDetailsMaxLines = MutableLiveData(1)
    var showMoreOrLessLabel = MutableLiveData("")

    fun setUp(notifyItemChanged: () -> Unit) {
        queryShippingDetails()
        this.notifyItemChanged = notifyItemChanged
    }

    //region Utils

    fun getCustomerText(): Spanned? {
        orderViewModel.shipToName?.let { customerName ->
            customerName?.let {
                return stringConverter.convertToHtmlSpan(it)
            }
        }

        return null
    }

    fun getShipViaText(): Spanned? {
        orderViewModel.shipVia?.let { shipVia ->
            shipVia?.let {
                return stringConverter.convertToHtmlSpan(it)
            }
        }

        return null
    }

    fun getPickCount(): Spanned? {
        orderViewModel.order.pickCount?.let { customerName ->
            customerName?.let {
                return stringConverter.convertToHtmlSpan(it)
            }
        }

        return null
    }

    fun getOrderText(): Spanned? {
        orderViewModel.getOrderText()?.let {
            activityService.getString(R.string.order_number_spannable, it)?.let {
                return stringConverter.convertToHtmlSpan(it)
            }
        }
        return null
    }

    //endregion

    //region Handlers

    fun cardTapped() {
        activityService.hideKeyboard()
        onItemClicked(orderViewModel)
    }

    fun showMoreOrLessButtonTapped() {
        if (shippingDetailsMaxLines.value == SHIPPING_INSTRUCTION_MIN_LINE){
            shippingDetailsMaxLines.value = SHIPPING_INSTRUCTION_MAX_LINE
            showMoreOrLessLabel.value = activityService.getString(R.string.show_less)
        }else{
            shippingDetailsMaxLines.value = SHIPPING_INSTRUCTION_MIN_LINE
            showMoreOrLessLabel.value = activityService.getString(R.string.show_more)
        }
        notifyItemChanged.invoke()
    }

    //endregion

    private fun  queryShippingDetails() = viewModelScope.launch {
        try {
            repository.queryShippingDetails(
                ShippingDetailsKourierInput(orderViewModel.getOrderIdValue())
            )?.also { response ->
                    shippingDetails.value = ""
                    if (response.errors.isNullOrEmpty()) {
                        response.data?.shippingDetails?.shippingtext?.let { it.forEach { shippingtext ->
                            shippingtext.shippingInstructions?.forEach { shippingDetail ->
                                if (shippingDetails.value.isNullOrEmpty()) {
                                    shippingDetails.value = shippingDetail
                                } else {
                                    shippingDetails.value =
                                        "${shippingDetails.value}\n$shippingDetail"
                                }
                            }
                        }
                        }
                        if ((response.data?.shippingDetails?.shippingtext?.get(0)?.shippingInstructions?.size ?: 0) > 1){
                            showMoreOrLessVisibility.value = true
                            showMoreOrLessLabel.value = activityService.getString(R.string.show_more)
                        }
                        shippingDetailsVisibility.value = (shippingDetails.value?.length ?: 0) > 0
                    }
                notifyItemChanged.invoke()
                }
        } catch (e: ApolloException) {}
    }
}