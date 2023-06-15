package com.reece.pickingapp.view.state

import com.reece.pickingapp.utils.emptyString

sealed class PickFromOtherLocationsState(
    val message: String? = emptyString
) {
    class ReadyToPick : PickFromOtherLocationsState()
    class Excess (message: String?) : PickFromOtherLocationsState(message)
    class Insufficient (message: String?) : PickFromOtherLocationsState(message)
    class Default : PickFromOtherLocationsState()
}
