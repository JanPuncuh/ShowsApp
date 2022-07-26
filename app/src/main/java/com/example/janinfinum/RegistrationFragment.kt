package com.example.janinfinum

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.example.janinfinum.databinding.ActivityLoginBinding
import com.example.janinfinum.databinding.RegistrationFragmentBinding

class RegistrationFragment : Fragment() {

    private var _binding: RegistrationFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextPassword.doAfterTextChanged {
            enableRegisterButton()
        }

        binding.editTextPasswordRepeat.doAfterTextChanged {
            enableRegisterButton()
        }

        binding.registerButton.setOnClickListener() {
            //todo
        }
    }

    private fun passwordValidate(password: String, passwordRepeat: String): Boolean {
        if (password.length >= LoginActivity.MIN_PASSWORD_LENGTH) {
            if (password == passwordRepeat) {
                return true
            }
        }
        return false
    }

    private fun enableButton() {
        binding.registerButton.isEnabled = true
    }

    private fun emailValidate(email: String): Boolean {
        return email.isNotEmpty() && LoginActivity.emailRegex.containsMatchIn(email)
    }

    private fun validateLogin(email: String, password: String, passwordRepeat: String): Boolean {
        return emailValidate(email) && passwordValidate(password, passwordRepeat)
    }

    private fun enableRegisterButton() {
        binding.registerButton.isEnabled = validateLogin(
            binding.editTextEmailAddress.text.toString(),
            binding.editTextPassword.text.toString(),
            binding.editTextPasswordRepeat.text.toString()
        )
    }

}