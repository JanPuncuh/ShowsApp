package com.example.janinfinum

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.Window
import android.view.WindowManager
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

        var showPassword = false

        //hides the action bar
        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {

            /*val substring = binding.editTextEmailAddress.text.toString().substringBefore('@')

            val sendNameIntent = Intent(this, WelcomeActivity::class.java).apply {
                putExtra(EXTRA_USERNAME, substring)
            }
            startActivity(sendNameIntent)*/

            val intent = ShowsActivity.buildIntent(this)
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
                        binding.editTextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_24, 0)

                    }
                    //shows password
                    else {
                        binding.editTextPassword.transformationMethod = null; // another option show the password from the edit text
                        binding.editTextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off_24, 0)
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

    private fun validateLogin(email: String, password: String): Boolean {
        return emailValidate(email) && passwordValidate(password)
    }

    private fun enableLoginButton() {
        binding.loginButton.isEnabled = validateLogin(binding.editTextEmailAddress.text.toString(), binding.editTextPassword.text.toString())
    }

}