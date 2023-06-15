package com.reece.pickingapp.interfaces

import android.text.Editable
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.TextView
import com.reece.pickingapp.viewmodel.DataEntryViewModel

/**
 * Optional methods available to the Interface.
 * Interface is added as a dependency to a ViewModel
 */
interface DataEntryInterface {
    fun onEditorAction(textView: TextView, actionId: Int, keyEvent: KeyEvent?): Boolean {
        return false

    }

    fun onTextChanged(id: Int, s: CharSequence, start: Int, before: Int, count: Int) {}

    fun afterTextChanged(id: Int, s: Editable) {}

    fun onInputComponentFocus(component: ViewGroup) {}

    fun deleteButtonTapped(dataEntryViewModel: DataEntryViewModel){
        dataEntryViewModel.deleteButtonTapped()
    }
}