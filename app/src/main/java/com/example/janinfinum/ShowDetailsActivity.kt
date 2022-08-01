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
import com.example.janinfinum.ShowsActivity.Companion.ID
import com.example.janinfinum.databinding.ActivityShowDetailsBinding
import com.example.janinfinum.databinding.NewReviewLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response
import kotlin.math.absoluteValue

class ShowDetailsActivity : Fragment() {

    private lateinit var show: Show2
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

        val showId = arguments?.getString(ID).toString()
        Log.d("TEST", showId)

        app = activity?.application as MyApplication

        ApiModule.initRetrofit(requireActivity())

        binding.button.setOnClickListener() {
            showWriteNewReviewDialog()
        }

        if (app.isOnline(requireContext())) {
            //gets show's details
            showLoadingState()

            ApiModule.retrofit.getShow(showId, "Bearer", app.token!!, app.client!!, app.uid!!)
                .enqueue(object : retrofit2.Callback<ShowDetailsResponse> {
                    override fun onResponse(call: Call<ShowDetailsResponse>, response: Response<ShowDetailsResponse>) {

                        if (response.isSuccessful) {

                            show = response.body()?.show2!!

                            setShowsDetails(show)

                            //fetch reviews
                            ApiModule.retrofit.getReviews(showId, "Bearer", app.token!!, app.client!!, app.uid!!)
                                .enqueue(object : retrofit2.Callback<ReviewResponse> {
                                    override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {

                                        if (response.isSuccessful) {
                                            //saves all reviews from this show
                                            app.database.reviewDao().insertAllReviewsFromShow(response.body()?.reviews!!)

                                            viewModel.onResponseAPI(response.body()?.reviews)
                                            viewModel.reviews.observe(viewLifecycleOwner) {
                                                initReviewRecycler(viewModel.reviews.value!!)
                                            }

                                            setEmptyOrNormalState(response.body()?.reviews!!)

                                        }
                                        else {
                                            Log.d("TEST", "respone not successful")
                                        }

                                    }

                                    override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                                        Log.d("TEST", "${t.message.toString()}\n${t.printStackTrace()}")
                                        //todo display to user
                                        Toast.makeText(requireActivity(), "Failed to load reviews", Toast.LENGTH_SHORT).show()
                                    }
                                })
                        }
                    }

                    override fun onFailure(call: Call<ShowDetailsResponse>, t: Throwable) {
                        //todo handle failure
                    }
                })
        }
        //no connection
        else {
            showLoadingState()

            //gets show from DB
            viewModel.getShowFromDatabase(showId).observe(viewLifecycleOwner) { show ->
                setShowsDetails(show)
                showNormalState()

                //gets reviews from that show
                viewModel.getReviewsFromDatabase(showId).observe(viewLifecycleOwner) { reviews ->
                    initReviewRecycler(reviews as ArrayList<Review2>)
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

    private fun setShowsDetails(show: Show2) {
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

    private fun setEmptyOrNormalState(list: ArrayList<Review2>) {
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

            //review
            val reviewRequest = ReviewRequest(rating.toInt(), reviewComment, show.id.toInt())
            addReview(reviewRequest)

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initReviewRecycler(list: ArrayList<Review2>) {
        //click on item in recycler view
        adapter = ReviewAdapter(list) {

        }

        binding.recyclerVewReviews.layoutManager = LinearLayoutManager(activity)
        binding.recyclerVewReviews.adapter = adapter
    }

    private fun addReview(reviewRequest: ReviewRequest) {
        ApiModule.retrofit.postReview("Bearer", app.token!!, app.client!!, app.uid!!, reviewRequest)
            .enqueue(object : retrofit2.Callback<AddedReviewResponse> {
                override fun onResponse(call: Call<AddedReviewResponse>, response: Response<AddedReviewResponse>) {

                    if (response.isSuccessful) {
                        val review = response.body()?.review

                        //todo viewmodel
                        if (review != null) {
                            adapter.addItem(review)
                            app.database.reviewDao().insertNewReview(review)
                        }
                    }
                }

                override fun onFailure(call: Call<AddedReviewResponse>, t: Throwable) {
                    //todo handle failure
                }
            })
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