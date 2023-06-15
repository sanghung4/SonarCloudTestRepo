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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.reece.pickingapp.BarcodeScannerActivity
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.FragmentProductsListBinding
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.SnackBarType
import com.reece.pickingapp.utils.extensions.onBackPressedAction
import com.reece.pickingapp.view.state.SnackBarState
import com.reece.pickingapp.viewmodel.OrderViewModel
import com.reece.pickingapp.viewmodel.ProductModel
import com.reece.pickingapp.viewmodel.ProductsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProductsFragment : Fragment() {
    private val TAG = "ProductsFragment"
    private lateinit var binding: FragmentProductsListBinding
    private val args: ProductsFragmentArgs by navArgs()
    private val viewModel by viewModels<ProductsFragmentViewModel>()

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
            selectedOrder = OrderViewModel(args.selectedOrder),
            lifecycleOwner = viewLifecycleOwner,
            navigationForStagingCallback = { navigationToOrderList() },
            navigationForScanBarCodeCallback = { scanBarcode() },
            navigateForReportFragment = { navigationForReport() },
            showDialogSplitQty = { product,position -> showQtyDialog(product,position) }
        )

        binding = FragmentProductsListBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: Remove when Warning is invalid
        view.findViewById<View>(R.id.staging_location_warning)
            .findViewById<TextView>(R.id.staging_location_warning_textview).text =
            Html.fromHtml(getString(R.string.staging_order_warning), Html.FROM_HTML_MODE_LEGACY)
        setOnBackPressAction()
    }


    private fun setOnBackPressAction() {
        onBackPressedAction {
            if (viewModel.locationFormVisible.value == VISIBLE) {
                activityService.showMessage(
                    SnackBarState(
                        SnackBarType.ERROR,
                        activityService.getString(R.string.force_completion_staging)
                    ),
                    Snackbar.LENGTH_SHORT
                )
            } else {
                findNavController().navigate(
                    ProductsFragmentDirections.actionGlobalStart()
                )
            }
        }
    }

    //endregion

    //region Navigation

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
    //endregion

    private fun showQtyDialog(product:ProductModel,position: Int?) {

        val dialog = activityService.activity?.let { BottomSheetDialog(it) }
        val view =
            activityService.activity?.layoutInflater?.inflate(R.layout.bottom_sheet_dialog, null)
        val pickfromTxt = view?.findViewById<MaterialButton>(R.id.pick_from_other_locations_button)
        pickfromTxt?.setOnClickListener {
            //call other locations view
            dialog?.dismiss()
            navigationForOtherLocations(product)
        }
        val backOrderTxt = view?.findViewById<MaterialButton>(R.id.back_order_button)
        backOrderTxt?.setOnClickListener {
            //I will call the view back order
            dialog?.dismiss()
            navigationForBackOrder(product)
        }
        val postPoneTxt = view?.findViewById<MaterialButton>(R.id.postpone_pick_button)
        postPoneTxt?.setOnClickListener {
            dialog?.dismiss()
            position?.let {
                viewModel.productsAdapter.value?.moveItemToLast(position)
                Handler(Looper.getMainLooper()).postDelayed({ viewModel.productsAdapter.value?.recyclerViewScrollToTop() }, 500)
                activityService.showProductMovedMessage(Snackbar.LENGTH_SHORT)

            }

        }
        dialog?.setCancelable(true)
        dialog?.setContentView(view!!)
        dialog?.show()
    }

    private fun navigationForBackOrder(product: ProductModel) {
        findNavController().navigate(
            ProductsFragmentDirections.actionProductListFragmentToBackOrderFragment(product)
        )
    }
    private fun navigationForOtherLocations(product: ProductModel) {
        findNavController().navigate(
            ProductsFragmentDirections.actionPickFromOtherLocationsFragment(product)
        )
    }
    private fun navigationForReport() {
        findNavController().navigate(
            ProductsFragmentDirections.actionProductListFragmentToReportFragment()
        )
    }
}