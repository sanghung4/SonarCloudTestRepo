package com.reece.pickingapp.view.state

import com.reece.pickingapp.utils.emptyString


sealed class InputState(
    val message: String? = emptyString
) {
    class Default : InputState()
    class Disabled : InputState()
    class Error(message: String?) : InputState(message)
    class Success(message: String?) : InputState(message)
    class Hidden : InputState()
}