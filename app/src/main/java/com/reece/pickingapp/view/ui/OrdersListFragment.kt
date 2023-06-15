package com.reece.pickingapp.view.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.FragmentOrdersListBinding
import com.reece.pickingapp.models.OrderDTO
import com.reece.pickingapp.utils.extensions.onBackPressedAction
import com.reece.pickingapp.viewmodel.OrdersListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class OrdersListFragment : Fragment() {
    private val TAG = "OrdersListFragment"
    private lateinit var binding: FragmentOrdersListBinding
    private val viewModel by viewModels<OrdersListFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setUp(
            navigationForAuthScreenCallback = { navigateToAuthScreen() },
            navigationForOrderCallback = { order -> navigationForOrderCallback(order) }
        )

        binding = FragmentOrdersListBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setOnBackPressAction()

        return binding.root
    }

    private fun setOnBackPressAction() {
        onBackPressedAction {
            activity?.finish()
            true
        }
    }

    private fun navigateToAuthScreen() {
        Log.d(TAG, "Entro a la navigateToAuthScreen()")
        findNavController().navigate(R.id.action_global_auth_navigation)
    }

    private fun navigationForOrderCallback(order: OrderDTO) {
        findNavController().navigate(
            OrdersListFragmentDirections.navigateToProductsList(
                order
            )
        )
    }
}