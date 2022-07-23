package com.example.janinfinum

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.janinfinum.ShowDetailsActivity.Companion.EXTRA_DESC
import com.example.janinfinum.ShowDetailsActivity.Companion.EXTRA_IMG
import com.example.janinfinum.ShowDetailsActivity.Companion.EXTRA_TITLE
import com.example.janinfinum.databinding.ActivityShowsBinding

class ShowsActivity : AppCompatActivity() {

    private val shows = listOf(
        Show(
            "The Office",
            "The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.",
            R.drawable.ic_office
        ),
        Show("Stranger Things", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.ic_stranger_things),
        Show("Krv Nije Voda", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.krv_nije_voda_1),
    )

    private lateinit var binding: ActivityShowsBinding
    private lateinit var adapter: ShowsAdapter

    companion object {
        fun buildIntent(activity: Activity): Intent {
            return Intent(activity, ShowsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        //shows appear when user clicks on image
        binding.emptyStateImageBackground.setOnClickListener() {
            binding.recycleView.isVisible = !binding.recycleView.isVisible

            binding.emptyStateText.isVisible = !binding.emptyStateText.isVisible
            binding.emptyStateImageBackground.isVisible = !binding.emptyStateImageBackground.isVisible
            binding.emptyStateImageForeground.isVisible = !binding.emptyStateImageForeground.isVisible
            binding.showsText.isVisible = !binding.showsText.isVisible
        }

    }

    private fun initShowsRecycler() {
        //click on item in recycler view
        adapter = ShowsAdapter(shows) { show ->
            val title = show.title
            val desc = show.description
            val img = show.imageResourceId
            val intent = ShowDetailsActivity.buildIntent(this)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_DESC, desc)
            intent.putExtra(EXTRA_IMG, img)

            startActivity(intent)
        }

        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter

    }
}