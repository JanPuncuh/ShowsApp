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
        Show("The Office", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.ic_office),
        Show("Stranger Things", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.ic_stranger_things),
        Show("Krv Nije Voda", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.krv_nije_voda_1),
    )

    //use this list for empty recyclerView testing
    //private val shows = emptyList<Show>()

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

        //hides the action bar
        supportActionBar?.hide()

        initShowsRecycler()

        if (shows.isNotEmpty()) {
            binding.emptyStateText.isVisible = false
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