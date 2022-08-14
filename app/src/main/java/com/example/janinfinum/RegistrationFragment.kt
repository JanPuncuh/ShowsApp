package com.example.janinfinum

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.janinfinum.databinding.RegistrationFragmentBinding

class RegistrationFragment : Fragment() {

    private var _binding: RegistrationFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val REGISTER_SUCCESS = "REGISTER_SUCCESS"
    }

    private val viewModel by viewModels<RegistrationViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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

        binding.registerButton.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val email = binding.editTextEmailAddress.text.toString()
        val password = binding.editTextPassword.text.toString()
        val passwordRepeat = binding.editTextPasswordRepeat.text.toString()

        val preferences = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)

        viewModel.register(email, password, passwordRepeat, preferences)
        viewModel.success.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigate(
                    R.id.action_registrationFragment_to_loginActivity,
                    bundleOf(REGISTER_SUCCESS to true)
                )
            }
            else Toast.makeText(requireContext(), "Error registering", Toast.LENGTH_SHORT).show()
        }
    }

    private fun passwordValidate(password: String, passwordRepeat: String): Boolean {
        if (password.length >= LoginFragment.MIN_PASSWORD_LENGTH) {
            if (password == passwordRepeat) {
                return true
            }
        }
        return false
    }

    private fun emailValidate(email: String): Boolean {
        return email.isNotEmpty() && LoginFragment.emailRegex.containsMatchIn(email)
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