package com.example.janinfinum

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.janinfinum.databinding.ActivityLoginBinding
import android.view.MotionEvent
import android.view.View

import android.view.View.OnTouchListener
import android.text.method.PasswordTransformationMethod





lateinit var binding: ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var showPassword = false

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {

            // Create the text message with a string.
            val substring = binding.editTextEmailAddress.text.toString().substringBefore('@')

            val sendNameIntent = Intent(this, WelcomeActivity::class.java).apply {
                putExtra("NAME", substring)
            }
            startActivity(sendNameIntent)
        }

        //ob vsaki spremembi preveri veljavnost emaila
        binding.editTextEmailAddress.addTextChangedListener {
            if (!emailValidate(binding.editTextEmailAddress.text.toString())) {
                binding.editTextEmailAddress.error = "Invalid e-mail address"
            }

            //login button dela, če je pravilen mail in geslo
            binding.button.isEnabled = validateLogin(binding.editTextEmailAddress.text.toString(), binding.editTextPassword.text.toString())
        }

        //ob vsaki spremembi preveri veljavnost geslo
        binding.editTextPassword.addTextChangedListener {
            /*if (!passwordValidate(binding.editTextPassword.text.toString())) {
                binding.editTextPassword.error = "Must contain at least 6 characters"
            }*/

            //login button dela, če je pravilen mail in geslo
            binding.button.isEnabled = validateLogin(binding.editTextEmailAddress.text.toString(), binding.editTextPassword.text.toString())
        }

        binding.editTextPassword.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.editTextPassword.right - binding.editTextPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    //your action here

                    //geslo je prikazano, skrije ga
                    if (showPassword) {
                        Log.i("TEST", "test")
                        binding.editTextPassword.transformationMethod = PasswordTransformationMethod() //hide the password from the edit text

                    }
                    //geslo je skirto, pokaže ga
                    else {
                        Log.i("TEST", "test2")
                        binding.editTextPassword.transformationMethod = null; // another option show the password from the edit text
                    }
                    showPassword = !showPassword

                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })

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