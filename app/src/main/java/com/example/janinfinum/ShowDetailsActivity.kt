package com.example.janinfinum

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.janinfinum.databinding.ActivityShowDetailsBinding
import com.example.janinfinum.databinding.ActivityShowsBinding
import com.example.janinfinum.databinding.NewReviewLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class ShowDetailsActivity : Fragment() {

    private var _binding: ActivityShowDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ReviewAdapter

    private var reviews = mutableListOf<DetailsItem>()

    companion object {

        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_DESC = "EXTRA_DESC"
        const val EXTRA_IMG = "EXTRA_IMG"

        fun buildIntent(activity: Activity): Intent {
            return Intent(activity, ShowDetailsActivity::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title: String = "intent.getStringExtra(EXTRA_TITLE).toString()"
        val desc = "intent.getStringExtra(EXTRA_DESC).toString()"
        //val img = intent.getIntExtra(EXTRA_IMG, 0)
        val img = 0

        binding.showDetailTitle.title = title
        binding.showDetailDesc.text = desc
        binding.showDetailImage.setImageResource(img)

        binding.textViewReviews.text = resources.getString(R.string.reviewsExtra, averageRating(reviews), reviews.size)
        binding.ratingBar.rating = averageRating(reviews)

        if (reviews.isEmpty()) {
            binding.recyclerVewReviews.isVisible = false
            binding.ratingBar.isVisible = false
            binding.textViewReviews.text = resources.getString(R.string.reviews)
        }

        initReviewRecycler()

        binding.button.setOnClickListener() {
            showWriteNewReviewDialog()
        }

    }

    private fun showWriteNewReviewDialog() {
        val dialog = BottomSheetDialog(requireActivity())

        val bottomSheetBinding = NewReviewLayoutBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.newReviewButton.setOnClickListener {

            val reviewComment = bottomSheetBinding.newReviewEditText.text.toString()
            val rating = bottomSheetBinding.newReviewRatingBar.rating

            //if unrated
            if (bottomSheetBinding.newReviewRatingBar.rating == 0F) {
                //notifies the user, doesn't add to list
                //Toast.makeText(this, "Please rate the show", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newReview = Review("addedReview", reviewComment, rating, R.drawable.ic_profile_placeholder)
            addReview(newReview)
            updateRatings()

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initReviewRecycler() {
        //click on item in recycler view
        adapter = ReviewAdapter(reviews) {

        }

        binding.recyclerVewReviews.layoutManager = LinearLayoutManager(activity)
        binding.recyclerVewReviews.adapter = adapter
    }

    private fun addReview(review: Review) {
        adapter.addItem(review)
        reviews.add(review)
        binding.textViewReviews.text = resources.getString(R.string.reviewsExtra, averageRating(reviews), reviews.size)

        if (reviews.isEmpty()) {
            binding.textViewReviews.text = resources.getString(R.string.reviews)
        }

        if (reviews.isNotEmpty()) {
            binding.recyclerVewReviews.isVisible = true
            binding.ratingBar.isVisible = true
            binding.emptyStateText.isVisible = false
        }

    }

    private fun averageRating(list: List<DetailsItem>): Float {
        var rating = 0F
        list.forEach {
            if (it is Review) {
                rating += it.rating
            }
        }
        rating /= list.count()
        return rating
    }

    private fun updateRatings() {
        binding.ratingBar.rating = averageRating(reviews)
    }
}