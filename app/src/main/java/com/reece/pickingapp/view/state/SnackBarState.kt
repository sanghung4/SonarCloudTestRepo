package com.reece.pickingapp.view.state

import com.reece.pickingapp.utils.SnackBarType
import com.reece.pickingapp.utils.emptyString

class SnackBarState(
    val type: SnackBarType,
    val message: String? = emptyString
)