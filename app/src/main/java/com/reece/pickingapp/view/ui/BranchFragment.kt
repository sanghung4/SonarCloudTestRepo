package com.reece.pickingapp.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.FragmentBranchBinding
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.viewmodel.BranchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class BranchFragment : Fragment() {
    private val TAG: String = "BranchFragment"
    private lateinit var binding: FragmentBranchBinding
    private val viewModel by viewModels<BranchViewModel>()

    @Inject
    lateinit var activityService: ActivityService

    //region Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setUp(navigationCallback = { navigateToOrders() })

        binding = FragmentBranchBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    //endregion

    //region Navigation

    private fun navigateToOrders() {
        activity?.invalidateOptionsMenu()
        activityService.navController?.navigate(R.id.action_global_start)
    }

    //endregion
}