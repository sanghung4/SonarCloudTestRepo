@file:JvmName(name = "FragmentExtensions")

package com.reece.pickingapp.utils.extensions

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

fun Fragment.onBackPressedAction(action: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        override
        fun handleOnBackPressed() {
            action()
        }
    })
}