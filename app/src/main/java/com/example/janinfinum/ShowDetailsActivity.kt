package com.example.janinfinum

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.janinfinum.databinding.ActivityShowDetailsBinding
import com.example.janinfinum.databinding.ActivityShowsBinding
import android.R
import android.view.View

import android.widget.TextView
import android.widget.Toolbar


class ShowDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowDetailsBinding

    companion object {

        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_DESC = "EXTRA_DESC"
        const val EXTRA_IMG = "EXTRA_IMG"

        fun buildIntent(activity: Activity): Intent {
            return Intent(activity, ShowDetailsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title:String = intent.getStringExtra(EXTRA_TITLE).toString()
        val desc = intent.getStringExtra(EXTRA_DESC).toString()
        val img = intent.getIntExtra(EXTRA_IMG, 0)

        binding.showDetailTitle.text = title
        binding.showDetailDesc.text = desc
        binding.showDetailImage.setImageResource(img)

    }
}