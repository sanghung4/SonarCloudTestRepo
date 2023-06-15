package com.reece.pickingapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.okta.authfoundation.client.OidcClientResult
import com.okta.authfoundation.credential.RevokeTokenType
import com.okta.authfoundationbootstrap.CredentialBootstrap
import com.okta.webauthenticationui.WebAuthenticationClient.Companion.createWebAuthenticationClient
import com.reece.pickingapp.BuildConfig
import com.reece.pickingapp.R
import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.models.SplitQtyDTO
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.utils.emptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val pickingApi: PickingApi
) : ViewModel(), UserPreferences {

    override fun getUsername(): String? {
        return userPreferences.getUsername()
    }

    override fun setUsername(username: String) {
        userPreferences.setUsername(username)
    }

    override fun getEmailId(): String? {
        return userPreferences.getEmailId()
    }

    override fun setEmailId(emailId: String) {
        userPreferences.setEmailId(emailId)
    }

    override fun getBranch(): String? {
        return userPreferences.getBranch()
    }

    override fun setBranch(branchId: String) {
        userPreferences.setBranch(branchId)
    }

    override fun setErrorLog(errorLog: ArrayList<ErrorLogDTO>) {
        userPreferences.setErrorLog(errorLog)
    }

    override fun getErrorLogs(): ArrayList<ErrorLogDTO> {
        return userPreferences.getErrorLogs()
    }

    override fun getUserPreference(): UserPreferences {
        return userPreferences
    }

    fun getUserName(): String? {
        return userPreferences.getUsername()
    }
    override fun saveSplitQty(splitList: SplitQtyDTO) {
        userPreferences.saveSplitQty(splitList)
    }

    override fun getSplitQty(): SplitQtyDTO {
        return userPreferences.getSplitQty()
    }

    override fun deleteSplitQty() {
        userPreferences.deleteSplitQty()
    }

    fun logoutOfBrowser(context: Context) {
        viewModelScope.launch{
            pickingApi.signOut(context)
        }
    }



}