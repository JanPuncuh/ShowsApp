package com.example.janinfinum

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.janinfinum.databinding.ReviewLayoutBinding
import com.squareup.picasso.Picasso

class ReviewAdapter(
    private var items: ArrayList<Review2>,
    private val onItemClickCallback: () -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(private val binding: ReviewLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        //sets single review line
        fun bind(item: Review2) {
            binding.username.text = item.user.email
            Picasso.get().load(item.user.imageUrl)
                .placeholder(R.drawable.ic_profile_placeholder)
                .error(R.drawable.ic_profile_placeholder)
                .fit()
                .into(binding.userIcon)

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
        if (items?.get(position) is Review2) {
            holder.bind(items[position] as Review2)
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun addItem(review: Review2) {
        items.add(review)
        notifyItemInserted(items.lastIndex)
    }

    override fun getItemViewType(position: Int): Int {
        if (items.get(position) is Review2) {
            return 0
        }
        return 1
    }
}