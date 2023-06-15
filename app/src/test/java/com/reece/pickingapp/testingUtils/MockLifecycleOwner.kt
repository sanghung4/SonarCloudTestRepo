package com.reece.pickingapp.testingUtils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic

class MockLifecycleOwner {
    fun create(): LifecycleOwner {
        mockkStatic(LifecycleOwner::class)
        mockkStatic(LifecycleRegistry::class)
        val owner: LifecycleOwner = mockk()
        val lifecycle: LifecycleRegistry = mockk()
        every { lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME) } returns Unit
        every { owner.lifecycle } returns lifecycle

        return owner
    }
}