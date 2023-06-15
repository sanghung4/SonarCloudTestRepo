package com.reece.pickingapp.repository

import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.models.SplitQtyDTO

interface UserPreferences {
    fun getUsername(): String?
    fun setUsername(username: String)
    fun getEmailId(): String?
    fun setEmailId(username: String)
    fun getBranch(): String?
    fun setBranch(branchId: String)
    fun setErrorLog(errorLog: ArrayList<ErrorLogDTO>)
    fun getErrorLogs(): ArrayList<ErrorLogDTO>
    fun getUserPreference(): UserPreferences
    fun saveSplitQty(splitList: SplitQtyDTO)
    fun getSplitQty(): SplitQtyDTO
    fun deleteSplitQty()
}