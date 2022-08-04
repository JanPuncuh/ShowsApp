package com.example.janinfinum

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
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
import retrofit2.Call
import retrofit2.Response
import kotlin.math.absoluteValue

class ShowFragment : Fragment() {

    private lateinit var show: Show2
    private var _binding: ActivityShowDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ShowDetailsViewModel>()

    private lateinit var adapter: ReviewAdapter
    private lateinit var app: MyApplication

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString(ID).toString()

        app = activity?.application as MyApplication

        showLoadingState()

        ApiModule.initRetrofit(requireActivity())


        binding.button.setOnClickListener() {
            showWriteNewReviewDialog()
        }

        //gets show's details
        ApiModule.retrofit.getShow(id, "Bearer", app.token!!, app.client!!, app.uid!!)
            .enqueue(object : retrofit2.Callback<ShowDetailsResponse> {
                override fun onResponse(call: Call<ShowDetailsResponse>, response: Response<ShowDetailsResponse>) {

                    if (response.isSuccessful) {

                        show = response.body()?.show2!!

                        viewModel.setShowDetails(show.title, show.description!!, show.imageUrl)

                        binding.showDetailTitle.title = show.title
                        binding.showDetailDesc.text = show.description
                        Picasso.get().load(show.imageUrl).into(binding.showDetailImage)
                        binding.ratingBar.rating = show.averageRating!!

                        //fetch reviews
                        ApiModule.retrofit.getReviews(id, "Bearer", app.token!!, app.client!!, app.uid!!)
                            .enqueue(object : retrofit2.Callback<ReviewResponse> {
                                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {

                                    if (response.isSuccessful) {

                                        viewModel.onResponseAPI(response.body()?.reviews)
                                        viewModel.reviews.observe(viewLifecycleOwner) {
                                            initReviewRecycler()
                                        }

                                        viewModel.averageRating(viewModel.reviews.value!!)
                                        viewModel.avg.observe(viewLifecycleOwner) {
                                            binding.textViewReviews.text =
                                                resources.getString(
                                                    R.string.reviewsExtra,
                                                    it.absoluteValue,
                                                    viewModel.reviews.value!!.size
                                                )
                                            binding.ratingBar.rating = it.absoluteValue
                                        }

                                        if (viewModel.reviews.value?.isEmpty()!!) {
                                            binding.recyclerVewReviews.isVisible = false
                                            binding.ratingBar.isVisible = false
                                            binding.textViewReviews.text = resources.getString(R.string.reviews)
                                        }

                                        if (viewModel.reviews.value?.isNotEmpty()!!) {
                                            binding.emptyStateText.isVisible = false
                                        }

                                        showNormalState()

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

        binding.loadingStateGif.isVisible = false
        binding.loadingTextState.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            activity?.finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
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

            //review
            val reviewRequest = ReviewRequest(rating.toInt(), reviewComment, show.id.toInt())
            addReview2(reviewRequest)

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initReviewRecycler() {
        //click on item in recycler view
        adapter = ReviewAdapter(viewModel.reviews.value!!) {

        }

        binding.recyclerVewReviews.layoutManager = LinearLayoutManager(activity)
        binding.recyclerVewReviews.adapter = adapter

    }

    private fun addReview2(reviewRequest: ReviewRequest) {
        ApiModule.retrofit.postReview("Bearer", app.token!!, app.client!!, app.uid!!, reviewRequest)
            .enqueue(object : retrofit2.Callback<AddedReviewResponse> {
                override fun onResponse(call: Call<AddedReviewResponse>, response: Response<AddedReviewResponse>) {

                    val review = response.body()?.review

                    if (review != null) {
                        adapter.addItem(review)
                    }
                    Log.d("TEST", (review == null).toString())

                }

                override fun onFailure(call: Call<AddedReviewResponse>, t: Throwable) {

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