package com.example.janinfinum

import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.janinfinum.databinding.ActivityLoginBinding


lateinit var binding: ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.isEnabled = false
        binding.button.setOnClickListener {
        }

        //ob vsaki spremembi preveri veljavnost emaila
        binding.editTextEmailAddress.addTextChangedListener {
            if (!emailValidate(binding.editTextEmailAddress.text.toString())) {
                binding.editTextEmailAddress.error = "Invalid e-mail address"
            }

            binding.button.isEnabled =
                validateLogin(binding.editTextEmailAddress.text.toString(), binding.editTextPassword.text.toString())

        }

        //ob vsaki spremembi preveri veljavnost geslo
        binding.editTextPassword.addTextChangedListener {
            /*if (!passwordValidate(binding.editTextPassword.text.toString())) {
                binding.editTextPassword.error = "Must contain at least 6 characters"
            }*/

            binding.button.isEnabled =
                validateLogin(binding.editTextEmailAddress.text.toString(), binding.editTextPassword.text.toString())
        }
    }

    private fun emailValidate(email: String): Boolean {
        val emailRegex = Regex("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+\$")
        return email.isNotEmpty() && emailRegex.containsMatchIn(email)
    }

    private fun passwordValidate(password: String): Boolean {
        return password.length >= 6
    }

    //preveri, če se email ujema z regexom in če je koda dolga vsaj 6 znakov
    private fun validateLogin(email: String, password: String): Boolean {
        return emailValidate(email) && passwordValidate(password)
    }

}