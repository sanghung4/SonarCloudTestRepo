package com.reece.pickingapp.viewmodel

import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.snackbar.Snackbar
import com.reece.pickingapp.PickTasksQuery
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.DataEntryBinding
import com.reece.pickingapp.interfaces.BarCodeScanInterface
import com.reece.pickingapp.interfaces.DataEntryInterface
import com.reece.pickingapp.models.AlternateLocationsDTO
import com.reece.pickingapp.models.SplitQtyDTO
import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.type.SerialLineInput
import com.reece.pickingapp.type.UpdateProductSerialNumbersInput
import com.reece.pickingapp.utils.*
import com.reece.pickingapp.utils.extensions.setErrorLog
import com.reece.pickingapp.view.adapter.PickFromOtherLocationsAdapter
import com.reece.pickingapp.view.state.*
import com.reece.pickingapp.wrappers.PickFromOtherLocationsAdapterWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class PickFromOtherLocationsViewModel @Inject constructor(
    private val repository: PickingRepository,
    private val userPreferences: UserPreferences,
    private val activityService: ActivityService,
    private val stringConverter: StringConverter = StringConverter,
    private val pickFromOtherLocationsAdapterWrapper: PickFromOtherLocationsAdapterWrapper = PickFromOtherLocationsAdapterWrapper()
) : ViewModel(), DataEntryInterface, BarCodeScanInterface {

    //region LiveData Vars
    val loaderState = MutableLiveData<LoaderState>()
    var serialNumbersHolderYPos = 0
    var productImageUrl = MutableLiveData("")
    var locationDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    var quantityDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    var productSerialNumberViewModels = MutableLiveData<MutableList<DataEntryViewModel>>()
    val pickFromOtherLocationsAdapter = MutableLiveData<PickFromOtherLocationsAdapter>()
    val topErrorMessageState = MutableLiveData<PickFromOtherLocationsState>()
    val bottomErrorMessageState = MutableLiveData<PickFromOtherLocationsState>()
    val completeButtonState = MutableLiveData<PickFromOtherLocationsState>()
    var pickFromOtherLocationsState = MutableLiveData<PickFromOtherLocationsState>()
    val backOrderCheckBox = MutableLiveData<Boolean>(false)
    var successCallback:( (barcode: String) -> Unit)? = null
    lateinit var navigateAfterBarcodeScanCallback: () -> Unit



    //endregion
    lateinit var viewLifecycleOwner: LifecycleOwner
    lateinit var navigateAfterStagingCallback: () -> Unit
    lateinit var productData: ProductModel
    lateinit var viewHolderViewModel :PickFromOtherLocationsViewHolderViewModel



    private val _qtyLabel = MutableLiveData<String?>(null)
    val qtyLabel: LiveData<String?>
        get() = _qtyLabel

    private val _showProgressBar = MutableLiveData<Int?>(null)
    val showProgressBar: LiveData<Int?>
        get() = _showProgressBar


    fun setUp(
        product: ProductModel,
        lifecycleOwner: LifecycleOwner,
        navigationForStagingCallback: () -> Unit,
        navigationForScanBarCodeCallback: () -> Unit
    ) {
        viewLifecycleOwner = lifecycleOwner
        navigateAfterStagingCallback = navigationForStagingCallback
        setUpProductIdEntryViewModel()
        setUpQuantityEntryViewModel()
        queryPickProductImage(product)
        productData = product
        navigateAfterBarcodeScanCallback = navigationForScanBarCodeCallback

        pickFromOtherLocationsAdapter.value = pickFromOtherLocationsAdapterWrapper.createAdapter(viewLifecycleOwner,
            onChangeQtyLabel = {
                updateLabelQty()
            }
        )
        addAnotherLocationButtonTapped() //Add first item to list
        initState()
        updateLabelQty()
    }

    private fun initState(){
        setPickFromOtherLocationState(PickFromOtherLocationsState.Default())
    }


    private fun queryPickProductImage(product: ProductModel?) = viewModelScope.launch {
        try {
            repository.queryPickingProductImage(
                product?.productId?:""
            )
                ?.also { response ->
                    if (response.errors.isNullOrEmpty()) {
                        response.data?.productImageUrl?.let { productImageUrl.value = it }
                    }
                }
        } catch (e: ApolloException) {
            Log.w("Image Load Error", "Image response error")
        }
    }

    fun setPickFromOtherLocationState (state: PickFromOtherLocationsState){
        topErrorMessageState.value = state
        bottomErrorMessageState.value = state
        completeButtonState.value = state
        pickFromOtherLocationsState.value = state
    }

    fun getPickFromOtherLocationState (): PickFromOtherLocationsState? {
        return pickFromOtherLocationsState.value
    }


    override fun afterTextChanged(id: Int, s: Editable) {
    }

    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        backOrderCheckBox.value = check
        validatePickFromOtherLocationsState()
    }

    private fun validatePickFromOtherLocationsState(){
        //Check if BackOrder is needed
        if(countAllQtyFields()<productData.quantity){
            if (backOrderCheckBox.value == false){
                setPickFromOtherLocationState(PickFromOtherLocationsState.Insufficient(
                    activityService.getString(R.string.split_qnty_insufficient))
                )
            }
        }
        //Validate each alternate Locations
        var allAlternateLocationsValid = true
        pickFromOtherLocationsAdapter.value?.currentList?.forEach{
            if (!it.validateAlternateLocation()){
                allAlternateLocationsValid = false
            }
        }
        if (allAlternateLocationsValid && (getPickFromOtherLocationState() is PickFromOtherLocationsState.Default)){
            setPickFromOtherLocationState(PickFromOtherLocationsState.ReadyToPick())
        }

    }

    fun completePickButtonTapped() {
        validatePickFromOtherLocationsState()
        if(getPickFromOtherLocationState() is PickFromOtherLocationsState.ReadyToPick){
            if (productData.isSerial){
                onItemClickedIsSerial(productData, getAllAlternateLocationsSerialNumbers())
            }else{
                submitPickItem(productData)
            }
        }
    }

    private fun getAllAlternateLocationsSerialNumbers(): List<SerialLineInput> {
        val listInput = mutableListOf<SerialLineInput>()
        if (productData.defaultLocationSerialLineInput.isNotEmpty()){
            listInput.addAll(productData.defaultLocationSerialLineInput)
        }
        pickFromOtherLocationsAdapter.value?.currentList?.forEach{ thisLocation ->
            thisLocation.getAllSerialNumbers().forEach{ thisSerialNumber ->
                listInput.add(thisSerialNumber)
            }
        }

        var normalizedListInput = mutableListOf<SerialLineInput>()

        var line = 1
        listInput.forEach { thisSerial ->
            normalizedListInput.add(SerialLineInput(line, thisSerial.serial))
            line++
        }

        return normalizedListInput
    }

    fun addAnotherLocationButtonTapped(){

        if((pickFromOtherLocationsAdapter.value?.currentList?.size ?: 0) < 4){
            if ((productData.qtyPicked?.toInt()?:0) <= productData.quantity) {
                viewHolderViewModel = PickFromOtherLocationsViewHolderViewModel(
                    activityService,
                    productData,
                    viewLifecycleOwner,
                    repository,
                    onRemoveItem = {
                        removeItem(it)
                    },
                    onChangeQtyLabel = {
                        updateLabelQty()
                    },
                    onBarcodeScanClick
                )
                pickFromOtherLocationsAdapter.value?.addItem(viewHolderViewModel)
            }

        }else{
            activityService.showMessage(
                SnackBarState(
                    SnackBarType.ERROR,
                    activityService.getString(R.string.snackbar_error_locations_exeeded)
                ),
                Snackbar.LENGTH_LONG
            )
        }


    }

    fun saveProductOnUserPreferences(){ //Split Quantity Workaround
        pickFromOtherLocationsAdapter.value?.currentList?.forEach{
            var newAlternateLocation=AlternateLocationsDTO(
                location = it.locationDataEntryViewModel.value?.entryString?.value.toString(),
                qty = it.quantityDataEntryViewModel.value?.entryString?.value.toString()
            )
            if (productData.isSerial){
                it.getAllSerialNumbers().forEach{
                    newAlternateLocation.serialNumbers.add(it.serial)
                }
            }
            productData.alternateLocationDTO.add(newAlternateLocation)

        }
        if (backOrderCheckBox.value == true){
            productData.isBackOrder = true
        }

        var splitQtyDTOLocalPreference : SplitQtyDTO = userPreferences.getSplitQty()
        splitQtyDTOLocalPreference.productsList.add(productData)
        userPreferences.saveSplitQty(splitQtyDTOLocalPreference)
        postAlertAndNavigateBack()
    }

    private fun setUpProductIdEntryViewModel() {
        locationDataEntryViewModel.value?.setHintString(activityService.getString(R.string.product_location_label_string))
        locationDataEntryViewModel.value?.setInputCanBeCleared(false)
        locationDataEntryViewModel.value?.setDataEntryFieldType(DataEntryFieldType.DEFAULT)
    }
    private fun setUpQuantityEntryViewModel() {
        quantityDataEntryViewModel.value?.setHintString(activityService.getString(R.string.product_quantity_label_string))
        quantityDataEntryViewModel.value?.setInputCanBeCleared(true)
        quantityDataEntryViewModel.value?.setDataEntryFieldType(DataEntryFieldType.NUMBER_ENTRY)
    }

    fun removeItem(i: Int) {
        pickFromOtherLocationsAdapter.value?.removeItem(i)
        updateLabelQty()
    }

    private fun updateLabelQty(){
        _qtyLabel.value = activityService.getString(R.string.label_qty_picked_label_and_value,
            countAllQtyFields().toString(),
            productData.quantity.toString())

    }

    fun setSerialHolderPos(yPos: Int) {
        serialNumbersHolderYPos = yPos
    }

    fun countAllQtyFields(): Int {
        var counter :Int = productData.qtyPicked?.toInt()?:0
        pickFromOtherLocationsAdapter.value?.currentList?.forEach{
            val currentQty = it.quantityDataEntryViewModel.value?.entryString?.value
            if (!currentQty.isNullOrEmpty()){
                counter+=currentQty.toInt()
            }
        }
        if(counter>productData.quantity){
            setPickFromOtherLocationState(PickFromOtherLocationsState.Excess(activityService.getString(R.string.exceed_quantity)))
        }else{
            setPickFromOtherLocationState(PickFromOtherLocationsState.Default())
        }
        return counter
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
    override fun onInputComponentFocus(component: ViewGroup) {
        GlobalScope.launch {
            delay(500)
            val inputY = component.y
            val inputHeight = component.height
            val offset = serialNumbersHolderYPos + inputY - (inputHeight / 2)
        }
    }
    override fun onEditorAction(textView: TextView, actionId: Int, keyEvent: KeyEvent?): Boolean {
        if ((keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) ||
            actionId == EditorInfo.IME_ACTION_NEXT ||
            actionId == EditorInfo.IME_ACTION_DONE
        ) {
            textView.nextFocusDownId = textView.id

        }
        return true
    }


    fun submitPickItem(
        product: ProductModel
    ) = viewModelScope.launch {
        try {
            _showProgressBar.value = 1
            // set start time and create unique tote label
            product.setStartPickTime(Instant.now().toString())
            product.setTote()

            repository.mutationCompletePick(product.getProductDTO())?.also { response ->
                if (response.errors.isNullOrEmpty()) {
                    //save back order
                    saveProductOnUserPreferences()
                } else {
                    _showProgressBar.value = 0
                    setErrorLog(userPreferences, response.errors.toString())

                    when{
                        response.errors?.first()?.message.toString().contains("400") ->{
                            activityService.errorMessage(activityService.getString(R.string.error_400))
                            _showProgressBar.value = 0
                        }
                        else -> {activityService.errorMessage(
                            response.errors?.first()?.message
                                ?: activityService.getString(R.string.unknown))
                            _showProgressBar.value = 0
                        }
                    }
                }
            }
        } catch (e: ApolloException) {
            activityService.errorMessage(activityService.getString(R.string.error_submit_pick));
            _showProgressBar.value = 0

        }
    }

    private fun postAlertAndNavigateBack() {
        activityService.showMessage(
            SnackBarState(
                SnackBarType.SUCCESS,
                activityService.getString(R.string.snackbar_success_split_qty)
            ),
            Snackbar.LENGTH_SHORT
        )
        navigateAfterStagingCallback()
    }

    private val onBarcodeScanClick: ((
        userPick: ProductModel,
        successCallback: (barcode: String) -> Unit,
        failureCallback: () -> Unit
    ) -> Unit) = { userPick, successCallback, failureCallback ->
        this@PickFromOtherLocationsViewModel.successCallback = successCallback
        navigateAfterBarcodeScanCallback()
    }
    fun setScannedBarcode(barcode: String) {
        this@PickFromOtherLocationsViewModel.successCallback?.invoke(barcode)
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

    private fun getBranchId(): String {
        return userPreferences.getBranch() ?: emptyString
    }

}