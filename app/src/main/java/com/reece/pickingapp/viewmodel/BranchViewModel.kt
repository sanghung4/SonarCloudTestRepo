package com.reece.pickingapp.viewmodel

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.textfield.TextInputLayout
import com.reece.pickingapp.PickingOrdersQuery
import com.reece.pickingapp.R
import com.reece.pickingapp.ValidateBranchQuery
import com.reece.pickingapp.interfaces.DataEntryInterface
import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.type.ValidateBranchInput
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.emptyString
import com.reece.pickingapp.view.state.InputState
import com.reece.pickingapp.view.state.LoaderState
import com.reece.pickingapp.view.state.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class BranchViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val activityService: ActivityService,
    private val repository: PickingRepository,
) : ViewModel(), DataEntryInterface {

    //region LiveData Vars

    val loaderState = MutableLiveData<LoaderState>()
    val branchDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    val submitButtonEnabled = MutableLiveData(false)
    val validateBranchResponseState = MutableLiveData<ResponseState<Response<ValidateBranchQuery.Data>>>()

    //endregion

    lateinit var navigateToOrdersCallback: () -> Unit

    fun setUp(
        navigationCallback: () -> Unit
    ) {
        navigateToOrdersCallback = navigationCallback
        setInputFields()
    }

    //region LiveData Updates

    private fun setBranchInputState(state: InputState) {
        branchDataEntryViewModel.value?.setInputState(state)
    }

    private fun setInputFields() {
        branchDataEntryViewModel.value?.setHintString(activityService.getString(R.string.branch_id))
        branchDataEntryViewModel.value?.setEndIconMode(TextInputLayout.END_ICON_NONE)

        userPreferences.getBranch()?.let { branchId ->
            if (branchId.isNotEmpty()) {
                branchDataEntryViewModel.value?.entryString?.value = userPreferences.getBranch()
                validateBranchInput()
            }
        }
    }

    private fun setSubmitButtonEnabled(enabled: Boolean) {
        submitButtonEnabled.value = enabled
    }

    //endregion

    //region DataEntryInterface Methods

    override fun afterTextChanged(id: Int, s: Editable) {
        if (id > 0) {
            validateBranchInput()
        }
    }

    //endregion

    //region Validation

    private fun validateBranchInput() {
        var submitEnabled = false
        var inputState: InputState = InputState.Default()

        activityService.getInteger(R.integer.mincron_branch_id_length_minimum)?.let { minLength ->
            if ((branchDataEntryViewModel.value?.getInputString()?.length ?: 0) >= minLength) {
                submitEnabled = true
            } else {
                inputState = InputState.Error(
                    message = activityService.getString(
                        R.string.branch_missing_error,
                        minLength.toString()
                    )
                )
            }
        }

        setSubmitButtonEnabled(submitEnabled)
        setBranchInputState(inputState)
    }

    //endregion

    //region Actions

    fun submitBranchId() {
        branchDataEntryViewModel.value?.getInputString()?.let { branchId ->
            activityService.hideKeyboard()
            queryValidateBranch(branchId)
        }
    }

    //endregion

    private fun setLoaderState(state: LoaderState) {
        loaderState.value = state
    }

    private fun setValidateBranchResponseState(state: ResponseState<Response<ValidateBranchQuery.Data>>) {
        validateBranchResponseState.value = state

        when (state) {
            is ResponseState.Success -> {
                state.response?.data?.validateBranch?.let { validateBranch ->
                    if (validateBranch.isValid) {
                        validateBranch.branch?.branchId?.let { userPreferences.setBranch(it) }
                        navigateToOrdersCallback()
                    } else {
                        activityService.errorMessage(activityService.getString(R.string.error_invalid_branch))
                    }
                }
            }
            is ResponseState.Error -> {
                activityService.errorMessage(state.message)
            }
            else -> {}
        }
    }

    fun queryValidateBranch(branchId: String) = viewModelScope.launch {
        setLoaderState(LoaderState.Loading)
        setValidateBranchResponseState(ResponseState.Default())

        try {
            repository.queryValidateBranch(
                ValidateBranchInput(branchId)
            )?.also { response ->
                if (response.errors.isNullOrEmpty()) {
                    setLoaderState(LoaderState.Default)
                    setValidateBranchResponseState(ResponseState.Success(response))
                } else {
                    setLoaderState(LoaderState.Default)
                    setValidateBranchResponseState(
                        ResponseState.Error(
                            message = response.errors?.first()?.message
                        )
                    )
                }
            }
        } catch (e: ApolloException) {
            setLoaderState(LoaderState.Default)
            setValidateBranchResponseState(
                ResponseState.Error(
                    message = activityService.getString(
                        R.string.error_validating_branch_id
                    )
                )
            )
        }
    }
}