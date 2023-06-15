@file:JvmName(name = "ActivityExtensions")

package com.reece.pickingapp.utils.extensions

import android.app.Activity
import android.graphics.Color
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.CustomSnackbarBinding
import com.reece.pickingapp.databinding.MoveProductToEndSnackbarBinding
import com.reece.pickingapp.utils.SnackBarType
import com.reece.pickingapp.utils.emptyString
import com.reece.pickingapp.view.state.SnackBarState
import com.reece.pickingapp.viewmodel.CustomSnackBarViewModel

fun Activity.showMessage(snackBarState: SnackBarState, duration: Int = Snackbar.LENGTH_INDEFINITE) {
    val view = window.decorView.findViewById<View>(android.R.id.content)
    val snackBar = Snackbar.make(view, emptyString, duration)
    snackBar.view.setBackgroundColor(Color.TRANSPARENT)
    val snackBarLayout = snackBar.view as SnackbarLayout
    snackBarLayout.setPadding(0, 0, 0, 0)
    val binding: CustomSnackbarBinding = DataBindingUtil.inflate(
        layoutInflater,
        R.layout.custom_snackbar,
        snackBarLayout,
        false
    )

    val viewModel = CustomSnackBarViewModel(
        snackBarState,
        dismissCallback = { snackBar.dismiss() }
    )
    binding.viewModel = viewModel
    snackBarLayout.addView(binding.root, 0)
    snackBar.show()
}

fun Activity.showProductMovedMessage(duration: Int = Snackbar.LENGTH_INDEFINITE) {
    val view = window.decorView.findViewById<View>(android.R.id.content)
    val snackBar = Snackbar.make(view, emptyString, duration)
    snackBar.view.setBackgroundColor(Color.TRANSPARENT)
    val snackBarLayout = snackBar.view as SnackbarLayout
    snackBarLayout.setPadding(0, 0, 0, 0)
    val binding: MoveProductToEndSnackbarBinding = DataBindingUtil.inflate(
        layoutInflater,
        R.layout.move_product_to_end_snackbar,
        snackBarLayout,
        false
    )

    snackBarLayout.addView(binding.root, 0)
    snackBar.show()
}

fun Activity.errorMessage(message: String?) {
    val state = SnackBarState(
        SnackBarType.ERROR,
        message
    )
    this.showMessage(state, Snackbar.LENGTH_SHORT)
}