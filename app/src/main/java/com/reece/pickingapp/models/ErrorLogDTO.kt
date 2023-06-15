package com.reece.pickingapp.models

import android.os.Parcelable
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.toInput
import com.reece.pickingapp.type.CompletePickInput
import com.reece.pickingapp.utils.emptyString
import kotlinx.parcelize.Parcelize
import java.text.DateFormat
import java.util.*


data class ErrorLogDTO(
    var errorName: String, var timestmap:String = DateFormat.getDateTimeInstance().format(Date())
)