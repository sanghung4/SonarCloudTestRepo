package com.reece.pickingapp.utils.extensions

import androidx.lifecycle.*
import com.reece.pickingapp.view.state.ViewState

fun <T> ViewModel.removeObserverFor(
    liveData: MutableLiveData<ViewState<T>>,
    observer: Observer<ViewState<T>>
) {
    liveData.removeObserver(observer)
    liveData.value = ViewState.Default(liveData.value?.value)
}

fun <T> ViewModel.errorStateFor(
    liveData: MutableLiveData<ViewState<T>>,
    message: String?,
    observerToRemove: Observer<ViewState<T>>? = null
) {
    liveData.value = ViewState.Error(message ?: "ERROR")
    observerToRemove?.let {
        removeObserverFor(liveData, it)
    }
}