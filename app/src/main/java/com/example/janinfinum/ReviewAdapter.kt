package com.example.janinfinum

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.janinfinum.databinding.ReviewLayoutBinding

class ReviewAdapter(
    private var items: List<DetailsItem>,
    private val onItemClickCallback: () -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(private val binding: ReviewLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        //sets single review line
        fun bind(item: Review) {
            binding.username.text = item.username
            binding.userIcon.setImageResource(item.profilePicture)
            binding.reviewComment.text = item.comment
            binding.textViewRating.text = item.rating.toString()

            binding.cardContainer.setOnClickListener {
                onItemClickCallback()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        if (viewType == 0) {
            val binding = ReviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ReviewViewHolder(binding)
        }
        else if (viewType == 1) {
            val binding = ReviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ReviewViewHolder(binding)
            //todo tk ko pr viewtype == 0
        }
        val binding = ReviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        if (items[position] is Review) {
            holder.bind(items[position] as Review)
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun addItem(review: Review) {
        items = items + review
        notifyItemInserted(items.lastIndex)
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position] is Review) {
            return 0
        }
        return 1
    }
}