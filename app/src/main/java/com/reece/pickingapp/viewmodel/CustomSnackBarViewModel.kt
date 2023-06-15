package com.reece.pickingapp.viewmodel

import android.text.Spanned
import androidx.lifecycle.MutableLiveData
import com.reece.pickingapp.utils.StringConverter
import com.reece.pickingapp.view.state.SnackBarState

class CustomSnackBarViewModel(
    state: SnackBarState,
    private val dismissCallback: (() -> Unit),
    private val stringConverter: StringConverter = StringConverter
) {
    val snackBarState = MutableLiveData(state)

    fun closeButtonTapped() {
        dismissCallback()
    }

    fun getMessage(): Spanned? {
        snackBarState.value?.message?.let {
            return stringConverter.convertToHtmlSpan(it)
        }

        return null
    }
}