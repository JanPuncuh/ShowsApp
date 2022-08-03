package com.example.janinfinum

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.janinfinum.databinding.RegistrationFragmentBinding
import retrofit2.Call
import retrofit2.Response

class RegistrationFragment : Fragment() {

    private var _binding: RegistrationFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val REGISTER_SUCCESS = "REGISTER_SUCCESS"
    }

    private val viewModel by viewModels<RegistrationViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ApiModule.initRetrofit(requireActivity())

        binding.editTextPassword.doAfterTextChanged {
            enableRegisterButton()
        }

        binding.editTextPasswordRepeat.doAfterTextChanged {
            enableRegisterButton()
        }

        binding.registerButton.setOnClickListener {

            val email = binding.editTextEmailAddress.text.toString()
            val password = binding.editTextPassword.text.toString()
            val passwordRepeat = binding.editTextPasswordRepeat.text.toString()

            val registerRequest = RegisterRequest(email, password, passwordRepeat)

            ApiModule.retrofit.register(registerRequest)
                .enqueue(object : retrofit2.Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        viewModel.registrationResultLiveData.value = response.isSuccessful

                        if (response.isSuccessful) {
                            val preferences = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
                            preferences.edit().putString("TOKEN", response.headers()["access-token"]).apply()
                            preferences.edit().putString("CLIENT", response.headers()["client"]).apply()
                            preferences.edit().putString("UID", response.headers()["uid"]).apply()

                            findNavController().navigate(
                                R.id.action_registrationFragment_to_loginActivity,
                                bundleOf(REGISTER_SUCCESS to true)
                            )
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        viewModel.registrationResultLiveData.value = false
                        return
                    }

                })
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