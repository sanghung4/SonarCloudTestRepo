package com.reece.pickingapp.viewmodel

import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.common.Barcode
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.DataEntryBinding
import com.reece.pickingapp.interfaces.BarCodeScanInterface
import com.reece.pickingapp.interfaces.DataEntryInterface
import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.type.SerialLineInput
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.DataEntryFieldType
import com.reece.pickingapp.utils.emptyString
import com.reece.pickingapp.view.state.CardState
import com.reece.pickingapp.view.state.InputFocusState
import com.reece.pickingapp.view.state.InputState

class PickFromOtherLocationsViewHolderViewModel(
    private val activityService: ActivityService,
    val product: ProductModel,
    val lifecycleOwner: LifecycleOwner,
    val repository: PickingRepository,
    private val onRemoveItem: (position:Int) -> Unit,
    private val onChangeQtyLabel: () -> Unit,
    private val onBarcodeScanClick: (userPick: ProductModel, successCallback: (barcode: String) -> Unit, failureCallback: () -> Unit) -> Unit,

) : ViewModel(), DataEntryInterface, BarCodeScanInterface {

    //region LiveData Vars

    var locationDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    var quantityDataEntryViewModel = MutableLiveData(DataEntryViewModel())

    val cardValidationState = MutableLiveData<CardState>()
    var productSerialNumberViewModels = MutableLiveData<MutableList<DataEntryViewModel>>()

    var serialNumbersHolderYPos = 0
    //endregion
    var adapterPosition: Int = 0
    var deleteButtonVisibility = View.VISIBLE
    lateinit var linearLayout:LinearLayout

    fun setUp(layout: LinearLayout) {
        setUpProductIdEntryViewModel()
        setLocationState(InputState.Default())
        setUpQuantityEntryViewModel()
        setQuantityState(InputState.Default())
        linearLayout = layout
    }

    fun validateAllSerialNumbersIsError() :Boolean {
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


                }else{
                    validationIsError = true
                }
            }

            focusNextSerialEntry(isError = validationIsError)
            if (allSerialNumbersAdded()) {
                activityService.hideKeyboard()
            }
        }
        return validationIsError
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

    //region LiveData Updates

    private fun setUpProductIdEntryViewModel() {
        locationDataEntryViewModel.value?.setHintString(activityService.getString(R.string.product_location_label_string))
        locationDataEntryViewModel.value?.setInputCanBeCleared(false)
        locationDataEntryViewModel.value?.setDataEntryFieldType(DataEntryFieldType.DEFAULT)
    }


    private fun setCardValidationState(state: CardState) {
        cardValidationState.value = state
    }

    private fun cardStateIsNotInErrorState(): Boolean {
        return when (cardValidationState.value) {
            is CardState.Error -> false
            else -> true
        }
    }

    private fun setAllSerialInputsCanBeCleared(canBeCleared: Boolean) {
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            for (serial in serialNumberViewModels) {
                serial.setInputCanBeCleared(canBeCleared)
            }
        }
    }

    //endregion

    //region DataEntryInterface Methods

    override fun onEditorAction(textView: TextView, actionId: Int, keyEvent: KeyEvent?): Boolean {

        if ((keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) ||
                actionId == EditorInfo.IME_ACTION_NEXT ||
                actionId == EditorInfo.IME_ACTION_DONE){
            validateAlternateLocation()
        }

        if ((keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) ||
            actionId == EditorInfo.IME_ACTION_NEXT ||
            actionId == EditorInfo.IME_ACTION_DONE
        ) {
            if (isInputIdIsSerialEntry(textView.hashCode())) {
                validateAllSerialNumbersIsError()
            }
        }
        return true
    }

    override fun onTextChanged(id: Int, s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(id: Int, s: Editable) {
        if(id == quantityDataEntryViewModel.value?.getTextInputId()){
            onChangeQtyLabel()
            if(product.isSerial){
                val currentSerialQty = quantityDataEntryViewModel.value?.entryString?.value
                if(!currentSerialQty.isNullOrEmpty()){
                    this.pickFromAnotherSerial(currentSerialQty.toInt())
                }else{
                    this.pickFromAnotherSerial(0)
                }
                validateAlternateLocation()
            }
        }

    }

    override fun deleteButtonTapped(data: DataEntryViewModel) {
        super.deleteButtonTapped(data)
        validateAllSerialNumbersIsError()
    }

    private fun cardFailureCallback() {
        setCardValidationState(CardState.Error(emptyString))
        if (product.isSerial) {
            removeSerialNumbers()
            focusNextSerialEntry(isError = true)
            setAllSerialInputsCanBeCleared(true)
        }
    }

    //endregion

    //region Serial Number Methods

    fun setSerialHolderPos(yPos: Int) {
        serialNumbersHolderYPos = yPos
    }

    private fun isInputIdIsSerialEntry(id: Int): Boolean {
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            val filtered = serialNumberViewModels.filter { serial ->
                serial.getTextInputId() == id
            }
            return filtered.isNotEmpty() //this id is a serial number
        }
        return false
    }

    private fun serialInputIsNotInErrorState(serialId: Int): Boolean {
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            val filtered = serialNumberViewModels.filter { serial ->
                serial.getTextInputId() == serialId
            }

            filtered.first().getInputStateValue()?.let { state ->
                return when (state) {
                    is InputState.Error -> false
                    else -> true
                }
            }
        }

        return false
    }

    fun getAllSerialNumbers(): List<SerialLineInput> {
        val listInput = mutableListOf<SerialLineInput>()
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            for ((i, serialViewModel) in serialNumberViewModels.withIndex()) {
                var line = i
                val serialNumber = serialViewModel.getInputString()
                if (!serialNumber.isNullOrEmpty()){
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
                serialViewModel.entryString.value=""
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

    private fun hideEmptySerialEntry() {
        productSerialNumberViewModels.value?.let { serialNumberViewModels ->
            val filtered = serialNumberViewModels.filter { serial ->
                serial.entryIsBlank()
            }

            for (serialViewModel in filtered) {
                serialViewModel.setInputState(InputState.Hidden())
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
            if(hint.contains(activityService.getString(R.string.serial_entry_hint).toString().substring(0,13))){
                productSerialNumberViewModels.value?.get(hint.replace("[^0-9]".toRegex(),"").toInt()-1)?.entryString?.value = (barcode)
                validateAllSerialNumbersIsError()
            }
        } else {
            matchedSerialIds.first().entryString.value = barcode
            focusNextSerialEntry()
            validateAllSerialNumbersIsError()
        }
    }

    fun onRemoveItemClick() {
        onRemoveItem(adapterPosition)
    }
    private fun setLocationState(state: InputState) {
        locationDataEntryViewModel.value?.setInputState(state)
    }
    private fun setUpQuantityEntryViewModel() {
        quantityDataEntryViewModel.value?.setHintString(activityService.getString(R.string.product_quantity_label_string))
        quantityDataEntryViewModel.value?.setInputCanBeCleared(true)
        quantityDataEntryViewModel.value?.setDataEntryFieldType(DataEntryFieldType.NUMBER_ENTRY)
    }
    private fun setQuantityState(state: InputState) {
        quantityDataEntryViewModel.value?.setInputState(state)
    }


    fun validateAlternateLocation(): Boolean{
        var isValid = true
        if(locationDataEntryViewModel.value?.getInputString().isNullOrEmpty()){
            locationDataEntryViewModel.value?.setInputState(InputState.Error(
                activityService.getString(R.string.alternate_location_blank)
            ))
            isValid =  false;
        }

        val alternateQty: String? = quantityDataEntryViewModel.value?.getInputString()

        if (!alternateQty.isNullOrEmpty()) {
            if (alternateQty.toInt()<1){ //Cannot save an alternate qty null or 0
                quantityDataEntryViewModel.value?.setInputState(InputState.Error(
                    activityService.getString(R.string.invalid_quantity)
                ))
                isValid =  false
            }
        }else{
            quantityDataEntryViewModel.value?.setInputState(InputState.Error(
                activityService.getString(R.string.invalid_quantity)
            ))
            isValid = false
        }


        if(!isValid || validateAllSerialNumbersIsError()){
            return false
        }else{
            locationDataEntryViewModel.value?.setInputState(InputState.Default())
            quantityDataEntryViewModel.value?.setInputState(InputState.Default())
            return true
        }
    }


    fun pickFromAnotherSerial(selectedQuantity:Int) {
        //Reset all views and view models
        linearLayout.removeAllViews()
        productSerialNumberViewModels.value = mutableListOf()


        for (i in 0 until selectedQuantity) {
            val binding: DataEntryBinding = DataBindingUtil.inflate(
                LayoutInflater.from(linearLayout.context),
                R.layout.data_entry,
                linearLayout,
                false
            )
            binding.root.apply {
                id = i
                nextFocusDownId = i + 1
            }
            binding.lifecycleOwner = lifecycleOwner
            binding.dataEntryInterface = this
            binding.barcodeScanInterface = this

            //Check if SerialViewModel already exists

            if (productSerialNumberViewModels.value?.indices?.contains(i) == true) {
                productSerialNumberViewModels.value?.let { serialList ->
                    binding.dataEntryViewModel = serialList[i]
                }
            } else {
                var dataEntryViewModel = DataEntryViewModel()
                dataEntryViewModel.setDataEntryFieldType(DataEntryFieldType.SCANNABLE_SERIAL)
                var serialNum = i
                serialNum++
                dataEntryViewModel.setHintString(
                    linearLayout.context.getString(
                        R.string.serial_entry_hint,
                        serialNum.toString()
                    )
                )
                dataEntryViewModel.setInputState(InputState.Default())
                productSerialNumberViewModels.value?.add(dataEntryViewModel)
                binding.dataEntryViewModel = dataEntryViewModel
            }
            linearLayout.addView(binding.root)
        }
    }
    //endregion
}
