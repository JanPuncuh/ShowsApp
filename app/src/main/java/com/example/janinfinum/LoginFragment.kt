package com.example.janinfinum

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.janinfinum.databinding.ActivityLoginBinding


class LoginFragment : Fragment() {

    lateinit var app: MyApplication
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    private val args by navArgs<LoginFragmentArgs>()


    companion object {
        const val REMEMBER_ME = "REMEMBER_ME"
        const val MIN_PASSWORD_LENGTH = 6
        val emailRegex = Regex("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+\$")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = this.requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        ApiModule.initRetrofit(requireActivity())

        app = activity?.application as MyApplication

        setAnimations()

        //todo this crashes???
        //setTextIfRegistered()

        val directions = LoginFragmentDirections.actionLoginActivityToShowsActivity()
        //if remember me, skip login
        if (preferences.getBoolean(REMEMBER_ME, false)) {
            findNavController().navigate(directions)
        }

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

            //shows to user that login request is in process
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.logging_in_layout)
            dialog.setCancelable(false)
            dialog.show()

            viewModel.login(email, password)
            viewModel.uid.observe(viewLifecycleOwner) {
                app.uid = it
                preferences.edit().putString("UID", app.uid).apply()
            }
            viewModel.token.observe(viewLifecycleOwner) {
                app.token = it
                preferences.edit().putString("TOKEN", app.token).apply()
            }
            viewModel.client.observe(viewLifecycleOwner) {
                app.client = it
                preferences.edit().putString("CLIENT", app.client).apply()
            }
            viewModel.successfulLogin.observe(viewLifecycleOwner) { success ->
                if (success) {
                    dialog.dismiss()
                    //login -> shows
                    findNavController().navigate(LoginFragmentDirections.actionLoginActivityToShowsActivity())
                }
            }
        }

        binding.registerButton.setOnClickListener() {
            findNavController().navigate(LoginFragmentDirections.actionLoginActivityToRegistrationFragment())
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

    private fun setTextIfRegistered() {
        if (args.success) {
            binding.textViewLoginBig.text = getString(R.string.registration_successful)
            binding.registerButton.isVisible = false
        }
    }

    private fun setAnimations() {
        binding.arrowLogoShows.startAnimation(AnimationUtils.loadAnimation(context, R.anim.drop_down))
        binding.textViewShowsLogo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_1s_offset))
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
        binding.loginButton.isEnabled =
            validateLogin(binding.editTextEmailAddress.text.toString(), binding.editTextPassword.text.toString())
    }

}