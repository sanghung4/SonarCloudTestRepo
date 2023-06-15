package com.reece.pickingapp.view.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.reece.pickingapp.BarcodeScannerActivity
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.FragmentPickFromOtherLocationsBinding
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.viewmodel.PickFromOtherLocationsViewModel
import com.reece.pickingapp.viewmodel.ProductModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import androidx.lifecycle.Observer


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PickFromOtherLocationsFragment : Fragment() {
    private val TAG = "PickFromOtherLocationsFragment"
    private lateinit var binding: FragmentPickFromOtherLocationsBinding
    private val args: PickFromOtherLocationsFragmentArgs by navArgs()
    private val viewModel by viewModels<PickFromOtherLocationsViewModel>()
    @Inject
    lateinit var activityService: ActivityService

    //region Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setUp(
            product = args.product!!,
            lifecycleOwner = viewLifecycleOwner,
            navigationForStagingCallback = { navigationToOrderList()},
            navigationForScanBarCodeCallback = { scanBarcode() }
        )

        binding = FragmentPickFromOtherLocationsBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        //viewModel.linearLayout = binding.serialNumberLayout
        binding.product = args.product


        viewModel.qtyLabel.observe(viewLifecycleOwner, Observer{
            binding.qntyPicked.text = it
        })

        //block the UI to save the back order and show the progressbar
        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            when(it) {
                0 -> {
                    binding.progressBarCompletePick.visibility = View.GONE
                    binding.completePickButton.isEnabled = true

                }
                1 ->{ binding.progressBarCompletePick.visibility= View.VISIBLE
                    binding.completePickButton.isEnabled = false
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.altLocationsScroll.isNestedScrollingEnabled = false
        super.onViewCreated(view, savedInstanceState)

    }

    //endregion

    //region Listeners

    private fun navigationToOrderList() {
        findNavController().popBackStack()
    }

    private fun scanBarcode() {
       startActivityForResult(Intent(activity, BarcodeScannerActivity::class.java), 3)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val barcode = data?.getStringExtra("barcode")
            Log.d("Barcode", "Scanned Barcode::$barcode")
            barcode?.let {
                viewModel.setScannedBarcode(it)
            }
        }
    }
}