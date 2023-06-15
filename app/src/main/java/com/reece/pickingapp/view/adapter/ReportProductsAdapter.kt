package com.reece.pickingapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.ItemReportProductBinding
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.emptyString
import com.reece.pickingapp.viewmodel.ProductModel
import com.reece.pickingapp.viewmodel.ReportProductViewHolderViewModel

class ReportProductsAdapter (private val activityS: ActivityService):
    ListAdapter<ReportProductViewHolderViewModel, ReportProductsAdapter.ReportViewHolder>(
        ReportDiffUtil()) {

    lateinit var reportRecyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding: ItemReportProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_report_product,
            parent,
            false
        )
        return ReportViewHolder(binding)
    }



    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val viewModel = getItem(position)
        holder.binding.viewModel = viewModel
        viewModel.setUp(activityS)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        reportRecyclerView = recyclerView
    }

    class ReportViewHolder(val binding: ItemReportProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ReportDiffUtil : DiffUtil.ItemCallback<ReportProductViewHolderViewModel>() {
        override fun areItemsTheSame(
            oldItem: ReportProductViewHolderViewModel,
            newItem: ReportProductViewHolderViewModel
        ): Boolean {
            return oldItem.product == newItem.product
        }

        override fun areContentsTheSame(
            oldItem: ReportProductViewHolderViewModel,
            newItem: ReportProductViewHolderViewModel
        ): Boolean {
            return oldItem.product == newItem.product
        }
    }
}