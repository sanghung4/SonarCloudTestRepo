package com.reece.pickingapp.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.utils.extensions.errorMessage
import com.reece.pickingapp.utils.extensions.showMessage
import com.reece.pickingapp.utils.extensions.showProductMovedMessage
import com.reece.pickingapp.view.state.SnackBarState
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivityService {
    var activity: Activity? = null
    var navController: NavController? = null
    var userPreferences: UserPreferences? = null
    fun setUp(activity: Activity, navController: NavController, userPreferences: UserPreferences) {
        this.activity = activity
        this.navController = navController
        this.userPreferences = userPreferences
    }

    fun getString(resourceInt: Int): String? {
        return activity?.getString(resourceInt)
    }

    fun getString(resourceInt: Int, concat: String): String? {
        return activity?.getString(resourceInt, concat)
    }

    fun getString(resourceInt: Int, concat1: String?, concat2: String?): String? {
        return activity?.getString(resourceInt, concat1, concat2)
    }

    fun getInteger(resourceInt: Int): Int? {
        return activity?.resources?.getInteger(resourceInt)
    }

    fun hideKeyboard() {
        activity?.let { activity ->
            val inputMethodService =
                activity.baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val windowToken = activity.window.decorView.rootView.windowToken
            inputMethodService.hideSoftInputFromWindow(windowToken, 0)
        }
    }

    fun showKeyboard() {
        activity?.let { activity ->
            val inputMethodService =
                activity.baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodService.showSoftInput(
                activity.currentFocus,
                InputMethodManager.SHOW_IMPLICIT
            )
        }
    }

    fun showMessage(snackBarState: SnackBarState, duration: Int = Snackbar.LENGTH_INDEFINITE) {
        activity?.showMessage(snackBarState, duration)
    }

    fun showProductMovedMessage(duration: Int = Snackbar.LENGTH_INDEFINITE) {
        activity?.showProductMovedMessage(duration)
    }

    fun errorMessage(message: String?) {
        activity?.errorMessage(message = message)
    }

    fun showToolBarBackButton() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    fun hideToolBarBackButton() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeButtonEnabled(false)
        }
    }

    fun setToolbarTitle(title: String?){
        (activity as AppCompatActivity).supportActionBar?.title = title?:""
    }
}