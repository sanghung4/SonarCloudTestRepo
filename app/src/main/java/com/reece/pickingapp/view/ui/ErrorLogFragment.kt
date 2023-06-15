package com.reece.pickingapp.view.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.reece.pickingapp.BarcodeScannerActivity
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.FragmentErrorLogListBinding
import com.reece.pickingapp.databinding.FragmentProductsListBinding
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.SnackBarType
import com.reece.pickingapp.utils.extensions.onBackPressedAction
import com.reece.pickingapp.view.state.SnackBarState
import com.reece.pickingapp.viewmodel.ErrorLogFragmentViewModel
import com.reece.pickingapp.viewmodel.OrderViewModel
import com.reece.pickingapp.viewmodel.ProductsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ErrorLogFragment : Fragment() {
    private val TAG = "ErrorLogFragment"
    private lateinit var binding: FragmentErrorLogListBinding
    private val viewModel by viewModels<ErrorLogFragmentViewModel>()
    @Inject
    lateinit var activityService: ActivityService
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setUp(
            lifecycleOwner = viewLifecycleOwner)
        binding = FragmentErrorLogListBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view:View,savedInstanceState:Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        setOnBackPressAction()
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.showErrorData()
        },2000)
    }

    private fun setOnBackPressAction() {
        onBackPressedAction {
            findNavController().navigate(
                ProductsFragmentDirections.actionGlobalStart()
            )
        }
    }
}