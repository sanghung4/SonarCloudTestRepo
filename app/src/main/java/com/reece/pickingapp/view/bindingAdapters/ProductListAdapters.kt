package com.reece.pickingapp.view.bindingAdapters

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.view.View.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.reece.pickingapp.view.state.ResponseState
import com.reece.pickingapp.view.state.StagingOrderState
import com.reece.pickingapp.viewmodel.ProductsFragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

object ProductFragmentAdapters {

    //region Visibility

    @BindingAdapter("buttonVisibility")
    @JvmStatic
    fun buttonVisibility(button: MaterialButton, state: ResponseState<*>?) {
        button.visibility = when (state) {
            is ResponseState.Error -> VISIBLE
            else -> GONE
        }
    }

    @BindingAdapter("textFieldVisibility")
    @JvmStatic
    fun textFieldVisibility(layout: TextInputLayout, state: ResponseState<*>?) {
        layout.visibility = when (state) {
            is ResponseState.Error -> VISIBLE
            else -> GONE
        }
    }

    @BindingAdapter("textViewVisibility")
    @JvmStatic
    fun textFieldVisibility(layout: MaterialTextView, state: ResponseState<*>?) {
        layout.visibility = when (state) {
            is ResponseState.Error -> VISIBLE
            else -> GONE
        }
    }

    @BindingAdapter("recyclerViewVisibility")
    @JvmStatic
    fun recyclerViewVisibility(recyclerView: RecyclerView, state: ResponseState<*>?) {
        recyclerView.visibility = when (state) {
            is ResponseState.Success -> VISIBLE
            else -> INVISIBLE
        }
    }

    @BindingAdapter("enabledForState")
    @JvmStatic
    fun enabledForState(view: View, state: StagingOrderState?) {
        view.isEnabled = when (state) {
            is StagingOrderState.StageReady -> true
            else -> false
        }
    }

    //endregion

    @OptIn(ExperimentalCoroutinesApi::class)
    @BindingAdapter("refreshIsEnabled")
    @JvmStatic
    fun refreshIsEnabled(refresh: SwipeRefreshLayout, viewModel: ProductsFragmentViewModel?) {
        refresh.isEnabled =
            viewModel?.activeResponseState?.value == viewModel?.productsResponseState?.value
    }

    @BindingAdapter("errorTextState")
    @JvmStatic
    fun errorTextState(textView: TextView, state: ResponseState<*>?) {
        val activeVisibility = when (state) {
            is ResponseState.Error -> VISIBLE
            else -> GONE
        }
        textView.apply {
            text = state?.message
            visibility = activeVisibility
        }
    }

    @BindingAdapter("noDataState")
    @JvmStatic
    fun noDataState(constraintLayout: ConstraintLayout, state: Boolean) {
        val activeVisibility = when (state) {
            true -> VISIBLE
            else -> GONE
        }
        constraintLayout.apply {
            visibility = activeVisibility
        }
    }

    @BindingAdapter("viewVisibility")
    @JvmStatic
    fun viewVisibility(view: View, state: Boolean) {
        view.visibility = when (state) {
            true -> VISIBLE
            else -> GONE
        }
    }

    @BindingAdapter("underlineText")
    @JvmStatic
    fun underlineText(view: TextView, string: String) {
        if (string.isNullOrEmpty()) return

        val spannableString = SpannableString(string).apply {
            setSpan(UnderlineSpan(), 0, string.length, 0)
        }

        view.text = spannableString
    }
}