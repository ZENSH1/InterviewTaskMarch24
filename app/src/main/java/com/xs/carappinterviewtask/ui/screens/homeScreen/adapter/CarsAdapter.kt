package com.xs.carappinterviewtask.ui.screens.homeScreen.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xs.carappinterviewtask.data.model.responses.GetCarsResponseItem
import com.xs.carappinterviewtask.databinding.CarItemLayoutBinding
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.isEmptyOrBlank
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.load
import com.xs.carappinterviewtask.utils.objects.SharedTransitions
import java.util.Locale

class CarsAdapter(
    private var carsList: MutableList<GetCarsResponseItem>,
    private val onItemClick: (GetCarsResponseItem,CarItemLayoutBinding) -> Unit
) : ListAdapter<GetCarsResponseItem, CarsAdapter.CarViewHolder>(DiffCallback) {

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
    private var searchQuery: String = ""

    inner class CarViewHolder(private val binding: CarItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(car: GetCarsResponseItem) {
            with(binding) {
                val imageAdapter = HorizontalImageAdapter(car.multipleImages)
                carImage.adapter = imageAdapter
                makeTv.text = car.make
                modelTv.text = car.model
                root.setOnClickListener {
                    onItemClick(car,binding) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = CarItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    fun filter(query: String) {
        searchQuery = query.trim()
        val lowercaseQuery = searchQuery.lowercase()
        val newFilteredList = if (lowercaseQuery.isEmpty()) {
            carsList
        } else {
            carsList.filter { it.model.lowercase().contains(lowercaseQuery) }
        }
        submitList(newFilteredList)
    }

    fun updateData(newCarsList: List<GetCarsResponseItem>) {
        carsList = newCarsList.toMutableList()
        submitList(carsList)
    }

}
