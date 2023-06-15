package com.reece.pickingapp.view.state

import com.reece.pickingapp.utils.emptyString

sealed class CardState(
    val message: String? = emptyString
) {
    class Default : CardState()
    class Error(message: String?) : CardState(message)
    class Loading : CardState()
    class Ready : CardState()
    class Success : CardState()
}