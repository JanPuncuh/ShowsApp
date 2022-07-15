package com.example.janinfinum

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.janinfinum.databinding.ReviewLayoutBinding

class ReviewAdapter(
    private val items: List<Review>,
    private val onItemClickCallback: (Review) -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(private val binding: ReviewLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}