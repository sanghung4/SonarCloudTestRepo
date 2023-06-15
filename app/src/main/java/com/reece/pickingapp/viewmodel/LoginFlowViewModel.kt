package com.reece.pickingapp.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reece.pickingapp.R
import com.reece.pickingapp.interfaces.DataEntryInterface
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.utils.ActivityService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.math.roundToInt

@ExperimentalCoroutinesApi
@HiltViewModel
class LoginFlowViewModel @Inject constructor(
    private val activityService: ActivityService,
    private val userPreferences: UserPreferences,
    ) : ViewModel(), DataEntryInterface {

    val stepData = MutableLiveData<String>()
    val loginProgressData = MutableLiveData<Int>()
    val isVisible = MutableLiveData<Int>()

    fun setUp() {
        isVisible.value = View.VISIBLE
    }

     fun setLoginStepData(stepNumber: Int) {
         stepData.value = activityService.getString(R.string.step) + " $stepNumber/3"
         if(stepNumber == 1){
             isVisible.value = View.GONE
         }else{
             if (isAnyEclipseUserSaved()){
                 isVisible.value = View.GONE
             }else{
                 isVisible.value = View.VISIBLE
             }
         }

    }

    fun isAnyEclipseUserSaved ():Boolean{
        return !userPreferences.getUsername().isNullOrEmpty()
    }

     fun setLoginIndicatorProgress(count: Int) {
         loginProgressData.value = ((33.3) * count).roundToInt()
    }
}