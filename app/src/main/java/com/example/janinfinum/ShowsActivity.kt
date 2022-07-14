package com.example.janinfinum

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.janinfinum.databinding.ActivityShowsBinding

class ShowsActivity : AppCompatActivity() {

    private val shows = listOf(
        Show("The Office", R.drawable.ic_office),
        Show("Stranger Things", R.drawable.ic_stranger_things),
        Show("Krv Nije Voda", R.drawable.krv_nije_voda_1),
    )

    //private val shows = emptyList<Show>()

    private lateinit var binding: ActivityShowsBinding
    private lateinit var adapter: ShowsAdapter

    companion object {
        fun buildIntent(activity: Activity): Intent {
            return Intent(activity, ShowsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        //val shows = listOf()

        super.onCreate(savedInstanceState)

        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initShowsRecycler()

        if (shows.isNotEmpty()) {
            binding.emptyStateText.isVisible = false
        }

    }

    private fun initShowsRecycler() {
        //click on item
        adapter = ShowsAdapter(shows) { show ->
            Toast.makeText(this, show.name, Toast.LENGTH_SHORT).show()
        }

        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter

    }
}