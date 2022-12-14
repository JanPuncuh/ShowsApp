package com.example.janinfinum

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.janinfinum.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    companion object {
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name:String = intent.getStringExtra(EXTRA_USERNAME).toString()

        binding.textViewWelcomeName.text = name
    }
}