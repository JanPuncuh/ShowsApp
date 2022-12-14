package com.example.janinfinum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.janinfinum.ShowsFragment.Companion.ID
import com.example.janinfinum.databinding.ActivityShowDetailsBinding
import com.example.janinfinum.databinding.NewReviewLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import kotlin.math.absoluteValue

class ShowsDetailsFragment : Fragment() {

    private lateinit var selectedShow: Show
    private lateinit var showId: String
    private var _binding: ActivityShowDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShowDetailsViewModel by viewModels {
        ShowDetailsViewModelFactory(app.database)
    }

    private lateinit var adapter: ReviewAdapter
    private lateinit var app: MyApplication

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showId = arguments?.getString(ID).toString()

        app = activity?.application as MyApplication

        binding.button.setOnClickListener() {
            if (app.isOnline(requireContext())) {
                showWriteNewReviewDialog()
            }
            else Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
        }

        if (app.isOnline(requireContext())) {
            //gets show's details
            showLoadingState()

            viewModel.getShow(showId, app)
            viewModel.show.observe(viewLifecycleOwner) { show ->
                if (show != null) {
                    setShowsDetails(show)

                    selectedShow = show

                    viewModel.fetchReview(showId, app)
                    viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
                        if (reviews != null) {
                            app.database.reviewDao().insertAllReviewsFromShow(reviews)
                            initReviewRecycler(reviews)
                            setEmptyOrNormalState(reviews)
                        }
                        else {
                            Toast.makeText(requireContext(), "Error getting reviews", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        //no connection
        else {
            showLoadingState()

            //gets show from DB
            viewModel.getShowFromDatabase(showId).observe(viewLifecycleOwner) { show ->
                setShowsDetails(show)
                showNormalState()

                selectedShow = show

                //gets reviews from that show
                viewModel.getReviewsFromDatabase(showId).observe(viewLifecycleOwner) { reviews ->
                    initReviewRecycler(reviews as ArrayList<Review>)
                    if (reviews.isNullOrEmpty()) {
                        showEmptyState()
                    }
                    else if (reviews.isNotEmpty()) {
                        showNormalState()
                    }
                }
            }
        }
    }

    private fun setShowsDetails(show: Show) {
        binding.showDetailTitle.title = show.title
        binding.showDetailDesc.text = show.description
        Picasso.get().load(show.imageUrl).into(binding.showDetailImage)
        binding.ratingBar.rating = show.averageRating!!
        binding.textViewReviews.text = resources.getString(R.string.reviewsExtra, show.averageRating, show.no_of_reviews)

    }

    private fun showLoadingState() {
        binding.recyclerVewReviews.isVisible = false
        binding.emptyStateText.isVisible = false
        binding.textViewReviews.isVisible = false
        binding.ratingBar.isVisible = false
        binding.button.isVisible = false
        binding.showDetailDesc.isVisible = false
        binding.showDetailImage.isVisible = false
        binding.showDetailTitle.isVisible = false
    }

    private fun showNormalState() {
        binding.recyclerVewReviews.isVisible = true
        binding.emptyStateText.isVisible = true
        binding.textViewReviews.isVisible = true
        binding.ratingBar.isVisible = true
        binding.button.isVisible = true
        binding.emptyStateText.isVisible = false
        binding.showDetailDesc.isVisible = true
        binding.showDetailImage.isVisible = true
        binding.showDetailTitle.isVisible = true
        binding.emptyStateText.isVisible = false
        binding.loadingStateGif.isVisible = false
        binding.loadingTextState.isVisible = false
    }

    private fun showEmptyState() {
        with(binding) {
            emptyStateText.isVisible = true
            ratingBar.isVisible = false
            binding.textViewReviews.setText(R.string.reviews)
            loadingTextState.isVisible = false
            loadingStateGif.isVisible = false
        }
    }

    private fun setEmptyOrNormalState(list: ArrayList<Review>) {
        if (list.isEmpty()) {
            showEmptyState()
        }
        else if (list.isNotEmpty()) {
            showNormalState()
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
                Toast.makeText(activity, getString(R.string.unrated_review_toast_message), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (app.isOnline(requireContext())) {
                val reviewRequest = ReviewRequest(rating.toInt(), reviewComment, selectedShow.id.toInt())
                addReview(reviewRequest)
            }

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initReviewRecycler(list: ArrayList<Review>) {
        //click on item in recycler view
        adapter = ReviewAdapter(list) {

        }

        binding.recyclerVewReviews.layoutManager = LinearLayoutManager(activity)
        binding.recyclerVewReviews.adapter = adapter
    }

    private fun addReview(reviewRequest: ReviewRequest) {
        viewModel.postReview(reviewRequest, app)
        viewModel.postedReview.observe(viewLifecycleOwner) { postedReview ->
            if (postedReview != null) {
                adapter.addItem(postedReview)
                app.database.reviewDao().insertNewReview(postedReview)
            }
        }
        updateRatings()
    }

    private fun updateRatings() {
        viewModel.averageRating(viewModel.reviews.value!!)
        viewModel.avg.observe(viewLifecycleOwner) {
            binding.textViewReviews.text = resources.getString(R.string.reviewsExtra, it.absoluteValue, viewModel.reviews.value!!.size)
            binding.ratingBar.rating = it.absoluteValue
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
}