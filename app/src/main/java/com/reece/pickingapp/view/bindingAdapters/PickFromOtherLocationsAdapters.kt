package com.reece.pickingapp.view.bindingAdapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.reece.pickingapp.view.state.PickFromOtherLocationsState

/**
 * See DataEntryViewModel for usage of the BindingAdapter methods
 */

object PickFromOtherLocationsAdapters {

    @BindingAdapter("otherLocationsTopErrorMessage")
    @JvmStatic
    fun otherLocationsTopErrorMessage(layout: TextView, state: PickFromOtherLocationsState?) {
        when (state) {
            is PickFromOtherLocationsState.Excess -> {
                layout.visibility = View.VISIBLE
                layout.text = state.message.toString()
            }
            else -> layout.visibility = View.GONE
        }

    }

    @BindingAdapter("otherLocationsBottomErrorMessage")
    @JvmStatic
    fun otherLocationsBottomErrorMessage(layout: TextView, state: PickFromOtherLocationsState?) {
        when (state) {
            is PickFromOtherLocationsState.Insufficient -> {
                layout.visibility = View.VISIBLE
                layout.text = state.message.toString()
            }
            else -> layout.visibility = View.GONE
        }

    }

    @BindingAdapter("otherLocationsCompleteButtonState")
    @JvmStatic
    fun otherLocationsCompleteButtonState(layout: MaterialButton, state: PickFromOtherLocationsState?) {
        when (state) {
            is PickFromOtherLocationsState.ReadyToPick -> {
                layout.isEnabled = true
            }
            is PickFromOtherLocationsState.Default -> {
                layout.isEnabled = true
            }
            else -> layout.isEnabled = false
        }

    }
}