package com.reece.pickingapp.models

import com.reece.pickingapp.utils.emptyString

data class AlternateLocationsDTO(
    var location: String = emptyString,
    var qty: String = "0",
    var serialNumbers: MutableList<String> = mutableListOf()
)
