package com.example.janinfinum

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.janinfinum.databinding.ActivityShowsBinding

class ShowsActivity : Fragment() {

    private val shows = listOf(
        Show(
            "The Office",
            "The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.",
            R.drawable.ic_office
        ),
        Show("Stranger Things", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.ic_stranger_things),
        Show("Krv Nije Voda", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.krv_nije_voda_1),
    )

    private var _binding: ActivityShowsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ShowsAdapter

    companion object {
        fun buildIntent(activity: Activity): Intent {
            return Intent(activity, ShowsActivity::class.java)
        }

        const val TITLE_ARG = "TITLE_ARG"
        const val DESC_ARG = "DESC_ARG"
        const val IMG_ARG = "IMG_ARG"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initShowsRecycler()

        if (shows.isNotEmpty()) {
            binding.emptyStateText.isVisible = false
            binding.emptyStateImageBackground.isVisible = false
            binding.emptyStateImageForeground.isVisible = false
            binding.showsText.isVisible = false
        }

        //makes it as there are no shows
        binding.recycleView.isVisible = false
        binding.emptyStateText.isVisible = true
        binding.emptyStateImageBackground.isVisible = true
        binding.emptyStateImageForeground.isVisible = true
        binding.showsText.isVisible = false
        binding.imageLogout.isVisible = false

        //shows appear when user clicks on image
        binding.emptyStateImageBackground.setOnClickListener() {
            binding.recycleView.isVisible = !binding.recycleView.isVisible
            binding.emptyStateText.isVisible = !binding.emptyStateText.isVisible
            binding.emptyStateImageBackground.isVisible = !binding.emptyStateImageBackground.isVisible
            binding.emptyStateImageForeground.isVisible = !binding.emptyStateImageForeground.isVisible
            binding.showsText.isVisible = !binding.showsText.isVisible
            binding.imageLogout.isVisible = !binding.imageLogout.isVisible
        }

        binding.imageLogout.setOnClickListener {
            findNavController().navigate(R.id.action_showsActivity_to_loginActivity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initShowsRecycler() {
        //click on item in recycler view
        adapter = ShowsAdapter(shows) { show ->
            val title = show.title
            val desc = show.description
            val img = show.imageResourceId

            findNavController().navigate(
                R.id.action_showsActivity_to_showDetailsActivity,
                bundleOf(TITLE_ARG to title, DESC_ARG to desc, IMG_ARG to img)
            )
        }

        binding.recycleView.layoutManager = LinearLayoutManager(activity)
        binding.recycleView.adapter = adapter

    }
}