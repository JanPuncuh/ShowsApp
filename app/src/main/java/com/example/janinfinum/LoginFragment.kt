package com.example.janinfinum

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.janinfinum.databinding.ActivityLoginBinding
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import retrofit2.Call
import retrofit2.Response
import kotlin.math.log


class LoginFragment : Fragment() {

    lateinit var app: MyApplication
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EMAIL = "EMAIL"
        const val REMEMBER_ME = "REMEMBER_ME"
        const val MIN_PASSWORD_LENGTH = 6
        val emailRegex = Regex("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+\$")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //return super.onCreateView(inflater, container, savedInstanceState)
        _binding = ActivityLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = this.requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        ApiModule.initRetrofit(requireActivity())

        app = activity?.application as MyApplication

        val registered = arguments?.getBoolean(RegistrationFragment.REGISTER_SUCCESS)
        if (registered != null && registered == true) {
            binding.textViewLoginBig.text = "Registration\nsuccessful!"
            binding.registerButton.isVisible = false
        }

        //if remember me, skip login
        if (preferences.getBoolean(REMEMBER_ME, false)) {
            findNavController().navigate(R.id.action_loginActivity_to_showsActivity)
        }

        binding.checkBox.isChecked = preferences.getBoolean(REMEMBER_ME, false)

        //sets preference
        binding.checkBox.setOnClickListener() {
            preferences.edit {
                putBoolean(REMEMBER_ME, binding.checkBox.isChecked)
            }
        }

        //LOGIN BUTTON PRESSED
        binding.loginButton.setOnClickListener {

            val email = binding.editTextEmailAddress.text.toString()
            val password = binding.editTextPassword.text.toString()

            val loginRequest = LoginRequest(email, password)

            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.logging_in_layout)
            dialog.setCancelable(false)
            dialog.show()

            ApiModule.retrofit.login(loginRequest)
                .enqueue(object : retrofit2.Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                        if (response.isSuccessful) {

                            app.token = response.headers()["access-token"]
                            app.client = response.headers()["client"]
                            app.uid = email
                            app.user = response.body()?.user

                            //if checked at login, save email
                            if (binding.checkBox.isChecked) {
                                if (!preferences.contains(EMAIL)) {
                                    preferences.edit() {
                                        putString(EMAIL, binding.editTextEmailAddress.text.toString())
                                    }
                                }
                            }
                            //if unchecked at login, remove email from storage
                            else {
                                if (preferences.contains(EMAIL)) {
                                    preferences.edit().remove(EMAIL).apply()
                                }
                            }

                            //if login success, navigate to shows
                            dialog.dismiss()
                            if (findNavController().currentDestination?.id == R.id.loginActivity) {
                                findNavController().navigate(
                                    R.id.action_loginActivity_to_showsActivity,
                                    bundleOf(EMAIL to binding.editTextEmailAddress.text.toString())
                                )
                            }
                        }
                        else if (!response.isSuccessful) {
                            Toast.makeText(requireActivity(), R.string.login_unsuccessful, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("TEST", "${t.message.toString()}\n${t.stackTraceToString()}")
                    }

                })
        }

        binding.registerButton.setOnClickListener() {
            findNavController().navigate(R.id.action_loginActivity_to_registrationFragment)
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