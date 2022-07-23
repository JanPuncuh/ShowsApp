package com.example.janinfinum

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.janinfinum.WelcomeActivity.Companion.EXTRA_USERNAME
import com.example.janinfinum.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
        private val emailRegex = Regex("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+\$")
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //starts shows activity
        binding.loginButton.setOnClickListener {
            startActivity(ShowsActivity.buildIntent(this))
        }

        //checks validation of email
        binding.editTextEmailAddress.doAfterTextChanged {
            //if email isn't valid, shows error icon and error text
            if (!emailValidate(binding.editTextEmailAddress.text.toString())) {
                binding.editTextEmailAddress.error = getString(R.string.invalidEmailErrorMessage)
            }

            enableLoginButton()
        }

        //checks validation of password
        binding.editTextPassword.doAfterTextChanged {
            enableLoginButton()
        }

    }

    private fun emailValidate(email: String): Boolean {
        return email.isNotEmpty() && emailRegex.containsMatchIn(email)
    }

    private fun passwordValidate(password: String): Boolean {
        return password.length >= MIN_PASSWORD_LENGTH
    }

    private fun validateLogin(email: String, password: String): Boolean {
        return emailValidate(email) && passwordValidate(password)
    }

    private fun enableLoginButton() {
        binding.loginButton.isEnabled = validateLogin(binding.editTextEmailAddress.text.toString(), binding.editTextPassword.text.toString())
    }

}