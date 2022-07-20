package com.example.janinfinum

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.janinfinum.WelcomeActivity.Companion.EXTRA_USERNAME
import com.example.janinfinum.databinding.ActivityLoginBinding

class LoginActivity : Fragment() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
        private val emailRegex = Regex("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+\$")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onStart() {
        super.onStart()

        binding = ActivityLoginBinding.inflate(layoutInflater)

        binding.loginButton.setOnClickListener {
            val intent = ShowsActivity.buildIntent(activity!!)
            startActivity(intent)
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