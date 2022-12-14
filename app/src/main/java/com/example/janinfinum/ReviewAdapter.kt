package com.example.janinfinum

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.janinfinum.databinding.ReviewLayoutBinding
import com.squareup.picasso.Picasso

class ReviewAdapter(
    private var items: ArrayList<Review>,
    private val onItemClickCallback: () -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(private val binding: ReviewLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        //sets single review line
        fun bind(item: Review) {
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
        val binding = ReviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun addItem(review: Review) {
        items.add(review)
        notifyItemInserted(items.lastIndex)
    }
}