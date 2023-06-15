package com.reece.pickingapp.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.PickFromOtherLocationCardBinding
import com.reece.pickingapp.viewmodel.PickFromOtherLocationsViewHolderViewModel
import kotlinx.coroutines.*

class PickFromOtherLocationsAdapter(private val lifecycleOwner: LifecycleOwner,
                                    private val onChangeQtyLabel: () -> Unit) :
    ListAdapter<PickFromOtherLocationsViewHolderViewModel, PickFromOtherLocationsViewHolder>(PickFromOtherLocationsDiffUtil()) {

    lateinit var pickFromOtherLocationsRecyclerView: RecyclerView
    private var TAG = "PickFromOtherLocationsAdapter"
    lateinit var holderMain: PickFromOtherLocationsViewHolder


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickFromOtherLocationsViewHolder {
        val binding: PickFromOtherLocationCardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.pick_from_other_location_card,
            parent,
            false
        )

        binding.lifecycleOwner = lifecycleOwner
        return PickFromOtherLocationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PickFromOtherLocationsViewHolder, position: Int) {
        val viewModel = getItem(position)
        viewModel.adapterPosition = position
        viewModel.setUp(holder.binding.serialNumberLayout)
        holder.binding.viewModel = viewModel
        this.holderMain = holder

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        pickFromOtherLocationsRecyclerView = recyclerView
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun removeItem(position:Int ) {
       var list = ArrayList<PickFromOtherLocationsViewHolderViewModel>()
        list.addAll(currentList)
        if (list != null) {
            list.remove(list[position])
            if (list.size > 0) list.last().deleteButtonVisibility = View.VISIBLE
            submitList(list)
        }
        this.notifyDataSetChanged()
        onChangeQtyLabel()
    }

    override fun onCurrentListChanged(
        previousList: MutableList<PickFromOtherLocationsViewHolderViewModel>,
        currentList: MutableList<PickFromOtherLocationsViewHolderViewModel>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        onChangeQtyLabel()
    }

    fun addItem(obj: PickFromOtherLocationsViewHolderViewModel) {
        var list = ArrayList<PickFromOtherLocationsViewHolderViewModel>()
        list.addAll(currentList)
        if (list.size>0){
            list.last().deleteButtonVisibility = View.GONE
        }
        if (obj != null) {
            list.add(obj)
            submitList(list)
        }
    }
}

//region ViewHolder

class PickFromOtherLocationsViewHolder(var binding: PickFromOtherLocationCardBinding) : RecyclerView.ViewHolder(binding.root)

//endregion

//region DiffUtil

class PickFromOtherLocationsDiffUtil : DiffUtil.ItemCallback<PickFromOtherLocationsViewHolderViewModel>() {
    override fun areItemsTheSame(
        oldItem: PickFromOtherLocationsViewHolderViewModel,
        newItem: PickFromOtherLocationsViewHolderViewModel
    ): Boolean {
        return oldItem.product.productId == newItem.product.productId
    }

    override fun areContentsTheSame(
        oldItem: PickFromOtherLocationsViewHolderViewModel,
        newItem: PickFromOtherLocationsViewHolderViewModel
    ): Boolean {
        return oldItem.product.equals(newItem.product)
    }
}