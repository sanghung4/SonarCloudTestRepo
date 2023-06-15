package com.reece.pickingapp.viewmodel

import android.text.Editable
import android.text.Spanned
import android.util.Log
import android.view.KeyEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.common.Barcode
import com.reece.pickingapp.*
import com.reece.pickingapp.interfaces.BarCodeScanInterface
import com.reece.pickingapp.interfaces.DataEntryInterface
import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.type.*
import com.reece.pickingapp.utils.*
import com.reece.pickingapp.utils.extensions.setErrorLog
import com.reece.pickingapp.utils.extensions.toCloseTaskInput
import com.reece.pickingapp.utils.extensions.toProductDTO
import com.reece.pickingapp.utils.extensions.toStagePickTotePackageInput
import com.reece.pickingapp.view.adapter.ProductsAdapter
import com.reece.pickingapp.view.state.*
import com.reece.pickingapp.wrappers.ProductsAdapterWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject
@ExperimentalCoroutinesApi
@HiltViewModel
class ProductsFragmentViewModel @Inject constructor(
    private val repository: PickingRepository,
    private val userPreferences: UserPreferences,
    private val activityService: ActivityService,
    private val stringConverter: StringConverter = StringConverter,
    private val productsAdapterWrapper: ProductsAdapterWrapper = ProductsAdapterWrapper()
) : ViewModel(), DataEntryInterface, BarCodeScanInterface {

    //region LiveData Vars

    val productsResponseState = MutableLiveData<ResponseState<Response<PickTasksQuery.Data>>>()
    val stageOrderResponseState =
        MutableLiveData<ResponseState<Response<StagePickTasksMutation.Data>>>()
    val activeResponseState = MutableLiveData<ResponseState<*>>()
    val loaderState = MutableLiveData<LoaderState>()
    val productsAdapter = MutableLiveData<ProductsAdapter>()
    val selectedOrder = MutableLiveData<OrderViewModel>()
    val stagingState = MutableLiveData<StagingOrderState>()
    val locationFormVisible = MutableLiveData(GONE)
    val locationDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    val boxesDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    val skidsDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    val bundlesDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    val piecesDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    var successCallback: ((barcode: String) -> Unit)? = null
    val orderProgressData = MutableLiveData<Int>()
    var shippingDetails = MutableLiveData("")
    var shippingDetailsVisibility = MutableLiveData(false)
    var showMoreOrLessVisibility = MutableLiveData(false)
    var shippingDetailsMaxLines = MutableLiveData(1)
    var showMoreOrLessLabel = MutableLiveData("")
    //endregion

    var showQueryPickTaskLoading = true
    lateinit var viewLifecycleOwner: LifecycleOwner
    lateinit var navigateAfterStagingCallback: () -> Unit
    lateinit var navigateAfterBarcodeScanCallback: () -> Unit
    private val packageList = mutableListOf<PackageInput>()
    private lateinit var navigateForReportFragment: () -> Unit
    lateinit var dialogQty: (product:ProductModel,userPick: Int) -> Unit

    fun setUp(
        selectedOrder: OrderViewModel,
        lifecycleOwner: LifecycleOwner,
        navigationForStagingCallback: () -> Unit,
        navigationForScanBarCodeCallback: () -> Unit,
        showDialogSplitQty: (product:ProductModel,userPick: Int) -> Unit,
        navigateForReportFragment: () -> Unit

    ) {
        viewLifecycleOwner = lifecycleOwner
        navigateAfterStagingCallback = navigationForStagingCallback
        navigateAfterBarcodeScanCallback = navigationForScanBarCodeCallback
        this.navigateForReportFragment = navigateForReportFragment
        setSelectedOrder(selectedOrder)
        queryPickTasks()
        setProductsResponseState(ResponseState.Default())
        productsAdapter.value = productsAdapterWrapper.createAdapter(viewLifecycleOwner)
        setStagingOrderState(StagingOrderState.Default)
        setDataEntryInputState(InputState.Default())
        setDataEntryHints()
        setDataEntryFieldType()
        queryShippingDetails()
        showMoreOrLessLabel.value = activityService.getString(R.string.show_more)
        dialogQty = showDialogSplitQty
    }

    //region LiveData Updates

    private fun setLocationDataEntryInputState(state: InputState) {
        locationDataEntryViewModel.value?.setInputState(state)
    }

    private fun setDataEntryHints() {
        locationDataEntryViewModel.value?.setHintString(activityService.getString(R.string.staging_location_hint))
        boxesDataEntryViewModel.value?.setHintString(activityService.getString(R.string.boxes))
        skidsDataEntryViewModel.value?.setHintString(activityService.getString(R.string.skids))
        bundlesDataEntryViewModel.value?.setHintString(activityService.getString(R.string.bundles))
        piecesDataEntryViewModel.value?.setHintString(activityService.getString(R.string.pieces))
    }

    private fun setDataEntryFieldType() {
        boxesDataEntryViewModel.value?.setDataEntryFieldType(DataEntryFieldType.NUMBER_ENTRY)
        skidsDataEntryViewModel.value?.setDataEntryFieldType(DataEntryFieldType.NUMBER_ENTRY)
        bundlesDataEntryViewModel.value?.setDataEntryFieldType(DataEntryFieldType.NUMBER_ENTRY)
        piecesDataEntryViewModel.value?.setDataEntryFieldType(DataEntryFieldType.NUMBER_ENTRY)
    }

    private fun setDataEntryInputState(state: InputState) {
        locationDataEntryViewModel.value?.setInputState(state)
        boxesDataEntryViewModel.value?.setInputState(state)
        skidsDataEntryViewModel.value?.setInputState(state)
        bundlesDataEntryViewModel.value?.setInputState(state)
        piecesDataEntryViewModel.value?.setInputState(state)
    }

    private fun setStagingOrderState(state: StagingOrderState) {
        stagingState.value = state
    }

    private fun setSelectedOrder(order: OrderViewModel) {
        selectedOrder.value = order
    }

    private fun setLoaderState(state: LoaderState) {
        loaderState.value = state
    }

    private fun setProductsResponseState(state: ResponseState<Response<PickTasksQuery.Data>>) {
        productsResponseState.value = state
        activeResponseState.value = productsResponseState.value
        when (state) {
            is ResponseState.Success -> {
                state.response?.data?.userPicks?.let { userPicks ->
                    if (userPicks.isNotEmpty()) {
                        val viewModels = generateViewHolderViewModels(
                            userPicks,
                            viewLifecycleOwner,
                            onItemClicked,
                            onItemClickedIsSerial,
                            onSplitQuantity,
                            onMoveCursor,
                            onBarcodeScanClick,
                            dialogQty
                        )
                        productsAdapter.value?.submitList(viewModels)
                        setOrderIndicatorProgress(userPicks?.size)
                    } else {
                        productsAdapter.value?.submitList(emptyList())
                        setFormVisibility()
                        setToolBarTitle(activityService.getString(R.string.stage_order))
                        focusLocation()
                        setDataEntryInputState(InputState.Default())
                    }
                }
            }
            is ResponseState.Error -> {
                setErrorLog(userPreferences, state.message.toString())

                activityService.errorMessage(
                    activityService.getString(R.string.product_list_error)
                )
            }

            else -> {}
        }
    }

    private fun setFormVisibility() {
        locationFormVisible.value = VISIBLE
        setOrderIndicatorProgress(0)
    }

    private fun setToolBarTitle(title: String?) {
        activityService.setToolbarTitle(title)
    }

    private fun moveCursorToNext(adapterPosition: Int) {
        val size = productsAdapter.value?.currentList?.size ?: 0
        if (size > 0 && adapterPosition <= size - 1) {
            var nextPosition = adapterPosition + 1
            if (nextPosition == size) {
                nextPosition = adapterPosition
            }
            val productViewHolderViewModel = productsAdapter.value?.currentList?.get(nextPosition)
            productViewHolderViewModel?.setProductIdFocusState(InputFocusState.Focussed)
        }
    }

    private fun setStageOrderResponseState(state: ResponseState<Response<StagePickTasksMutation.Data>>) {
        stageOrderResponseState.value = state
        activeResponseState.value = stageOrderResponseState.value
    }

    //endregion

    //region Queries and Mutations

    fun queryPickTasks() = viewModelScope.launch {
        selectedOrder.value?.let { order ->
            if (showQueryPickTaskLoading) {
                setLoaderState(LoaderState.Loading)
                setProductsResponseState(ResponseState.Default())
            }

            try {
                repository.queryUserTasks(
                    order.branchId.orEmpty(),
                    order.assignedUserId.orEmpty(),
                    order.orderId.orEmpty()
                )?.also { response ->
                    if (response.errors.isNullOrEmpty()) {
                        setLoaderState(LoaderState.Default)
                        setProductsResponseState(ResponseState.Success(response))
                    } else {
                        setProductsResponseState(
                            ResponseState.Error(
                                message = activityService.getString(
                                    R.string.error_fetch_product
                                )
                            )
                        )
                        setLoaderState(LoaderState.Default)
                    }
                }
            } catch (e: ApolloException) {
                setProductsResponseState(
                    ResponseState.Error(
                        message = activityService.getString(
                            R.string.error_fetch_product
                        )
                    )
                )
                setLoaderState(LoaderState.Default)
            }
        }
    }

    fun updateProductSerialNumbers(
        updateProductSerialNumbersInput: UpdateProductSerialNumbersInput,
        product: ProductModel,
        failureCallback: () -> Unit
    ) = viewModelScope.launch {
        try {
            repository.mutationUpdateProductSerialNumbers(updateProductSerialNumbersInput)
                ?.also { response ->
                    if (response.errors.isNullOrEmpty()) {
                        submitPickItem(product, failureCallback)
                    } else {
                        failureCallback()
                        activityService.errorMessage(
                            activityService.getString(R.string.error_serial_unique)
                        )
                    }
                }
        } catch (e: ApolloException) {
            when {
                e.message.toString()
                    .contains("400") -> activityService.errorMessage(activityService.getString(R.string.error_400))
                else -> activityService.errorMessage(activityService.getString(R.string.error_submit_serial))
            }
        }
    }

    fun submitPickItem(
        product: ProductModel, failureCallback: () -> Unit
    ) = viewModelScope.launch {
        try {
            // set start time and create unique tote label
            product.setStartPickTime(Instant.now().toString())
            product.setTote()

            repository.mutationCompletePick(product.getProductDTO())?.also { response ->
                if (response.errors.isNullOrEmpty()) {
                    productsAdapter.value?.removeItem(product)
                    if (productsAdapter.value?.currentList?.size ?: 0 <= 1) {
                        productsAdapter.value?.submitList(emptyList())
                        setFormVisibility()
                        setToolBarTitle(activityService.getString(R.string.stage_order))
                        focusLocation()
                        setDataEntryInputState(InputState.Default())
                    }
                    setOrderIndicatorProgress((productsAdapter.value?.currentList?.size ?: 0) - 1)
                } else {
                    setErrorLog(userPreferences, response.errors.toString())
                    failureCallback()
                    when {
                        response.errors?.first()?.message.toString()
                            .contains("400") -> activityService.errorMessage(
                            activityService.getString(
                                R.string.error_400
                            )
                        )
                        else -> activityService.errorMessage(
                            response.errors?.first()?.message
                                ?: activityService.getString(R.string.unknown)
                        )
                    }
                }
            }
        } catch (e: ApolloException) {
            activityService.errorMessage(activityService.getString(R.string.error_submit_pick));
        }
    }

    fun onCompleteStagingPickingClick() = viewModelScope.launch {
        selectedOrder.value?.let { order ->
            setLoaderState(LoaderState.Loading)
            setDataEntryInputState(InputState.Disabled())
            setStagingOrderState(StagingOrderState.Default)
            try {
                val branchId = getBranchId()
                val input = StagePickTaskInput(
                    orderId = order.orderId.orEmpty(),
                    invoiceId = order.invoiceId.orEmpty(),
                    branchId = branchId,
                    tote = order.getToteName(),
                    location = getLocationValue().toInput()
                )

                //Call API's in parallel
                val stagePickingTasksCall = async { stagePickingTasks(input) }
                val stagePickTotePackagesCall = async { stagePickTotePackages(input) }

                val stagePickingTasksResponse = stagePickingTasksCall.await()
                val stagePickTotePackagesResponse = stagePickTotePackagesCall.await()

                val closeTaskCall = async { closeTask(input) }
                val closeTaskResponse = closeTaskCall.await()

                processResponseData(
                    stagePickTotePackagesResponse, closeTaskResponse, stagePickingTasksResponse
                )

            } catch (e: ApolloException) {
                activityService.errorMessage(activityService.getString(R.string.error_picking_task))
                setLoaderState(LoaderState.Default)
                setDataEntryInputState(InputState.Default())
            }
        }
    }

    fun splitQuantityForProduct(
        product: ProductModel,
        updateProductSerialNumbersInput: UpdateProductSerialNumbersInput,
        quantityPicked: Int,
        failureCallback: () -> Unit
    ) = viewModelScope.launch {
        try {
            // set start time and create unique tote label
            product.setStartPickTime(Instant.now().toString())
            product.setTote()

            val completePickInput = product.getProductDTO().toCompletePickInput()
            val serialNumberList = updateProductSerialNumbersInput.serialNumberList.toInput()
            val splitQuantityInput = SplitQuantityInput(
                completePickInput, Input.absent(), quantityPicked
            );

            repository.mutationSplitQuantity(splitQuantityInput)?.also { response ->
                    if (response.errors.isNullOrEmpty()) {
                        val message = if (response?.data?.splitQuantity?.isSplit == true) {
                            activityService.getString(R.string.split_qnty_success_alternate_location_added_message)
                        } else {
                            activityService.getString(R.string.split_qnty_success_back_ordered_message)
                        }
                        activityService.showMessage(
                            SnackBarState(type = SnackBarType.MESSAGE, message = message),
                            Snackbar.LENGTH_LONG
                        )
                        showQueryPickTaskLoading = false
                        queryPickTasks()
                    } else {
                        setErrorLog(userPreferences, response.errors.toString())
                        failureCallback()
                        val message = response.errors?.first()?.message
                        activityService.showMessage(
                            SnackBarState(type = SnackBarType.MESSAGE, message = message)
                        )
                        queryPickTasks()
                    }
                }
        } catch (e: ApolloException) {
            activityService.errorMessage(
                activityService.getString(R.string.error_submit_pick)
            )
        }
    }

    fun queryShippingDetails() = viewModelScope.launch {
        try {
            repository.queryShippingDetails(
                ShippingDetailsKourierInput(selectedOrder.value?.getOrderIdValue() ?: "")
            )?.also { response ->
                shippingDetails.value = ""
                if (response.errors.isNullOrEmpty()) {
                    response.data?.shippingDetails?.shippingtext?.let {
                        it.forEach {
                            it.shippingInstructions?.forEach { shippingDetail ->
                                if (shippingDetails.value.isNullOrEmpty()) {
                                    shippingDetails.value = shippingDetail
                                } else {
                                    shippingDetails.value =
                                        "${shippingDetails.value}\n$shippingDetail"
                                }
                            }
                        }
                    }
                    if ((response.data?.shippingDetails?.shippingtext?.get(0)?.shippingInstructions?.size
                            ?: 0) > 1
                    ) {
                        showMoreOrLessVisibility.value = true
                        showMoreOrLessLabel.value = activityService.getString(R.string.show_more)
                    }
                    shippingDetailsVisibility.value = (shippingDetails.value?.length ?: 0) > 0

                }
            }
        } catch (e: ApolloException) {
            Log.w("Image Load Error", "Image response error")
        }
    }

    private fun processResponseData(
        stagePickTotePackagesResponse: Response<StagePickTotePackagesMutation.Data>?,
        closeTaskResponse: Response<CloseTaskMutation.Data>?,
        stagePickingTasksResponse: Response<StagePickTasksMutation.Data>?
    ) {
        if (stagePickTotePackagesResponse?.errors.isNullOrEmpty() && stagePickTotePackagesResponse?.errors.isNullOrEmpty() && closeTaskResponse?.errors.isNullOrEmpty()) {
            //validate shared preferences to check if back order needed
            var backOrderList = userPreferences.getSplitQty()
            if (!backOrderList.productsList.isNullOrEmpty()) {
                activityService.showMessage(
                    SnackBarState(
                        SnackBarType.SUCCESS,
                        activityService.getString(R.string.order_stage_success)
                    ), Snackbar.LENGTH_SHORT
                )
                //navigate to report back order
                navigateForReportFragment()
            } else {
                postAlertAndNavigateBack()
            }
            //TODO: Uncomment when closeTask is being replaced by closeOrder
//            processErrorCodeForCloseOrder(
//                closeTaskResponse?.data?.closeOrder?.errorCode,
//                closeTaskResponse?.data?.closeOrder?.errorMessage
//            )

            //TODO: Check flow from if its from other location then navigate to report screen
            //activityService.navController?.navigate(R.id.action_product_list_fragment_to_ReportFragment)

        } else {
            var errorMessage = when {
                !stagePickTotePackagesResponse?.errors.isNullOrEmpty() -> {
                    stagePickTotePackagesResponse?.errors?.first()?.message
                }
                !stagePickingTasksResponse?.errors.isNullOrEmpty() -> {
                    stagePickingTasksResponse?.errors?.first()?.message
                }
                else -> {
                    closeTaskResponse?.errors?.first()?.message
                }
            }
            setErrorLog(userPreferences, stagePickTotePackagesResponse?.errors.toString())
            if (!errorMessage.isNullOrEmpty() && errorMessage.contains("400")) {
                errorMessage = activityService.getString(R.string.error_400)
            }

            setStageOrderResponseState(ResponseState.Error(errorMessage))
            activityService.errorMessage(
                errorMessage ?: activityService.getString(R.string.unknown)
            )
            setLoaderState(LoaderState.Default)
            setDataEntryInputState(InputState.Default())
        }
    }

    private suspend fun stagePickingTasks(input: StagePickTaskInput) =
        repository.mutationStagePickTasks(input)


    private suspend fun stagePickTotePackages(input: StagePickTaskInput): Response<StagePickTotePackagesMutation.Data>? {
        if (!boxesDataEntryViewModel.value?.getInputString()
                .equals(emptyString) && !boxesDataEntryViewModel.value?.getInputString()
                .equals(ZERO)
        ) {
            boxesDataEntryViewModel.value?.getInputString()
                ?.let { PackageInput(PackageType.BOX.toString(), it.toInt()) }
                ?.let { packageList.add(it) }
        }

        if (!skidsDataEntryViewModel.value?.getInputString()
                .equals(emptyString) && !skidsDataEntryViewModel.value?.getInputString()
                .equals(ZERO)
        ) {
            skidsDataEntryViewModel.value?.getInputString()
                ?.let { PackageInput(PackageType.SKID.toString(), it.toInt()) }
                ?.let { packageList.add(it) }
        }

        if (!bundlesDataEntryViewModel.value?.getInputString()
                .equals(emptyString) && !bundlesDataEntryViewModel.value?.getInputString()
                .equals(ZERO)
        ) {
            bundlesDataEntryViewModel.value?.getInputString()
                ?.let { PackageInput(PackageType.BUND.toString(), it.toInt()) }
                ?.let { packageList.add(it) }
        }

        if (!piecesDataEntryViewModel.value?.getInputString()
                .equals(emptyString) && !piecesDataEntryViewModel.value?.getInputString()
                .equals(ZERO)
        ) {
            piecesDataEntryViewModel.value?.getInputString()
                ?.let { PackageInput(PackageType.PCS.toString(), it.toInt()) }
                ?.let { packageList.add(it) }
        }

        return repository.mutationPostPackageData(input.toStagePickTotePackageInput(packageList))
    }

    //TODO: When closeOrder is fixed replace closeTask with closeOrder
    //suspend fun closeOrder(input: CloseOrderInput) = repository.mutationCloseOrder(input)
    private suspend fun closeTask(input: StagePickTaskInput) =
        repository.mutationCloseTask(input.toCloseTaskInput())

    //endregion

    // region Handlers

    private val onItemClicked: ((
        userPick: ProductModel, failureCallback: () -> Unit
    ) -> Unit) = { userPick, failureCallback ->
        submitPickItem(
            userPick, failureCallback
        )
    }


    private val onMoveCursor: (adapterPosition: Int) -> Unit = { adapterPosition ->
        moveCursorToNext(adapterPosition)
    }

    private val onBarcodeScanClick: ((
        userPick: ProductModel, successCallback: (barcode: String) -> Unit, failureCallback: () -> Unit
    ) -> Unit) = { userPick, successCallback, failureCallback ->
        this@ProductsFragmentViewModel.successCallback = successCallback
        navigateAfterBarcodeScanCallback.invoke()
    }

    private val onItemClickedIsSerial: ((
        userPick: ProductModel, serialNumbers: List<SerialLineInput>, failureCallback: () -> Unit
    ) -> Unit) = { userPick, serialNumbers, failureCallback ->
        updateProductSerialNumbers(
            UpdateProductSerialNumbersInput(
                getBranchId(), userPick.warehouseID, serialNumbers, true
            ), userPick, failureCallback
        )
    }

    private val onSplitQuantity: ((
        userPick: ProductModel, serialNumbers: List<SerialLineInput>, quantityPicked: Int, failureCallback: () -> Unit
    ) -> Unit) = { userPick, serialNumbers, quantityPicked, failureCallback ->
        splitQuantityForProduct(
            userPick, UpdateProductSerialNumbersInput(
                getBranchId(), userPick.warehouseID, serialNumbers, true
            ), quantityPicked, failureCallback
        )
    }

    //TEMP
    private val adapterScroll: ((
        adapterPosition: Int, offset: Int
    ) -> Unit) = { adapterPosition, offset ->
        //TODO: For Future reference, We have kept this line
        //productsAdapter.value?.scrollTo(adapterPosition, offset)
    }

    //endregion

    //region Utility

    private fun getLocationValue(): String {
        return locationDataEntryViewModel.value?.getInputString().orEmpty()
    }

    private fun generateViewHolderViewModels(
        userPicks: List<PickTasksQuery.UserPick?>?,
        lifecycleOwner: LifecycleOwner,
        onItemClicked: ((userPick: ProductModel, failureCallback: () -> Unit) -> Unit),
        onItemClickedIsSerial: ((userPick: ProductModel, serialNumbers: List<SerialLineInput>, failureCallback: () -> Unit) -> Unit),
        onSplitQuantity: ((userPick: ProductModel, serialNumbers: List<SerialLineInput>, quantityPicked: Int, failureCallback: () -> Unit) -> Unit),
        onMoveCursor: (adapterPosition: Int) -> Unit,
        onBarcodeScanClick: (userPick: ProductModel, successCallback: (barcode: String) -> Unit, failureCallback: () -> Unit) -> Unit,
        showQtyDialog: (product:ProductModel,userPick: Int) -> Unit
    ): MutableList<ProductViewHolderViewModel> {
        val list = mutableListOf<ProductViewHolderViewModel>()
        userPicks?.let { picks ->
            for (pick in picks) {
                pick?.let {
                    val productModel = ProductModel(it.toProductDTO())
                    productModel.customerName = selectedOrder.value?.shipToName
                    val viewHolderViewModel = ProductViewHolderViewModel(
                        activityService,
                        productModel,
                        lifecycleOwner,
                        onItemClicked,
                        onItemClickedIsSerial,
                        onSplitQuantity,
                        adapterScroll,
                        onMoveCursor,
                        onBarcodeScanClick,
                        repository,
                        showQtyDialog
                    )
                    list.add(viewHolderViewModel)
                }
            }
        }

        return list
    }

    private fun postAlertAndNavigateBack() {
        activityService.showMessage(
            SnackBarState(
                SnackBarType.SUCCESS, activityService.getString(R.string.order_stage_success)
            ), Snackbar.LENGTH_SHORT
        )

        navigateAfterStagingCallback()
    }

    private fun getBranchId(): String {
        return userPreferences.getBranch() ?: emptyString
    }

    fun getOrderText(): Spanned? {
        selectedOrder.value?.getOrderText()?.let {
            activityService.getString(R.string.order_number_spannable, it)?.let {
                return stringConverter.convertToHtmlSpan(it)
            }
        }
        return null
    }

    fun getCustomerText(): Spanned? {
        selectedOrder.value?.shipToName?.let { customerName ->
            customerName?.let {
                return stringConverter.convertToHtmlSpan(it)
            }
        }

        return null
    }

    private fun fieldIsLastInput(hashCode: Int): Boolean {
        return hashCode == piecesDataEntryViewModel.value?.getTextInputId()
    }

    private fun focusLocation() {
        locationDataEntryViewModel.value?.setInputFocusState(InputFocusState.Focussed)
    }

    //endregion

    //region DataEntryInterface Methods

    override fun onEditorAction(textView: TextView, actionId: Int, keyEvent: KeyEvent?): Boolean {
        if (fieldIsLastInput(textView.hashCode())) {
            activityService.hideKeyboard()
        }

        return false
    }

    override fun afterTextChanged(id: Int, s: Editable) {
        if (id != 0 && id == locationDataEntryViewModel.value?.getTextInputId()) {
            if (getLocationValue().isNotEmpty()) {
                setStagingOrderState(StagingOrderState.StageReady)
                setLocationDataEntryInputState(InputState.Default())
            } else {
                setStagingOrderState(StagingOrderState.Default)
                setLocationDataEntryInputState(InputState.Error(activityService.getString(R.string.blank_field_error)))
            }
        }
    }

    override fun onBarcodeScanned(
        barcode: String, barcodes: List<Barcode>, id: Int, layoutId: String
    ) {
        super.onBarcodeScanned(barcode, barcodes, id, layoutId)

    }

    override fun onClickScan(id: Int, hint: String) {
        super.onClickScan(id, hint)
        navigateAfterBarcodeScanCallback.invoke()
    }

    fun setScannedBarcode(barcode: String) {
        this@ProductsFragmentViewModel.successCallback?.invoke(barcode)
    }

    fun getShipViaText(): Spanned? {
        selectedOrder.value?.shipVia?.let { shipVia ->
            shipVia?.let {
                return stringConverter.convertToHtmlSpan(it)
            }
        }

        return null
    }

    fun getPickCount(): Spanned? {
        selectedOrder.value?.order?.pickCount?.let { customerName ->
            customerName?.let {
                return stringConverter.convertToHtmlSpan(it)
            }
        }

        return null
    }

    fun setOrderIndicatorProgress(remainingCount: Int = 0) {
        val totalCount = selectedOrder.value?.order?.pickCount?.toIntOrNull() ?: 0
        val percentage = if (totalCount == remainingCount) 0
        else if (remainingCount == 0) 100
        else (remainingCount / totalCount.toDouble() * 100).toInt()
        orderProgressData.value =
            if (percentage == 0 || percentage == 100) percentage else 100 - percentage
    }

    //endregion
    fun showMoreOrLessButtonTapped() {
        if (shippingDetailsMaxLines.value == SHIPPING_INSTRUCTION_MIN_LINE) {
            shippingDetailsMaxLines.value = SHIPPING_INSTRUCTION_MAX_LINE
            showMoreOrLessLabel.value = activityService.getString(R.string.show_less)
        } else {
            shippingDetailsMaxLines.value = SHIPPING_INSTRUCTION_MIN_LINE
            showMoreOrLessLabel.value = activityService.getString(R.string.show_more)
        }
    }
}
