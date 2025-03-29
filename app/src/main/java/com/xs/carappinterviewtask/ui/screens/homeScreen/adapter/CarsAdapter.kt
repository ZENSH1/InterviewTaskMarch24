package com.xs.carappinterviewtask.ui.screens.homeScreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xs.carappinterviewtask.R
import com.xs.carappinterviewtask.data.model.responses.GetCarsResponseItem
import com.xs.carappinterviewtask.databinding.CarItemLayoutBinding
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.gone
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.visible
import com.xs.carappinterviewtask.utils.objects.SharedPreferenceObject

class CarsAdapter(
    private val globalContext: Context,
    private var carsList: MutableList<GetCarsResponseItem>,
    private val onItemClick: (GetCarsResponseItem,CarItemLayoutBinding) -> Unit
) : ListAdapter<GetCarsResponseItem, CarsAdapter.CarViewHolder>(DiffCallback) {

    private

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<GetCarsResponseItem>() {
            override fun areItemsTheSame(oldItem: GetCarsResponseItem, newItem: GetCarsResponseItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GetCarsResponseItem, newItem: GetCarsResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
     var searchQuery: String = ""
    var showFav = false

    inner class CarViewHolder(private val binding: CarItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(car: GetCarsResponseItem, position: Int) {
            with(binding) {
                val isFav = SharedPreferenceObject.getFav(globalContext,car.id)
                val imageAdapter = HorizontalImageAdapter(car.multipleImages)
                carImage.adapter = imageAdapter
                makeTv.text = car.make
                modelTv.text = car.model
                priceTv.text = car.price.toString()
                favBtn.setImageResource(if (isFav) R.drawable.baseline_heart_broken_24 else R.drawable.baseline_hourglass_empty_24)
                root.setOnClickListener {
                    onItemClick(car,binding) }
                favBtn.setOnClickListener {
                    SharedPreferenceObject.setFav(
                        globalContext, car.id, isFav =! isFav)
                    filter(query = searchQuery)
                    notifyItemChanged(position)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = CarItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(getItem(position),position)
    }


    fun filter(query: String) {
        searchQuery = query.trim()
        val lowercaseQuery = searchQuery.lowercase()
        val newFilteredList = if (lowercaseQuery.isEmpty()) {
            carsList
        } else {
            carsList.filter { it.model.lowercase().contains(lowercaseQuery) }
        }
        submitList(
            if (showFav) {
                newFilteredList.filter { SharedPreferenceObject.getFav(globalContext,it.id) }
            }else newFilteredList)
    }

    fun updateData(newCarsList: List<GetCarsResponseItem>) {
        carsList = newCarsList.toMutableList()
        submitList(carsList)
    }

}
