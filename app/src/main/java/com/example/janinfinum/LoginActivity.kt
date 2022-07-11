package com.example.janinfinum

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.janinfinum.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
        val emailRegex = Regex("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+\$")
        private const val EXTRA_USERNAME = "EXTRA_USERNAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        var showPassword = false

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {

            val substring = binding.editTextEmailAddress.text.toString().substringBefore('@')

            val sendNameIntent = Intent(this, WelcomeActivity::class.java).apply {
                putExtra(EXTRA_USERNAME, substring)
            }
            startActivity(sendNameIntent)
        }

        //checks validation of email
        binding.editTextEmailAddress.doAfterTextChanged {
            //if email isn't valid, shows error icon and error text
            if (!emailValidate(binding.editTextEmailAddress.text.toString())) {
                binding.editTextEmailAddress.error = getString(R.string.invalidEmailErrorMessage)
            }

            //enables login button when email and password are valid
            setValidationListener()
        }


        //checks validation of password
        binding.editTextPassword.doAfterTextChanged {
            //enables login button when email and password are valid
            setValidationListener()
        }

        binding.editTextPassword.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.editTextPassword.right - binding.editTextPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    //your action here

                    //hides the password
                    if (showPassword) {
                        binding.editTextPassword.transformationMethod = PasswordTransformationMethod() //hide the password from the edit text

                    }
                    //shows password
                    else {
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
        return email.isNotEmpty() && emailRegex.containsMatchIn(email)
    }

    private fun passwordValidate(password: String): Boolean {
        return password.length >= MIN_PASSWORD_LENGTH
    }

    //preveri, če se email ujema z regexom in če je koda dolga vsaj 6 znakov
    private fun validateLogin(email: String, password: String): Boolean {
        return emailValidate(email) && passwordValidate(password)
    }

    private fun setValidationListener() {
        binding.loginButton.isEnabled = validateLogin(binding.editTextEmailAddress.text.toString(), binding.editTextPassword.text.toString())
    }

}