package com.reece.pickingapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.reece.pickingapp.PickingOrdersQuery
import com.reece.pickingapp.R
import com.reece.pickingapp.models.EclipseCredentialModel
import com.reece.pickingapp.models.OrderDTO
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.repository.EclipseLoginRepository
import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.utils.AESEncyption
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.SnackBarType
import com.reece.pickingapp.utils.StringConverter
import com.reece.pickingapp.utils.emptyString
import com.reece.pickingapp.utils.extensions.setErrorLog
import com.reece.pickingapp.utils.extensions.toOrderDTO
import com.reece.pickingapp.view.adapter.OrdersAdapter
import com.reece.pickingapp.view.state.LoaderState
import com.reece.pickingapp.view.state.ResponseState
import com.reece.pickingapp.view.state.SnackBarState
import com.reece.pickingapp.wrappers.MaterialAlertDialogBuilderWrapper
import com.reece.pickingapp.wrappers.OrdersAdapterWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class OrdersListFragmentViewModel @Inject constructor(
    private val repository: PickingRepository,
    private val pickingApi: PickingApi,
    private val userPreferences: UserPreferences,
    private val activityService: ActivityService,
    private val stringConverter: StringConverter = StringConverter,
    private val ordersAdapterWrapper: OrdersAdapterWrapper = OrdersAdapterWrapper(),
    private val materialAlertDialogBuilderWrapper: MaterialAlertDialogBuilderWrapper = MaterialAlertDialogBuilderWrapper(),
    private val roomRepository: EclipseLoginRepository
) : ViewModel() {

    //region LiveData Vars

    val ordersResponseState = MutableLiveData<ResponseState<Response<PickingOrdersQuery.Data>>>()
    val loaderState = MutableLiveData<LoaderState>()
    val ordersAdapter = MutableLiveData<OrdersAdapter>()
    val noDataState = MutableLiveData<Boolean>()
    //endregion

    val TAG = "OrdersListFragmentViewModel"

    lateinit var navigateToAuthScreenCallback: () -> Unit
    lateinit var navigateToOrderCallback: (OrderDTO) -> Unit
    var orderViewHolderViewModels = emptyList<OrderViewHolderViewModel>()
     var credentialsNumber: Int = 0

    fun setUp(
        navigationForAuthScreenCallback: () -> Unit,
        navigationForOrderCallback: (OrderDTO) -> Unit
    ) {
        viewModelScope.launch {
            val credentialsInRepository = async(Dispatchers.IO) {
                credentialsNumber = roomRepository.getCountCredentials()
            }
            credentialsInRepository.await()
            if (!pickingApi.isOktaAuthenticated()) {
                if(credentialsNumber!=null && credentialsNumber > 0){
                    if(userPreferences.getUsername().isNullOrEmpty()){
                        navigateToAuthScreenCallback()
                    }else{
                        activityService.showMessage(
                            SnackBarState(
                                SnackBarType.ERROR,
                                activityService.getString(R.string.authorization_expired)
                            ),
                            Snackbar.LENGTH_LONG
                        )
                        activityService.activity?.let { pickingApi.signOut(it) }
                    }
                }else{
                    navigateToAuthScreenCallback()
                }
            } else {
                //get current email
                val currentEmail =  pickingApi.getOktaUserEmail()
                //get eclipse credentials
                val currentEclipseCredential = roomRepository.getCredentialByEmail(currentEmail)
                //eclipse validation
                if(currentEclipseCredential != null){
                   linkAccount(currentEclipseCredential)
                }else{
                    if (pickingApi.isAuthenticated()){
                        queryOrdersList()
                    }else{
                        navigateToAuthScreenWithErrorMsg(activityService.getString(R.string.authorization_not_completed))
                    }
                }
            }
        }

        navigateToAuthScreenCallback = navigationForAuthScreenCallback
        navigateToOrderCallback = navigationForOrderCallback
        ordersAdapter.value = ordersAdapterWrapper.createAdapter()


    }

    // region Navigation

    private fun navigateToAuthScreenWithErrorMsg(message: String?) {
        navigateToAuthScreenCallback()
        activityService.errorMessage(message = message ?: "")
    }

    private fun navigateToOrder(order: OrderDTO) {
        navigateToOrderCallback(order)
    }

    //endregion
    //region Handlers

    val onItemClicked: ((orderViewModel: OrderViewModel) -> Unit) = { orderViewModel ->
        if (orderViewModel.assignedUserId == getUsername()) {
            navigateToOrder(orderViewModel.getOrderDTO())
        } else {
            showDialogConfirmation(orderViewModel)
        }
    }

    //endregion
    //region LiveData Updates

    private fun setLoaderState(state: LoaderState) {
        loaderState.value = state
    }

    private fun setOrdersResponseState(state: ResponseState<Response<PickingOrdersQuery.Data>>) {
        ordersResponseState.value = state

        when (state) {
            is ResponseState.Success -> {
                state.response?.data?.pickingOrders?.let { orders ->
                    generateViewHolder(orders)
                    if (orders.isNotEmpty()) {
                        noDataState.value = false
                        ordersAdapter.value?.submitList(orderViewHolderViewModels)
                    } else {
                        noDataState.value = true
                        ordersAdapter.value?.submitList(emptyList())
                    }
                }
            }
            is ResponseState.Error -> {
                noDataState.value = true
                activityService.errorMessage(state.message)
            }
            else -> {}
        }
    }

    //endregion
    //region Queries and Mutations

    fun queryOrdersList() = viewModelScope.launch {
        setLoaderState(LoaderState.Loading)
        setOrdersResponseState(ResponseState.Default())

        try {
            repository.queryPickingTasks(
                getBranchId(),
                getUsername()
            )?.also { response ->
                if (response.errors.isNullOrEmpty()) {
                    setLoaderState(LoaderState.Default)
                    setOrdersResponseState(ResponseState.Success(response))
                } else {
                    setLoaderState(LoaderState.Default)
                    setOrdersResponseState(
                        ResponseState.Error(
                            message = response.errors?.first()?.message
                        )
                    )
                }
            }
        } catch (e: ApolloException) {
            Log.e(TAG,e.message.toString() )
            setOrdersResponseState(
                ResponseState.Error(
                    message = activityService.getString(R.string.error_fetch_order)
                )
            )
            setLoaderState(LoaderState.Default)
        }
    }

    fun assignPickTask(order: OrderViewModel) = viewModelScope.launch {
        setLoaderState(LoaderState.Loading)
        setOrdersResponseState(ResponseState.Default())

        try {
            repository.mutationAssignPickTask(order.getOrderDTO().toPickingTaskInput(getUsername()))
                ?.also { response ->
                    val orderDTO = response.data?.assignPickTask?.toOrderDTO()
                    if (response.errors.isNullOrEmpty() && orderDTO != null) {
                        navigateToOrder(orderDTO)
                    } else {
                        var errorMessage = ""
                        if (response.errors?.first()?.message?.contains("Cannot return null for non-nullable field Mutation.assignPickTask.") == true) {
                            errorMessage =
                                activityService.getString(R.string.order_lock_message) ?: ""
                        }
                        setOrdersResponseState(
                            ResponseState.Error(
                                message = errorMessage
                            )
                        )
                        queryOrdersList()
                    }
                }
        } catch (e: ApolloException) {
            setOrdersResponseState(
                ResponseState.Error(
                    message = activityService.getString(R.string.error_basic)
                )
            )
        }
    }

    //endregion

    //region Util
    private fun getUsername(): String {
        return userPreferences.getUsername() ?: emptyString
    }

    private fun getBranchId(): String {
        return userPreferences.getBranch() ?: emptyString
    }

    private fun generateViewHolder(
        orders: List<PickingOrdersQuery.PickingOrder?>?
    ) {
        val list = mutableListOf<OrderViewHolderViewModel>()
        orders?.let { orderList ->
            for (order in orderList) {
                order?.let {
                    val viewHolderViewModel = OrderViewHolderViewModel(
                        activityService,
                        stringConverter,
                        OrderViewModel(it.toOrderDTO()),
                        onItemClicked,
                        repository
                    )
                    list.add(viewHolderViewModel)
                }
            }
        }

        orderViewHolderViewModels = list
    }

    /**
     * Sets adapter with List of OrderViewHolderViewModel
     * Order of filter preference: 1: Customer (shipToName) / 2: orderId / 3: shipVia
     */
    fun filterResults(strSearch: String) {
        val filterList = orderViewHolderViewModels.filter {
            it.orderViewModel?.shipToName?.contains(strSearch, ignoreCase = true) ?: false
                    ||
                    it.orderViewModel?.orderId?.contains(strSearch, ignoreCase = true) ?: false
                    ||
                    it.orderViewModel?.shipVia?.contains(strSearch, ignoreCase = true) ?: false

        }
        ordersAdapter.value?.submitList(filterList) {
                    ordersAdapter.value?.recyclerViewScrollToTop()
                }
    }

    private fun hasFilterTerm(orderItem: String?, term: String): Boolean {
        return orderItem?.contains(term, ignoreCase = true) ?: false
    }

    private fun showDialogConfirmation(order: OrderViewModel) {
        activityService.activity?.let {
            materialAlertDialogBuilderWrapper.create(it, R.style.MorscoConfirmationStyle)
                .setTitle(activityService.getString(R.string.confirm_order_message))
                .setMessage(
                    activityService.getString(
                        R.string.order_dialog_info,
                        order.shipToName,
                        order.orderId
                    )?.let { it1 -> stringConverter.convertToHtmlSpan(it1) }
                )
                .setNegativeButton(activityService.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(activityService.getString(R.string.ready_to_start)) { dialog, _ ->
                    assignPickTask(order)
                    dialog.dismiss()
                }
                .show()
        }
    }
    //endregion
    //eclipselogin
    private fun linkAccount(credential: EclipseCredentialModel) = viewModelScope.launch {
        setLoaderState(LoaderState.Loading)
        //get the user name and password
        val eclipsePass =  AESEncyption.decrypt(credential.eclipsePass)
        eclipsePass?.let {
                try {
                    repository.mutationVerifyEclipseCredentials(credential.username, eclipsePass)
                        ?.also { response ->
                            if (response.data?.verifyEclipseCredentials?.success == true) {
                                //the login is ok
                                queryOrdersList()
                            } else {
                                setErrorLog(userPreferences, (response.errors ?: "") as String)
                                //return to eclipse login
                                userPreferences.setUsername("")
                                credential.eclipsePass = ""
                                credential.username = ""
                                roomRepository.updateCredential(credential)
                                navigateToAuthScreenWithErrorMsg(activityService.getString(R.string.authorization_not_completed))
                            }
                        }
                } catch (e: ApolloException) {
                    Firebase.crashlytics.recordException(e)
                    setErrorLog(userPreferences, (e.message ?: "") as String)
                    navigateToAuthScreenWithErrorMsg(activityService.getString(R.string.authorization_not_completed))
                    activityService.activity?.let { pickingApi.signOut(it) }
                }
            }
    }
    //endregion eclipselogin
}
