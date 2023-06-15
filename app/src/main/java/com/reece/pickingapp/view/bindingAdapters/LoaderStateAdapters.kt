package com.reece.pickingapp.view.bindingAdapters

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.reece.pickingapp.view.state.LoaderState

object LoaderStateAdapters {
    @BindingAdapter("refreshIsEnabled")
    @JvmStatic
    fun refreshIsEnabled(refresh: SwipeRefreshLayout, loaderState: LoaderState?) {
        refresh.isEnabled = loaderState == LoaderState.Default
    }

    @BindingAdapter("progressVisibility")
    @JvmStatic
    fun progressVisibility(progress: ProgressBar, state: LoaderState?) {
        progress.visibility = when (state) {
            is LoaderState.Loading -> View.VISIBLE
            else -> View.GONE
        }
    }

    @BindingAdapter("itemIsEnabledForLoaderState")
    @JvmStatic
    fun itemIsEnabledForLoaderState(view: View, state: LoaderState?) {
        view.isEnabled = when (state) {
            is LoaderState.Loading -> false
            else -> true
        }
    }
}