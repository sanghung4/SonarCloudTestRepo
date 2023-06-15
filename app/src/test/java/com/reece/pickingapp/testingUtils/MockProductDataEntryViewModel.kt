package com.reece.pickingapp.testingUtils

import com.reece.pickingapp.viewmodel.DataEntryViewModel
import io.mockk.every
import io.mockk.mockk

class MockDataEntryViewModel {
    fun create(): DataEntryViewModel {
        return mockk {
            every { setHintString(any()) } returns Unit
            every { setInputCanBeCleared(any()) } returns Unit
            every { setInputState(any()) } returns Unit
            every { setInputFocusState(any()) } returns Unit
            every { resetEntryString() } returns Unit
        }
    }
}