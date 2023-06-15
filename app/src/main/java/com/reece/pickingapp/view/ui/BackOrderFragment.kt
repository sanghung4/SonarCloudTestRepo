package com.reece.pickingapp.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.FragmentBackOrderBinding
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.viewmodel.BackOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BackOrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class BackOrderFragment : Fragment() {
    private lateinit var binding: FragmentBackOrderBinding
    private val viewModel by viewModels<BackOrderViewModel>()
    private val args: BackOrderFragmentArgs by navArgs()
    @Inject
    lateinit var activityService: ActivityService

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.setUp(
            lifecycleOwner = viewLifecycleOwner,
            navigationForStagingCallback = { navigationToOrderList() },
            selectedProduct = args.selectedProduct
        )
        binding = FragmentBackOrderBinding.inflate(inflater)
        binding.backOrderVM = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        //setup UI
        setupUI()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupUI() {
        val productSelected = viewModel.product.value
        if (productSelected != null) {
            //serial number tex setup
            binding.productNumberLabel.text=getString(R.string.pick_item_card_product_number,productSelected.productId)
            //product name tex set
            binding.productDescTxt.text = productSelected.description
//qty product set
            binding.qtyPickedValueLabel.text = getString(R.string.label_qty_picked_value,
                productSelected.qtyPicked ?:"0",
                productSelected.getProductDTO().quantity.toString())
            binding.buttonNoBackorder.setOnClickListener { navigationToOrderList() }

            //block the UI to save the back order and show the progressbar
            viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
                when(it) {
                    0 -> {
                        binding.progressBar.visibility= View.GONE
                        binding.buttonNoBackorder.isEnabled = true
                        binding.buttonYesBackorder.isEnabled = true
                    }
                    1 ->{ binding.progressBar.visibility= View.VISIBLE
                        binding.buttonNoBackorder.isEnabled = false
                        binding.buttonYesBackorder.isEnabled = false}
                }
            })
        }
    }
    private fun navigationToOrderList() {
        findNavController().popBackStack()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BackOrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BackOrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}