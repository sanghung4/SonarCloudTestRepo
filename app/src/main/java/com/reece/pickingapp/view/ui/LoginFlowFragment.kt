package com.reece.pickingapp.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.FragmentLoginFlowBinding
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.extensions.onBackPressedAction
import com.reece.pickingapp.viewmodel.LoginFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginFlowFragment : Fragment() {
    private val TAG: String = "LoginFlowFragment"
    private lateinit var binding: FragmentLoginFlowBinding
    private val viewModel by viewModels<LoginFlowViewModel>()

    @Inject
    lateinit var activityService: ActivityService

    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setUp()

        binding = FragmentLoginFlowBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setUpNavConfiguration()

        return binding.root
    }

    private fun setUpNavConfiguration() {
        val nestedNavHostFragment =
            childFragmentManager.findFragmentById(R.id.nested_nav_host_login_fragment) as? NavHostFragment
        navController = nestedNavHostFragment?.navController
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            var stepCount = 0
            when (destination.id) {
                R.id.sign_in_fragment -> {
                    activityService.hideToolBarBackButton()
                    stepCount = 1
                }
                R.id.eclipse_login_fragment -> {
                    activityService.showToolBarBackButton()
                    stepCount = 2
                }
                R.id.branch_id_entry -> {
                    activityService.showToolBarBackButton()
                    stepCount = 3
                }
            }
            viewModel.setLoginIndicatorProgress(stepCount)
            viewModel.setLoginStepData(stepCount)
        }

        onBackPressedAction {
            val backStackEntryCount =
                nestedNavHostFragment?.childFragmentManager!!.backStackEntryCount
            if (backStackEntryCount <= 1) {
                requireActivity().finish()
            } else {
                navController?.navigateUp()
            }
        }
    }
}