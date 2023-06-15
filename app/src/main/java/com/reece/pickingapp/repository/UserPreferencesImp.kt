package com.reece.pickingapp.repository

import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.models.SplitQtyDTO
import com.reece.pickingapp.utils.ActivityService
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import java.lang.reflect.Type
import javax.inject.Inject


@Module
@InstallIn(ActivityComponent::class)
class UserPreferencesImp @Inject constructor(
    private val activityService: ActivityService
) : UserPreferences {

    //region Username

    override fun getUsername(): String? {
        return activityService.activity?.getSharedPreferences(preferences_key, MODE_PRIVATE)
            ?.getString(username_key, null)
    }

    override fun setUsername(username: String) {
        activityService.activity?.getSharedPreferences(preferences_key, MODE_PRIVATE)?.edit()
            ?.putString(username_key, username)?.apply()
    }

    //endregion

    //region BranchId

    override fun getBranch(): String? {
        return activityService.activity?.getSharedPreferences(preferences_key, MODE_PRIVATE)
            ?.getString(branch_key, null)
    }

    override fun setBranch(branchId: String) {
        activityService.activity?.getSharedPreferences(preferences_key, MODE_PRIVATE)?.edit()
            ?.putString(branch_key, branchId)?.apply()
    }


    override fun setErrorLog(errorLog: ArrayList<ErrorLogDTO>) {
        val gson = Gson()

        val json = gson.toJson(errorLog)
        activityService.activity?.getSharedPreferences(preferences_key, MODE_PRIVATE)?.edit()
            ?.putString(error_log, json)?.apply()

    }

    override fun getErrorLogs(): ArrayList<ErrorLogDTO> {

        val gson = Gson()
        val json = activityService.activity?.getSharedPreferences(preferences_key, MODE_PRIVATE)
            ?.getString(error_log, "")
        var errorLogList = ArrayList<ErrorLogDTO>()

        if (json?.isNotEmpty() == true) {
            val type: Type = object : TypeToken<ArrayList<ErrorLogDTO?>?>() {}.getType()
            errorLogList = gson.fromJson<ArrayList<ErrorLogDTO>>(json, type)
        }
        return errorLogList
    }

    override fun getUserPreference(): UserPreferences {
       return activityService.userPreferences!!
    }
    //endregion

    //Start EmailId

    override fun getEmailId(): String? {
        return activityService.activity?.getSharedPreferences(preferences_key, MODE_PRIVATE)
            ?.getString(email_key, null)
    }

    override fun setEmailId(emailId: String) {
        activityService.activity?.getSharedPreferences(preferences_key, MODE_PRIVATE)?.edit()
            ?.putString(email_key, emailId)?.apply()
    }

    //end Email Id
    //Start Save BackOrder
    override fun saveSplitQty(splitList: SplitQtyDTO) {
        val gson = Gson()
        val json = gson.toJson(splitList)
        activityService.activity?.getSharedPreferences(split_qty_key, MODE_PRIVATE)?.edit()
            ?.putString(split_qty_key, json)?.apply()

    }

    override fun getSplitQty(): SplitQtyDTO {
        val gson = Gson()
        val json = activityService.activity?.getSharedPreferences(split_qty_key, MODE_PRIVATE)
            ?.getString(split_qty_key, "")
        var backOrderList = SplitQtyDTO()

        if (json?.isNotEmpty() == true) {
            val type: Type = object : TypeToken<SplitQtyDTO?>() {}.getType()
            backOrderList = gson.fromJson(json, type)
        }
        return backOrderList
    }

    override fun deleteSplitQty() {
        activityService.activity?.getSharedPreferences(split_qty_key, MODE_PRIVATE)?.edit()
            ?.remove(split_qty_key)?.apply()
    }
    //end Save BackOrder

    //region Keys

    companion object {
        const val preferences_key = "UserAccount"
        const val username_key = "username";
        const val branch_key = "branch_id"
        const val email_key = "email_id"
        const val error_log = "error_log"
        const val split_qty_key = "split_qty"
    }

    //endregion
}