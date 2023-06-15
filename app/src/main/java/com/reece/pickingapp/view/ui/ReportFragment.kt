package com.reece.pickingapp.view.ui

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.FragmentReportBinding
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.SnackBarType
import com.reece.pickingapp.utils.extensions.onBackPressedAction
import com.reece.pickingapp.view.state.SnackBarState
import com.reece.pickingapp.viewmodel.ReportFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ReportFragment : Fragment() {
    val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }
    private lateinit var binding: FragmentReportBinding
    private val viewModel by viewModels<ReportFragmentViewModel>()

    @Inject
    lateinit var activityService: ActivityService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setUp(
                    lifecycleOwner = viewLifecycleOwner,
            navigationForStagingCallback = { navigationToOrderList() }

        )
        binding = FragmentReportBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBackPressAction()
    }
    private fun navigationToOrderList() {
        findNavController().navigate(
            ReportFragmentDirections.actionProductListFragmentToOrdersListFragment()
        )
    }

    private fun setOnBackPressAction() {
        onBackPressedAction {
            navigationToOrderList()
        }
    }
}