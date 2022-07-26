package com.example.janinfinum

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.janinfinum.ShowsActivity.Companion.DESC_ARG
import com.example.janinfinum.ShowsActivity.Companion.IMG_ARG
import com.example.janinfinum.ShowsActivity.Companion.TITLE_ARG
import com.example.janinfinum.databinding.ActivityShowDetailsBinding
import com.example.janinfinum.databinding.NewReviewLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.math.absoluteValue

class ShowDetailsActivity : Fragment() {

    private var _binding: ActivityShowDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ShowDetailsViewModel>()

    private lateinit var adapter: ReviewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString(TITLE_ARG)
        val desc = arguments?.getString(DESC_ARG)
        val img = arguments?.getInt(IMG_ARG)

        viewModel.setShowDetails(title!!, desc!!, img!!)

        binding.showDetailTitle.title = viewModel.title.value
        binding.showDetailDesc.text = viewModel.desc.value
        binding.showDetailImage.setImageResource(viewModel.img.value!!)

        viewModel.avg.observe(viewLifecycleOwner) {
            viewModel.averageRating(viewModel.reviews.value!!)
            binding.textViewReviews.text = resources.getString(R.string.reviewsExtra, it.absoluteValue, viewModel.reviews.value!!.size)
            binding.ratingBar.rating = it.absoluteValue
        }

        if (viewModel.reviews.value?.isEmpty()!!) {
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
                Toast.makeText(activity, "Please rate the show", Toast.LENGTH_SHORT).show()
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
        adapter = ReviewAdapter(viewModel.reviews) {

        }

        binding.recyclerVewReviews.layoutManager = LinearLayoutManager(activity)
        binding.recyclerVewReviews.adapter = adapter
    }

    private fun addReview(review: Review) {
        adapter.addItem(review)
        //viewModel.reviews.value!!.add(review)

        viewModel.avg.observe(viewLifecycleOwner) {
            viewModel.averageRating(viewModel.reviews.value!!)
            binding.textViewReviews.text = resources.getString(R.string.reviewsExtra, it.absoluteValue, viewModel.reviews.value!!.size)
            Log.d("TEST", it.absoluteValue.toString())
        }

        if (viewModel.reviews.value!!.isEmpty()) {
            binding.textViewReviews.text = resources.getString(R.string.reviews)
        }
        else if (viewModel.reviews.value!!.isNotEmpty()) {
            binding.recyclerVewReviews.isVisible = true
            binding.ratingBar.isVisible = true
            binding.emptyStateText.isVisible = false
        }

    }

    private fun updateRatings() {
        viewModel.avg.observe(viewLifecycleOwner) {
            viewModel.averageRating(viewModel.reviews.value!!)
            binding.ratingBar.rating = it.absoluteValue
        }
    }
}