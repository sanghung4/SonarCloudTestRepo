package com.reece.pickingapp.viewmodel

import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.textfield.TextInputLayout
import com.google.mlkit.vision.barcode.common.Barcode
import com.reece.pickingapp.R
import com.reece.pickingapp.interfaces.BarCodeScanInterface
import com.reece.pickingapp.interfaces.DataEntryInterface
import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.type.SerialLineInput
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.DataEntryFieldType
import com.reece.pickingapp.utils.emptyString
import com.reece.pickingapp.utils.extensions.filterToNum
import com.reece.pickingapp.utils.extensions.setErrorLog
import com.reece.pickingapp.view.state.CardState
import com.reece.pickingapp.view.state.InputFocusState
import com.reece.pickingapp.view.state.InputState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProductViewHolderViewModel(
    private val activityService: ActivityService,
    val product: ProductModel,
    val lifecycleOwner: LifecycleOwner,
    private val onItemClicked: (userPick: ProductModel, failureCallback: () -> Unit) -> Unit,
    private val onItemClickedIsSerial: (userPick: ProductModel, serialNumbers: List<SerialLineInput>, failureCallback: () -> Unit) -> Unit,
    private val onSplitQuantity: ((userPick: ProductModel, serialNumbers: List<SerialLineInput>, quantityPicked: Int, failureCallback: () -> Unit) -> Unit),
    private val scrollTo: ((position: Int, offset: Int) -> Unit),
    private val onMoveCursor: (adapterPosition: Int) -> Unit,
    private val onBarcodeScanClick: (userPick: ProductModel, successCallback: (barcode: String) -> Unit, failureCallback: () -> Unit) -> Unit,
    val repository: PickingRepository,
    private val showQtyDialog: (product:ProductModel, userPick: Int) -> Unit
) : ViewModel(), DataEntryInterface, BarCodeScanInterface {

    //region LiveData Vars

    val cardValidationState = MutableLiveData<CardState>()
    var productIdDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    var quantityDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    val quantityEditString = MutableLiveData("0")
    var productSerialNumberViewModels = MutableLiveData<MutableList<DataEntryViewModel>>()
    var productImageUrl = MutableLiveData("")
    var confirmSplitButtonEnabled = MutableLiveData(false)
    var splitQtyIsActive = MutableLiveData(false)
    var serialNumbersHolderYPos = 0
    var splitButtonEnabled = MutableLiveData(false)
    var isAutoFillEnable = MutableLiveData(true)
    //endregion

    var adapterPosition: Int = 0

    fun setUp() {
        setUpProductIdEntryViewModel()
        setProductIdState(InputState.Default())
        setUpQuantityEntryViewModel()
        if (adapterPosition == 0) {
            setProductIdFocusState(InputFocusState.Focussed)
        }

        if (!product.isSerial) {
            if(!product.qtyPicked.isNullOrEmpty()){
                setCurrentQuantity(product.qtyPicked)
            }else{
                setCurrentQuantity(product.quantity.toString())
            }
            setQuantityState(InputState.Default())
        }
        else {
            setQuantityState(InputState.Disabled())
            quantityDataEntryViewModel.value?.setInputFocusState(InputFocusState.NonFocusable)
            quantityDataEntryViewModel.value?.entryString?.value = "0"

        }

        validateProductId(true)
        queryPickProductImage()
    }

    fun validateQuantity(): Boolean {
        val qtyString = quantityDataEntryViewModel.value?.getInputString()
        if (!qtyString.isNullOrEmpty()) {
            qtyString.toInt().let { capturedQty ->
                when {
                    capturedQty > product.quantity -> {
                        val errorMessage = activityService.getString(
                            R.string.exceed_quantity, emptyString
                        )
                        setQuantityState(InputState.Error(message = errorMessage))
                        return false
                    }
                    capturedQty == product.quantity -> {
                        setQuantityState(InputState.Default())
                        return true
                    }
                    else -> {
                        setQuantityState(InputState.Default())
                        return false
                    }
                }
            }
        } else {
            val errorMessage = activityService.getString(
                R.string.invalid_quantity, emptyString
            )
            setQuantityState(InputState.Error(message = errorMessage))
            return false
        }
    }

    //region Validation

    fun validateProductId(isInitialLoad: Boolean = false, textView: TextView? = null) {
        if (productIdIsValid()) {
            setCardValidationState(CardState.Ready())
            if (product.isSerial) {
                if (allSerialNumbersAdded()) {
                    activityService.hideKeyboard()
                }

                productSerialNumberViewModels.value?.first()?.getTextInputId()?.let { id ->
                    textView?.nextFocusDownId = id
                    if (productSerialNumberViewModels.value?.first()?.getInputString()
                            .isNullOrEmpty()
                    ) {
                        productSerialNumberViewModels.value?.first()
                            ?.setInputState(InputState.Default())
                    }
                }


                focusFirstSerial()
                setCurrentQuantity()
            } else {
                activityService.hideKeyboard()
            }

            val successMessage = activityService.getString(R.string.valid_product_id)
            setProductIdState(InputState.Success(successMessage))

        } else if (!isInitialLoad) {
            val errorMessage = activityService.getString(
                R.string.invalid_product_id,
                productIdDataEntryViewModel.value?.getInputString() ?: emptyString
            )
            setProductIdState(InputState.Error(message = errorMessage))
            setProductIdFocusState(InputFocusState.Focussed)
        }
    }


    fun validateAllSerialNumbers() :Boolean {
        setCardValidationState(CardState.Ready())
        var validationIsError = false
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            for (serialViewModel in serialNumberViewModels.reversed()) {
                if (serialViewModel.canBeValidated()) {
                    val isDuplicate = serialNumberIsDuplicate(serialViewModel)
                    val isBlank = serialViewModel.entryIsBlank()

                    if (isDuplicate || isBlank) {
                        val errorMessage: String? = if (isDuplicate) {
                            activityService.getString(R.string.duplicate_serial_message)
                        } else {
                            activityService.getString(R.string.serial_number_blank)
                        }
                        validationIsError = true
                        serialViewModel.setInputState(InputState.Error(errorMessage))
                    } else {
                        val successMessage = activityService.getString(R.string.serial_num_valid)
                        serialViewModel.setInputState(InputState.Success(successMessage))
                    }
                }
            }

            setCurrentQuantity()
            focusNextSerialEntry(isError = validationIsError)
            if (productIdIsValid() && allSerialNumbersAdded()) {
                activityService.hideKeyboard()
            }
        }
        return validationIsError

    }

    private fun productIdIsValid(): Boolean {
        return product.productId == productIdDataEntryViewModel.value?.getInputString()
    }

    private fun allSerialNumbersAdded(): Boolean {
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            return totalAddedSerialNumbers() == serialNumberViewModels.count()
        }

        return false
    }

    private fun serialNumberIsDuplicate(serialToCheck: DataEntryViewModel): Boolean {
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            val hasString = serialNumberViewModels.filter { serial ->
                serial != serialToCheck && serial.getInputString() == serialToCheck.getInputString() && !serial.entryIsBlank()
            }
            return hasString.isNotEmpty()
        }

        return true
    }

    //endregion

    //region Queries & mutations

    private fun queryPickProductImage() = viewModelScope.launch {
        try {
            repository.queryPickingProductImage(
                product.productId
            )
                ?.also { response ->
                    if (response.errors.isNullOrEmpty()) {
                        response.data?.productImageUrl?.let {
                            productImageUrl.value = it
                            product.productImageUrl = it}
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
    //endregion

    //region LiveData Updates

    private fun setUpProductIdEntryViewModel() {
        productIdDataEntryViewModel.value?.setHintString(activityService.getString(R.string.product_id_label_string))
        productIdDataEntryViewModel.value?.setInputCanBeCleared(false)
        productIdDataEntryViewModel.value?.setDataEntryFieldType(DataEntryFieldType.SCANNABLE_PRODUCT)
    }

    private fun setProductIdState(state: InputState) {
        productIdDataEntryViewModel.value?.setInputState(state)
    }

    private fun setQuantityState(state: InputState) {
        quantityDataEntryViewModel.value?.setInputState(state)
    }

    fun setProductIdFocusState(state: InputFocusState) {
        productIdDataEntryViewModel.value?.setInputFocusState(state)
    }

    private fun setCardValidationState(state: CardState) {
        cardValidationState.value = state
    }

    private fun setCurrentQuantity(newQuantity: String? = null) {
        quantityDataEntryViewModel.value?.entryString?.value =
            newQuantity ?: totalAddedSerialNumbers().toString()
    }

    private fun setAllSerialInputsCanBeCleared(canBeCleared: Boolean) {
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            for (serial in serialNumberViewModels) {
                serial.setInputCanBeCleared(canBeCleared)
            }
        }
    }


    private fun setUpQuantityEntryViewModel() {
        quantityDataEntryViewModel.value?.setHintString(activityService.getString(R.string.product_quantity_label_string))
        quantityDataEntryViewModel.value?.setEndIconMode(TextInputLayout.END_ICON_NONE)
        quantityDataEntryViewModel.value?.setDataEntryFieldType(DataEntryFieldType.QUANTITY_NUMBER)
        quantityDataEntryViewModel.value?.setInputCanBeCleared(false)
    }


    //endregion

    //region DataEntryInterface Methods

    override fun onEditorAction(textView: TextView, actionId: Int, keyEvent: KeyEvent?): Boolean {

        if ((keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent?.action == KeyEvent.ACTION_UP) ||
            actionId == EditorInfo.IME_ACTION_NEXT ||
            actionId == EditorInfo.IME_ACTION_DONE ||
            actionId == EditorInfo.IME_ACTION_GO
        ) {
            if (textView.hashCode() == quantityDataEntryViewModel.value?.getTextInputId()) {
                quantityDataEntryViewModel.value?.setInputFocusState(InputFocusState.ClearFocus)
                activityService.hideKeyboard()
                validateQuantity()
                return true
            }
            textView.nextFocusDownId = textView.id
            if (inputIdIsSerialEntry(textView.hashCode())) {
                validateAllSerialNumbers()
            } else {
                validateProductId(textView = textView)
            }
        }

        return true
    }

    override fun onTextChanged(id: Int, s: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun onInputComponentFocus(component: ViewGroup) {
        GlobalScope.launch {
            delay(500)
            val inputY = component.y
            val inputHeight = component.height
            val offset = serialNumbersHolderYPos + inputY - (inputHeight / 2)
            scrollTo(adapterPosition, offset.roundToInt())
        }
    }

    override fun deleteButtonTapped(data: DataEntryViewModel) {
        super.deleteButtonTapped(data)
        validateAllSerialNumbers()
    }

    fun autoFillBtnClick() {
        productIdDataEntryViewModel.value?.entryString?.value = product.productId
        validateProductId(false)
        isAutoFillEnable.value = false
        if (product.isSerial) {
            GlobalScope.launch {
                delay(500)
                activityService.showKeyboard()
            }
        }
    }
    //endregion

    //region Actions

    fun submitButtonTapped() {
        activityService.hideKeyboard()
        quantityDataEntryViewModel.value?.setInputFocusState(InputFocusState.ClearFocus)
        productIdDataEntryViewModel.value?.setInputFocusState(InputFocusState.ClearFocus)

        if (isSplitQty()) {
            product.qtyPicked = quantityDataEntryViewModel.value?.getInputString()
            if(product.isSerial){
                product.defaultLocationSerialLineInput = getAllSerialNumbers()

            }
            showQtyDialog(product,adapterPosition)
        } else {
            if (validateQuantity() && !validateAllSerialNumbers()) {
                setCardValidationState(CardState.Loading())
                setAllSerialInputsCanBeCleared(false)
                onMoveCursor(adapterPosition)

                if (product.isSerial) {
                    onItemClickedIsSerial(product, getAllSerialNumbers()) {
                        cardFailureCallback()
                    }
                } else {
                    onItemClicked(product) {
                        cardFailureCallback()
                    }
                }


            }
        }
    }

    private fun cardFailureCallback() {
        setCardValidationState(CardState.Error(emptyString))
        if (product.isSerial) {
            removeSerialNumbers()
            setCurrentQuantity()
            focusNextSerialEntry(isError = true)
            setAllSerialInputsCanBeCleared(true)
        }
    }

    //endregion

    //region Serial Number Methods

    fun setSerialHolderPos(yPos: Int) {
        serialNumbersHolderYPos = yPos
    }

    private fun inputIdIsSerialEntry(id: Int): Boolean {
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            val filtered = serialNumberViewModels.filter { serial ->
                serial.getTextInputId() == id
            }

            return filtered.isNotEmpty()
        }

        return false
    }

    private fun getAllSerialNumbers(): List<SerialLineInput> {
        val listInput = mutableListOf<SerialLineInput>()
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            for ((i, serialViewModel) in serialNumberViewModels.withIndex()) {
                var line = i
                val serialNumber = serialViewModel.getInputString()
                if (!serialNumber.isNullOrEmpty()) {
                    listInput.add(SerialLineInput(line++ + 1, serialNumber))
                }
            }
        }

        return listInput
    }


    //TODO: will accept string value to reset in later ticket
    private fun removeSerialNumbers() {
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            for (serialViewModel in serialNumberViewModels) {
                val errorMessage = activityService.getString(R.string.serial_num_reset)
                serialViewModel.setInputState(InputState.Error(errorMessage))
                serialViewModel.entryString.value = ""
            }
        }
    }

    private fun focusFirstSerial() {
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            GlobalScope.launch {
                delay(50)
                serialNumberViewModels.first().setInputFocusState(InputFocusState.Focussed)
            }
        }
    }

    private fun focusNextSerialEntry(isError: Boolean = false) {
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            val filtered = serialNumberViewModels.filter { serial ->
                serial.entryIsBlank()
            }

            if (filtered.isNotEmpty()) {
                val serialViewModel = filtered.first()

                if (!isError) {
                    serialViewModel.setInputState(InputState.Default())
                }

                serialViewModel.setInputFocusState(InputFocusState.Focussed)
            }
        }
    }

    private fun totalAddedSerialNumbers(): Int {
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            val filtered = serialNumberViewModels.filter { serial ->
                !serial.entryIsBlank()
            }

            return filtered.count()
        }

        return 0
    }

    //endregion

    //region Split Quantity Processing

    private fun isSplitQty(): Boolean {
        val qtyString = quantityDataEntryViewModel.value?.getInputString()
        if (!qtyString.isNullOrEmpty()) {
            qtyString.toInt().let { capturedQty ->
                return when {
                    capturedQty < product.quantity -> true
                    capturedQty == product.quantity -> false
                    capturedQty == 0 -> true
                    else -> {
                        false
                    }
                }
            }
        } else {
            return false
        }

    }




    override fun onClickScan(id: Int, hint: String) {
        onBarcodeScanClick(product, {
            onBarcodeScanned(it, emptyList(), id, hint)
        }) {

        }

    }

    override fun onBarcodeScanned(
        barcode: String,
        barcodes: List<Barcode>,
        id: Int,
        hint: String
    ) {
        super.onBarcodeScanned(barcode, barcodes, id, hint)
        val matchedSerialIds = productSerialNumberViewModels.value?.filter {
            it.getTextInputId() == id
        }

        if (matchedSerialIds.isNullOrEmpty()) {
            if (hint == activityService.getString(R.string.product_id_label_string)) {
                productIdDataEntryViewModel.value?.entryString?.value = (barcode.filterToNum())
                validateProductId(false)
            } else if (hint.contains(
                    activityService.getString(R.string.serial_entry_hint).toString()
                        .substring(0, 13)
                )
            ) {
                productSerialNumberViewModels.value?.get(
                    hint.replace("[^0-9]".toRegex(), "").toInt() - 1
                )?.entryString?.value = (barcode)
                validateAllSerialNumbers()
            }
        } else {
            matchedSerialIds.first().entryString.value = barcode
            focusNextSerialEntry()
            validateAllSerialNumbers()
        }
    }

    //endregion
}
