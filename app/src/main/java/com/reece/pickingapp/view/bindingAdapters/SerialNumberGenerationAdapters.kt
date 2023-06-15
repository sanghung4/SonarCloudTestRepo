package com.reece.pickingapp.view.bindingAdapters

import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.DataEntryBinding
import com.reece.pickingapp.utils.DataEntryFieldType
import com.reece.pickingapp.view.state.InputFocusState
import com.reece.pickingapp.view.state.InputState
import com.reece.pickingapp.viewmodel.DataEntryViewModel
import com.reece.pickingapp.viewmodel.ProductViewHolderViewModel

object SerialNumberGenerationAdapters {
    @BindingAdapter("productCheckForSerial")
    @JvmStatic
    fun productCheckForSerial(layout: LinearLayout, viewModel: ProductViewHolderViewModel?) {
        layout.removeAllViews()
        viewModel?.let {
            if (viewModel.product.isSerial) {

                viewModel.setSerialHolderPos(layout.y.toInt())
                if (viewModel.productSerialNumberViewModels.value.isNullOrEmpty()) {
                    viewModel.productSerialNumberViewModels.value = mutableListOf()
                }

                for (i in 0 until viewModel.product.quantity) {
                    val binding: DataEntryBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(layout.context),
                        R.layout.data_entry,
                        layout,
                        false
                    )
                    binding.root.apply {
                        id = i
                        nextFocusDownId = i + 1
                    }
                    binding.lifecycleOwner = viewModel.lifecycleOwner
                    binding.dataEntryInterface = viewModel
                    binding.barcodeScanInterface = viewModel




                    //Check if SerialViewModel already exists
                    if (viewModel.productSerialNumberViewModels.value?.indices?.contains(i) == true) {
                        viewModel.productSerialNumberViewModels.value?.let { serialList ->
                            binding.dataEntryViewModel = serialList[i]
                        }
                    } else {
                        var dataEntryViewModel = DataEntryViewModel()
                        dataEntryViewModel.setDataEntryFieldType(DataEntryFieldType.SCANNABLE_SERIAL)
                        dataEntryViewModel.setInputState(InputState.Hidden())
                        var serialNum = i
                        serialNum++
                        dataEntryViewModel.setHintString(
                            layout.context.getString(
                                R.string.serial_entry_hint,
                                serialNum.toString()
                            )
                        )

                        if (i == 0) {
                            dataEntryViewModel.setInputState(InputState.Disabled())
                            dataEntryViewModel.setInputFocusState(InputFocusState.NonFocusable)
                        }

                        viewModel.productSerialNumberViewModels.value?.add(dataEntryViewModel)
                        binding.dataEntryViewModel = dataEntryViewModel
                    }
                    layout.addView(binding.root)
                }
            }
        }
    }
}