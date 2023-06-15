package com.reece.pickingapp.view.state

sealed class LoaderState {
    object Loading : LoaderState()
    object Default : LoaderState()
}