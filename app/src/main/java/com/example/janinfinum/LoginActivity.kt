package com.example.janinfinum

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.janinfinum.databinding.ActivityLoginBinding
import android.content.SharedPreferences
import androidx.core.content.edit


class LoginActivity : Fragment() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    companion object {
        private val REMEMBER_ME = "REMEMBER_ME"
        private const val MIN_PASSWORD_LENGTH = 6
        private val emailRegex = Regex("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+\$")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //return super.onCreateView(inflater, container, savedInstanceState)
        _binding = ActivityLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = this.requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)

        //if remember me, skip login
        if (preferences.getBoolean(REMEMBER_ME, false)) {
            findNavController().navigate(R.id.action_loginActivity_to_showsActivity)
        }

        binding.checkBox.isChecked = preferences.getBoolean(REMEMBER_ME, false)

        binding.checkBox.setOnClickListener() {
            preferences.edit {
                putBoolean(REMEMBER_ME, binding.checkBox.isChecked)
            }
        }

        binding.loginButton.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.loginActivity) {
                Log.d("TEST", "navigate?")
                findNavController().navigate(R.id.action_loginActivity_to_showsActivity)
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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