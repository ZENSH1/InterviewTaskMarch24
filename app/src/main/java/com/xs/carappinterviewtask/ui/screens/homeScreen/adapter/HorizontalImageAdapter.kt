package com.xs.carappinterviewtask.ui.screens.homeScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xs.carappinterviewtask.databinding.ImageItemBinding

class HorizontalImageAdapter(
    private val imageList: List<String>
) : RecyclerView.Adapter<HorizontalImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(view: ImageItemBinding) : RecyclerView.ViewHolder(view.root) {
        val imageView: ImageView = view.carImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(holder.imageView.context)
            .load(imageList[position])
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = imageList.size
}
