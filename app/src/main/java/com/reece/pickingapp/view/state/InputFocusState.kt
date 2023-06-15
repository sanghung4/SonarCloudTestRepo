package com.reece.pickingapp.view.state

sealed class InputFocusState {
    object Default : InputFocusState()
    object Focussed : InputFocusState()
    object NonFocusable : InputFocusState()
    object ClearFocus : InputFocusState()
}