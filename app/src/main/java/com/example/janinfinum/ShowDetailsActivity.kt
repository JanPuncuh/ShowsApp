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

        ApiModule.initRetrofit(requireActivity())

        /*viewModel.averageRating(viewModel.reviews.value!!)
        viewModel.avg.observe(viewLifecycleOwner) {
            binding.textViewReviews.text = resources.getString(R.string.reviewsExtra, it.absoluteValue, viewModel.reviews.value!!.size)
            binding.ratingBar.rating = it.absoluteValue
        }*/

        binding.button.setOnClickListener() {
            showWriteNewReviewDialog()
        }

        //todo api
        ApiModule.retrofit.getShow(id, "Bearer", app.token!!, app.client!!, app.uid!!)
            .enqueue(object : retrofit2.Callback<ShowDetailsResponse> {
                override fun onResponse(call: Call<ShowDetailsResponse>, response: Response<ShowDetailsResponse>) {

                    if (response.isSuccessful) {
                        val show = response.body()?.show2!!

                        viewModel.setShowDetails(show.title, show.description!!, show.imageUrl)

                        binding.showDetailTitle.title = show.title
                        binding.showDetailDesc.text = show.description
                        Picasso.get().load(show.imageUrl).into(binding.showDetailImage)
                        binding.ratingBar.rating = show.averageRating!!

                        /*viewModel.onResponseAPI(response.body()?.shows!!)
                        viewModel.shows2.observe(viewLifecycleOwner) {
                            initShowsRecycler()
                        }*/

                        ApiModule.retrofit.getReviews(id, "Bearer", app.token!!, app.client!!, app.uid!!)
                            .enqueue(object : retrofit2.Callback<ReviewResponse> {
                                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {

                                    Toast.makeText(requireActivity(), "test", Toast.LENGTH_SHORT).show()

                                    if (response.isSuccessful) {

                                        viewModel.onResponseAPI(response.body()?.reviews)
                                        viewModel.reviews.observe(viewLifecycleOwner) {
                                            initReviewRecycler()
                                        }

                                        if (viewModel.reviews.value?.isEmpty()!!) {
                                            binding.recyclerVewReviews.isVisible = false
                                            binding.ratingBar.isVisible = false
                                            binding.textViewReviews.text = resources.getString(R.string.reviews)
                                        }
                                    }
                                    else {
                                        Log.d("TEST", "respone not successful")
                                    }

                                }

                                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                                    Log.d("TEST", "${t.message.toString()}\n${t.printStackTrace()}")
                                }
                            })
                    }
                }

                override fun onFailure(call: Call<ShowDetailsResponse>, t: Throwable) {
                    //todo handle failure
                }

            })
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

            //val newReview = Review("addedReview", reviewComment, rating, R.drawable.ic_profile_placeholder)
            //addReview(newReview)
            //updateRatings()

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

    private fun addReview(review: Review2) {
        adapter.addItem(review)
        //viewModel.reviews.value!!.add(review)

        viewModel.averageRating(viewModel.reviews.value!!)

        viewModel.avg.observe(viewLifecycleOwner) {
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
        viewModel.averageRating(viewModel.reviews.value!!)
        viewModel.avg.observe(viewLifecycleOwner) {
            binding.ratingBar.rating = it.absoluteValue
        }
    }
}