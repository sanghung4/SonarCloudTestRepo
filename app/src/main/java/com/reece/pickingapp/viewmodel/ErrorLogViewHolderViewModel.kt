package com.reece.pickingapp.viewmodel

import android.text.Editable
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.mlkit.vision.barcode.common.Barcode
import com.reece.pickingapp.R
import com.reece.pickingapp.interfaces.BarCodeScanInterface
import com.reece.pickingapp.interfaces.DataEntryInterface
import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.type.SerialLineInput
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.DataEntryFieldType
import com.reece.pickingapp.utils.extensions.filterToNum
import com.reece.pickingapp.view.state.CardState
import com.reece.pickingapp.view.state.InputFocusState
import com.reece.pickingapp.view.state.InputState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ErrorLogViewHolderViewModel(
    val errorLogs:ArrayList<ErrorLogDTO>,

) : ViewModel(), DataEntryInterface, BarCodeScanInterface {

    var adapterPosition: Int = 0

    fun setUp() {

    }


}
