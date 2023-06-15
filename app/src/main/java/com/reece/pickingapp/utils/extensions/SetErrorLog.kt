package com.reece.pickingapp.utils.extensions

import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.repository.UserPreferences
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

fun setErrorLog(userPreferences: UserPreferences, message:String){
    if(userPreferences.getErrorLogs().isEmpty()) {
        val errorLog=ArrayList<ErrorLogDTO>()
        errorLog.add(ErrorLogDTO(message))
        userPreferences.setErrorLog(errorLog)
    }else{
        val errorLog=ArrayList<ErrorLogDTO>()
        errorLog.add(ErrorLogDTO(message))
        errorLog.addAll(userPreferences.getErrorLogs())
        userPreferences.setErrorLog(errorLog)
    }
} 