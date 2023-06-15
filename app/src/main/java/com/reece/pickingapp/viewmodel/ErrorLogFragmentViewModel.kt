package com.reece.pickingapp.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.view.adapter.ErrorListAdapter
import com.reece.pickingapp.view.state.LoaderState
import com.reece.pickingapp.wrappers.ErrorListAdapterWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ErrorLogFragmentViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val errorsAdapterWrapper: ErrorListAdapterWrapper = ErrorListAdapterWrapper()
) : ViewModel() {

    //region LiveData Vars
    val errorsAdapter = MutableLiveData<ErrorListAdapter>()
    val loaderState = MutableLiveData<LoaderState>()
    lateinit var viewLifecycleOwner: LifecycleOwner

    fun setUp(
        lifecycleOwner: LifecycleOwner
    ) {
        viewLifecycleOwner = lifecycleOwner
        errorsAdapter.value = errorsAdapterWrapper.createAdapter(viewLifecycleOwner)
        setLoaderState(LoaderState.Loading)
    }

    fun showErrorData(){
        var errorLogList = userPreferences.getErrorLogs()
        if (errorLogList.isEmpty()){
            errorLogList.add(
                ErrorLogDTO(
                "", "No error logs found"
            ))
        }
        val viewModels = generateViewHolderViewModels(
            errorLogList,
        )
        setLoaderState(LoaderState.Default)

        errorsAdapter.value?.submitList(viewModels)
    }

    private fun generateViewHolderViewModels(
        errorLogs: ArrayList<ErrorLogDTO>,
    ): MutableList<ErrorLogViewHolderViewModel> {
        val list = mutableListOf<ErrorLogViewHolderViewModel>()
        errorLogs.let { errors ->
            for (error in errors) {
                error.let {
                    val viewHolderViewModel = ErrorLogViewHolderViewModel(
                        errorLogs,
                    )
                    list.add(viewHolderViewModel)
                }
            }
        }
        return list
    }

    private fun setLoaderState(state: LoaderState) {
        loaderState.value = state
    }
}