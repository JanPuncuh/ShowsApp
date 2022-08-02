package com.example.janinfinum

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.janinfinum.databinding.ShowFrameLayoutBinding

class ShowsAdapter(
    private val items: List<Show>,
    private val onItemClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    inner class ShowViewHolder(private val binding: ShowFrameLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Show) {
            binding.showTitle.text = item.title
            binding.showImage.setImageResource(item.imageResourceId)

            binding.cardContainer.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding = ShowFrameLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return ShowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.count()
    }
}