package com.reece.pickingapp.view.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.FragmentEclipseLoginBinding
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.viewmodel.EclipseLoginViewModel
import com.reece.pickingapp.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EclipseLoginFragment : Fragment() {
    private val TAG: String = "EclipseLoginFragment"
    private lateinit var binding: FragmentEclipseLoginBinding
    private val viewModel by viewModels<EclipseLoginViewModel>()
    private val signInViewModel by viewModels<SignInViewModel>()
    @Inject
    lateinit var activityService: ActivityService
    //region Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setUp(navigationCallback = { navigateToBranchEntry() })
        binding = FragmentEclipseLoginBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        verifyIfIsRegisteredUser()

        return binding.root
    }


    fun verifyIfIsRegisteredUser (){
        binding.eclipseLoginLayoutContainer.visibility = View.INVISIBLE
        if(viewModel.isAnyEclipseUserSaved()){
            navigateToBranchEntry()
        }else{
            binding.eclipseLoginLayoutContainer.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
    }

    //endregion

    //region Navigation

    private fun navigateToBranchEntry() {
        activity?.invalidateOptionsMenu()
        activityService.navController?.navigate(R.id.action_global_start)
    }

    //endregion

    //region Listeners

    private fun setUpListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(TAG, "Fragment back pressed invoked, LOGOUT")
                    viewModel.backButtonSignOut()
                }
            })
    }

    //endregion
}