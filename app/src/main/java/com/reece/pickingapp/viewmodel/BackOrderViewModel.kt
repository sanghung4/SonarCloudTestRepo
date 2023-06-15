package com.reece.pickingapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.snackbar.Snackbar
import com.reece.pickingapp.R
import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.type.SerialLineInput
import com.reece.pickingapp.type.UpdateProductSerialNumbersInput
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.SnackBarType
import com.reece.pickingapp.utils.emptyString
import com.reece.pickingapp.utils.extensions.setErrorLog
import com.reece.pickingapp.view.state.SnackBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class BackOrderViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    val repository: PickingRepository,
    private val activityService: ActivityService,
) : ViewModel() {
    private var TAG = this::class.java.simpleName
    private lateinit var viewLifecycleOwner: LifecycleOwner
    lateinit var navigateAfterStagingCallback: () -> Unit

    val product = MutableLiveData<ProductModel>()
    private val _showProgressBar = MutableLiveData<Int?>(null)
    val showProgressBar: LiveData<Int?>
        get() = _showProgressBar

    fun setUp(
        lifecycleOwner: LifecycleOwner,
        navigationForStagingCallback: () -> Unit,
        selectedProduct: ProductModel
    ) {
        viewLifecycleOwner = lifecycleOwner
        product.value = selectedProduct
        navigateAfterStagingCallback = navigationForStagingCallback
//validar url de la imagen
        validateImg()
    }

    private fun validateImg() {
        if (product.value?.productImageUrl.isNullOrEmpty()) {
            product.value?.productId?.let { queryPickProductImage(it) }
        }
    }

    private fun queryPickProductImage(productId: String) = viewModelScope.launch {
        try {
            repository.queryPickingProductImage(
                productId
            )
                ?.also { response ->
                    if (response.errors.isNullOrEmpty()) {
                        response.data?.productImageUrl?.let {
                            product.value?.productImageUrl = it
                        }
                    } else {
                        activityService.userPreferences.let {
                            setErrorLog(it!!, response.errors.toString())
                        }

                    }
                }
        } catch (e: ApolloException) {
            Log.w("Image Load Error", "Image response error")
        }
    }

    fun yesButtonTapped() {
        _showProgressBar.value = 1
        if (product.value!!.isSerial && product.value!!.defaultLocationSerialLineInput.isNotEmpty()){
            onItemClickedIsSerial(product.value!!, product.value!!.defaultLocationSerialLineInput)
        }else{
            submitPickItem(product.value!!)
        }

    }

    private val onItemClickedIsSerial: ((
        userPick: ProductModel,
        serialNumbers: List<SerialLineInput>,
    ) -> Unit) = { userPick, serialNumbers ->
        updateProductSerialNumbers(
            UpdateProductSerialNumbersInput(
                getBranchId(),
                userPick.warehouseID,
                serialNumbers,
                true
            ),
            userPick
        )
    }

    private fun getBranchId(): String {
        return userPreferences.getBranch() ?: emptyString
    }


    fun updateProductSerialNumbers(
        updateProductSerialNumbersInput: UpdateProductSerialNumbersInput,
        product: ProductModel,
    ) = viewModelScope.launch {
        try {
            _showProgressBar.value = 1
            repository.mutationUpdateProductSerialNumbers(updateProductSerialNumbersInput)
                ?.also { response ->
                    if (response.errors.isNullOrEmpty()) {
                        submitPickItem(product)
                    } else {
                        _showProgressBar.value = 0
                        activityService.errorMessage(
                            activityService.getString(R.string.error_serial_unique)
                        )
                    }
                }
        } catch (e: ApolloException) {
            when{
                e.message.toString().contains("400") -> {
                    activityService.errorMessage(activityService.getString(R.string.error_400))
                }
                else -> {
                    activityService.errorMessage(activityService.getString(R.string.error_submit_serial))
                }
            }
            _showProgressBar.value = 0
        }
    }


    fun postAlertAndNavigateBack() {
        _showProgressBar.value = 0
        activityService.showMessage(
            SnackBarState(
                SnackBarType.SUCCESS,
                activityService.getString(R.string.snackbar_success_suc)
            ),
            Snackbar.LENGTH_SHORT
        )
        navigateAfterStagingCallback()
    }

    fun submitPickItem(
        product: ProductModel
    ) = viewModelScope.launch {
        try {
            // set start time and create unique tote label
            product.setStartPickTime(Instant.now().toString())
            product.setTote()
            repository.mutationCompletePick(product.getProductDTO())?.also { response ->
                if (response.errors.isNullOrEmpty()) {
                    //save back order
                    saveBackOrder()
                } else {
                    _showProgressBar.value = 0
                    setErrorLog(userPreferences, response.errors.toString())

                    when {
                        response.errors?.first()?.message.toString().contains("400") -> {
                            activityService.errorMessage(activityService.getString(R.string.error_400))
                            _showProgressBar.value = 0
                        }
                        else -> {
                            activityService.errorMessage(
                                response.errors?.first()?.message
                                    ?: activityService.getString(R.string.unknown)
                            )
                            _showProgressBar.value = 0
                        }
                    }
                }
            }
        } catch (e: ApolloException) {
            _showProgressBar.value = 0
            activityService.errorMessage(activityService.getString(R.string.error_submit_pick));
        }
    }

    fun saveBackOrder() {
        var backOrderList = userPreferences.getSplitQty()
        //i have to create a new order
        product.value?.isBackOrder = true
        backOrderList.productsList.add(product.value)
        userPreferences.saveSplitQty(backOrderList)
        postAlertAndNavigateBack()
    }
}