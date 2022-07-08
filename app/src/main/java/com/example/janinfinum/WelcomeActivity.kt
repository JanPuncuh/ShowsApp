package com.example.janinfinum

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.janinfinum.databinding.ActivityLoginBinding
import com.example.janinfinum.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name:String = intent.getStringExtra("NAME").toString()
        binding.textViewWelcomeName.text = name
    }
}