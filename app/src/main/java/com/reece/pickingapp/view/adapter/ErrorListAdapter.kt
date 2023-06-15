package com.reece.pickingapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.ErrorLogItemBinding
import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.viewmodel.ErrorLogViewHolderViewModel
import com.reece.pickingapp.viewmodel.ErrorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ErrorListAdapter(private val lifecycleOwner: LifecycleOwner) :
    ListAdapter<ErrorLogViewHolderViewModel, ErrorViewHolder>(ErrorDiffUtil()) {

    lateinit var productRecyclerView: RecyclerView
    private var TAG = "ErrorListAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ErrorViewHolder {
        val binding: ErrorLogItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.error_log_item,
            parent,
            false
        )

        binding.lifecycleOwner = lifecycleOwner
        return ErrorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ErrorViewHolder, position: Int) {
        val viewModel = getItem(position)
        viewModel.adapterPosition = position
        holder.binding.viewModel = viewModel
        holder.binding.product = ErrorViewModel( ErrorLogDTO( viewModel.errorLogs[position].errorName,viewModel.errorLogs[position].timestmap))
        viewModel.setUp()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        productRecyclerView = recyclerView
    }

}

//region ViewHolder

class ErrorViewHolder(val binding: ErrorLogItemBinding) : RecyclerView.ViewHolder(binding.root)

//endregion

//region DiffUtil

class ErrorDiffUtil : DiffUtil.ItemCallback<ErrorLogViewHolderViewModel>() {
    override fun areItemsTheSame(
        oldItem: ErrorLogViewHolderViewModel,
        newItem: ErrorLogViewHolderViewModel
    ): Boolean {
        return oldItem.errorLogs == newItem.errorLogs
    }

    override fun areContentsTheSame(
        oldItem: ErrorLogViewHolderViewModel,
        newItem: ErrorLogViewHolderViewModel
    ): Boolean {
        return oldItem.errorLogs.equals(newItem.errorLogs)
    }
}

//endregion
