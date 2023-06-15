package com.reece.pickingapp.testingUtils

import io.mockk.mockkStatic

object ExtensionMock {
    fun activityExtensions() =
        mockkStatic("com.reece.pickingapp.utils.extensions.ActivityExtensions")
    fun stringExtensions() =
        mockkStatic("com.reece.pickingapp.utils.extensions.StringExtensions")
}