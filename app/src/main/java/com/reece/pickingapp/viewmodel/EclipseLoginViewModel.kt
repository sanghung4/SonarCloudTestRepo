package com.reece.pickingapp.viewmodel

import android.util.Log
import android.widget.CompoundButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.textfield.TextInputLayout
import com.reece.pickingapp.R
import com.reece.pickingapp.ValidateBranchQuery
import com.reece.pickingapp.VerifyEclipseCredentialsMutation
import com.reece.pickingapp.interfaces.DataEntryInterface
import com.reece.pickingapp.models.EclipseCredentialModel
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.repository.EclipseLoginRepository
import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.type.ValidateBranchInput
import com.reece.pickingapp.utils.AESEncyption
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.DataEntryFieldType
import com.reece.pickingapp.utils.extensions.setErrorLog
import com.reece.pickingapp.view.state.InputFocusState
import com.reece.pickingapp.view.state.InputState
import com.reece.pickingapp.view.state.LoaderState
import com.reece.pickingapp.view.state.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class EclipseLoginViewModel @Inject constructor(
    private val pickingRepository: PickingRepository,
    private val userPreferences: UserPreferences,
    private val pickingApi: PickingApi,
    private val activityService: ActivityService,
    private val repository: EclipseLoginRepository
) : ViewModel(), DataEntryInterface {
    val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }

    //region LiveData Vars
    val validateEclipseResponseState =
        MutableLiveData<ResponseState<Response<VerifyEclipseCredentialsMutation.Data>>>()
    val usernameDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    val passwordDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    val branchDataEntryViewModel = MutableLiveData(DataEntryViewModel())
    val loaderState = MutableLiveData<LoaderState>()
    val validateBranchResponseState =
        MutableLiveData<ResponseState<Response<ValidateBranchQuery.Data>>>()
    val eclipseLoginCheckBox = MutableLiveData<Boolean>(false)
    private val _checkBoxEnable = MutableLiveData<Boolean>(true)

    private lateinit var userEmail: String
    val checkBoxEnable: LiveData<Boolean>
        get() = _checkBoxEnable


    //endregion

    lateinit var navigateToBranchEntryCallback: () -> Unit

    fun setUp(
        navigationCallback: () -> Unit
    ) {
        navigateToBranchEntryCallback = navigationCallback
        setInputFields()
    }

    //region LiveData Updates
    private fun setValidateEclipseResponseState(state: ResponseState<Response<VerifyEclipseCredentialsMutation.Data>>) {
        validateEclipseResponseState.value = state
        when (state) {
            is ResponseState.Success -> {
                //make branchID request
                submitBranchId()
            }

            is ResponseState.Error -> {
                setAllInputFocusState(InputFocusState.Default)
                setEclipseLoginInputState(InputState.Error(message = state.message))
                activityService.errorMessage(state.message)
                _checkBoxEnable.value = true
            }

            else -> {}
        }
    }

    private fun setLoaderState(state: LoaderState) {
        loaderState.value = state
    }

    private fun setAllInputFieldsInputState(state: InputState) {
        usernameDataEntryViewModel.value?.setInputState(state)
        passwordDataEntryViewModel.value?.setInputState(state)
        branchDataEntryViewModel.value?.setInputState(state)
    }

    private fun setEclipseLoginInputState(state: InputState) {
        usernameDataEntryViewModel.value?.setInputState(state)
        passwordDataEntryViewModel.value?.setInputState(state)
    }

    private fun setAllInputFocusState(state: InputFocusState) {
        usernameDataEntryViewModel.value?.setInputFocusState(state)
        passwordDataEntryViewModel.value?.setInputFocusState(state)
        branchDataEntryViewModel.value?.setInputFocusState(state)
    }

    private fun setInputFields() {
        usernameDataEntryViewModel.value?.setHintString(activityService.getString(R.string.eclipse_username_hint))
        usernameDataEntryViewModel.value?.setEndIconMode(TextInputLayout.END_ICON_NONE)

        passwordDataEntryViewModel.value?.setHintString(activityService.getString(R.string.eclipse_password_hint))
        passwordDataEntryViewModel.value?.setDataEntryFieldType(DataEntryFieldType.PASSWORD)
        passwordDataEntryViewModel.value?.setEndIconMode(TextInputLayout.END_ICON_NONE)

        branchDataEntryViewModel.value?.setHintString(activityService.getString(R.string.branch_id))
        branchDataEntryViewModel.value?.setDataEntryFieldType(DataEntryFieldType.QUANTITY_NUMBER)
        branchDataEntryViewModel.value?.setEndIconMode(TextInputLayout.END_ICON_NONE)

        userPreferences.getBranch()?.let { branchId ->
            if (branchId.isNotEmpty()) {
                branchDataEntryViewModel.value?.entryString?.value = userPreferences.getBranch()
                branchDataEntryViewModel.value?.setInputState(InputState.Disabled())
                branchDataEntryViewModel.value?.setInputFocusState(InputFocusState.NonFocusable)
            }
        }
        eclipseLoginCheckBox.value = true
    }
    //endregion

    //region Queries and Mutations
    private fun linkAccount(username: String, password: String) = viewModelScope.launch {
        setLoaderState(LoaderState.Loading)
        setAllInputFocusState(InputFocusState.NonFocusable)
        setAllInputFieldsInputState(InputState.Disabled())
        _checkBoxEnable.value = false

        try {
            pickingRepository.mutationVerifyEclipseCredentials(username, password)
                ?.also { response ->
                    if (response.data?.verifyEclipseCredentials?.success == true) {
                        userPreferences.setUsername(username)
                        setValidateEclipseResponseState(ResponseState.Success(response))
                    } else {
                        setErrorLog(userPreferences, (response.errors ?: "") as String)
                        setLoaderState(LoaderState.Default)
                        _checkBoxEnable.value = true
                        setValidateEclipseResponseState(
                            ResponseState.Error(
                                message = response.data?.verifyEclipseCredentials?.message
                            )
                        )
                    }
                }
        } catch (e: ApolloException) {
            setValidateEclipseResponseState(
                ResponseState.Error(
                    message = e.message
                )
            )
            setLoaderState(LoaderState.Default)
            _checkBoxEnable.value = true
        }
    }

    //endregion
    //region Handlers

    fun signInButtonTapped() {
        activityService.hideKeyboard()
        validateInput()
    }

    fun backButtonSignOut() {
        viewModelScope.launch {
            activityService.activity?.let { pickingApi.signOut(it) }
        }
    }

    //endregion
    //region Utils
    private fun getUsernameFieldValue(): String {
        return usernameDataEntryViewModel.value?.entryString?.value.orEmpty().trimEnd().uppercase()
    }

    private fun getPasswordFieldValue(): String {
        return passwordDataEntryViewModel.value?.entryString?.value.orEmpty().trimEnd()
    }

    //endregion
    //branchID
    private fun validateInput() {
        var branchIsOK = false
        var usernameIsOk = false
        var passwordIsOk = false

        activityService.getInteger(R.integer.mincron_minimun_length_minimum)?.let { minLength ->
            if ((usernameDataEntryViewModel.value?.getInputString()?.length ?: 0) >= minLength) {
                usernameIsOk = true
                usernameDataEntryViewModel.value?.setInputState(InputState.Default())
            } else {
                val inputState = InputState.Error(
                    message = activityService.getString(
                        R.string.user_name_missing_error,
                        minLength.toString()
                    )
                )
                usernameDataEntryViewModel.value?.setInputState(inputState)
            }
        }
        activityService.getInteger(R.integer.mincron_password_length_minimum)?.let { minLength ->
            if ((passwordDataEntryViewModel.value?.getInputString()?.length ?: 0) >= minLength) {
                passwordIsOk = true
                passwordDataEntryViewModel.value?.setInputState(InputState.Default())
            } else {
                val inputState = InputState.Error(
                    message = activityService.getString(
                        R.string.password_missing_error,
                        minLength.toString()
                    )
                )
                passwordDataEntryViewModel.value?.setInputState(inputState)
            }
        }
        activityService.getInteger(R.integer.mincron_branch_id_length_minimum)?.let { minLength ->
            if ((branchDataEntryViewModel.value?.getInputString()?.length ?: 0) >= minLength) {
                branchIsOK = true
                branchDataEntryViewModel.value?.setInputState(InputState.Default())
            } else {
                val inputState = InputState.Error(
                    message = activityService.getString(
                        R.string.branch_missing_error,
                        minLength.toString()
                    )
                )
                branchDataEntryViewModel.value?.setInputState(inputState)
            }
        }
        if (branchIsOK && usernameIsOk && passwordIsOk) {
            linkAccount(
                username = getUsernameFieldValue(),
                password = getPasswordFieldValue()
            )
        }
    }

    fun submitBranchId() {
        branchDataEntryViewModel.value?.getInputString()?.let { branchId ->
            queryValidateBranch(branchId)
        }
    }

    private fun setValidateBranchResponseState(state: ResponseState<Response<ValidateBranchQuery.Data>>) {
        validateBranchResponseState.value = state
        when (state) {
            is ResponseState.Success -> {
                state.response?.data?.validateBranch?.let { validateBranch ->
                    if (validateBranch.isValid) {
                        validateBranch.branch?.branchId?.let { userPreferences.setBranch(it) }
                        if (eclipseLoginCheckBox.value == true) {
                            //save data in db
                            viewModelScope.launch {
                                //make the eclipse login validation
                                validateUserStatus(userPreferences.getEmailId()
                                    ?.let { getUserCredentials(it) })
                            }
                        }else{
                            navigateToBranchEntryCallback()
                        }

                    } else {
                        setAllInputFieldsInputState(InputState.Default())
                        branchDataEntryViewModel.value?.setInputFocusState(InputFocusState.Focussed)
                        branchDataEntryViewModel.value?.setInputState(InputState.Error(message = state.message))
                        activityService.errorMessage(activityService.getString(R.string.error_invalid_branch))
                    }
                }
            }

            is ResponseState.Error -> {
                setLoaderState(LoaderState.Default)
                _checkBoxEnable.value = true
                activityService.errorMessage(state.message)
            }

            else -> {}
        }
    }

    fun queryValidateBranch(branchId: String) = viewModelScope.launch {
        setValidateBranchResponseState(ResponseState.Default())
        try {
            pickingRepository.queryValidateBranch(
                ValidateBranchInput(branchId)
            )?.also { response ->
                if (response.errors.isNullOrEmpty()) {
                    //setLoaderState(LoaderState.Default)
                    setValidateBranchResponseState(ResponseState.Success(response))
                } else {
                    setLoaderState(LoaderState.Default)
                    _checkBoxEnable.value = true
                    setValidateBranchResponseState(
                        ResponseState.Error(
                            message = response.errors?.first()?.message
                        )
                    )
                }
            }
        } catch (e: ApolloException) {
            setLoaderState(LoaderState.Default)
            _checkBoxEnable.value = true
            setValidateBranchResponseState(
                ResponseState.Error(
                    message = activityService.getString(
                        R.string.error_validating_branch_id
                    )
                )
            )
        }
    }

    //endRegion branchID
    //checkBox
    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        eclipseLoginCheckBox.value = check
    }

    //save credential in db
    private suspend fun addUserToLogin(credential: EclipseCredentialModel) {
        repository.addCredential(credential = credential)
        navigateToBranchEntryCallback()
    }

    private suspend fun getUserCredentials(email: String):EclipseCredentialModel? {
        return repository.getCredentialByEmail(email)
    }

    private suspend fun getOktaEmail(): String {
        userEmail = pickingApi.getOktaUserEmail()
        Log.i(TAG,userEmail)
        return userEmail
    }

    fun isAnyEclipseUserSaved ():Boolean{
        return !userPreferences.getUsername().isNullOrEmpty()
    }

    private suspend fun validateUserStatus(credential: EclipseCredentialModel?) = viewModelScope.launch {
        if (credential != null) {
            credential.isRemember = true
            credential.username = getUsernameFieldValue()
            credential.eclipsePass = AESEncyption.encrypt(getPasswordFieldValue()) ?: ""
            addUserToLogin(credential)
        } else {
            var newCredential = AESEncyption.encrypt(getPasswordFieldValue())?.let {
                EclipseCredentialModel(
                    username = getUsernameFieldValue(),
                    email = userPreferences.getEmailId()?:"",
                    eclipsePass = it,
                    isRemember = true,
                )
            }
            newCredential?.let { addUserToLogin(it) }
        }
    }
}