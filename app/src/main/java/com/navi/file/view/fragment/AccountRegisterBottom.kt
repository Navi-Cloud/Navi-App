package com.navi.file.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.navi.file.databinding.BottomRegisterBinding
import com.navi.file.helper.FormValidator
import com.navi.file.helper.ViewModelFactory
import com.navi.file.model.intercommunication.DisplayScreen
import com.navi.file.model.intercommunication.ExecutionResult
import com.navi.file.model.intercommunication.ResultType
import com.navi.file.viewmodel.AccountViewModel
import com.navi.file.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import javax.inject.Inject

@AndroidEntryPoint
class AccountRegisterBottom @Inject constructor(): BottomSheetDialogFragment() {
    companion object {
        val fragmentTag = "AccountRegisterBottom"
    }
    private var _binding: BottomRegisterBinding? = null
    private val binding: BottomRegisterBinding get() = _binding!!

    // View Model Factory
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    // Account View Model[Or DisplayViewModel]
    private val accountViewModel: AccountViewModel by activityViewModels { viewModelFactory.accountViewModelFactory }

    // Register View Model
    private val registerViewModel: RegisterViewModel by viewModels { viewModelFactory.registerViewModelFactory }

    // Private getter for email and password
    private val userInputEmail: String get() = binding.registerEmail.editText?.text.toString()
    private val userInputPassword: String get() = binding.registerPassword.editText?.text.toString()
    private val userInputPasswordMatches: String get() = binding.registerPasswordMatches.editText?.text.toString()

    // Toast object
    private val toast: Toast by lazy {
        Toast(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe
        registerViewModel.registerResult.observe(viewLifecycleOwner) {
            when (it.resultType) {
                ResultType.Success -> {
                    accountViewModel.displayLiveData.value = DisplayScreen.Login
                }
                ResultType.Conflict -> toast.showLongToast("User already exists!")
                else -> {
                    Log.e(this::class.java.simpleName, "Unknown Error occurred.")
                    Log.e(this::class.java.simpleName, "Message: ${it.message}")
                    toast.showLongToast("Unknown Error occurred!")
                }
            }
        }

        // setup View
        setupView()
    }

    override fun onDestroyView() {
        Log.d(this::class.java.simpleName, "Destroying AccountRegisterBottom!")
        _binding = null
        super.onDestroyView()
    }

    /**
     * Setup View.
     *
     */
    private fun setupView() {
        // When GOTO Login Clicked -> Change Display to Login.
        binding.gotoLoginFromRegister.setOnClickListener {
            accountViewModel.displayLiveData.value = DisplayScreen.Login
        }

        // When 'GetStarted' Clicked -> Register
        binding.registerButton.setOnClickListener {
            registerViewModel.requestUserRegister(
                email = binding.registerEmail.editText!!.text.toString(),
                password = binding.registerPassword.editText!!.text.toString(),
                name = "random"
            )
        }

        // Email Input
        setupEmailInput()

        // setup Password
        setupPasswordInput()

        // Setup Password Matcher
        setupRePasswordInput()
    }

    /**
     * Setup Email Input[like listener.]
     *
     */
    private fun setupEmailInput() {
        binding.registerEmail.apply {
            editText?.let {
                // Add TextChangedListener
                it.addTextChangedListener { _ ->
                    binding.registerButton.isEnabled = FormValidator.validateModel(userInputEmail, userInputPassword)
                }

                // Setup FocusChangedListener for Email Input
                it.setOnFocusChangeListener { _, hasFocus ->
                    Log.e(this::class.java.simpleName, "FocusOnListener Executed[Email]")
                    binding.registerEmail.error = if (!hasFocus && !FormValidator.validateEmail(userInputEmail)) {
                        Log.e(this::class.java.simpleName, "Email is not valid at all.")
                        "Please check your email again."
                    } else {
                        null
                    }
                }
            }
        }
    }

    /**
     * Setup Password Input[like listener]
     *
     */
    private fun setupPasswordInput() {
        binding.registerPassword.apply {
            editText?.let {
                // Add TextChangedListener
                it.addTextChangedListener { _ ->
                    binding.registerButton.isEnabled = FormValidator.validateModel(userInputEmail, userInputPassword)
                }

                // Setup FocusChangedListener for Email Input
                it.setOnFocusChangeListener { _, hasFocus ->
                    binding.registerPassword.error = if (!hasFocus && !FormValidator.validatePassword(userInputPassword)) {
                        "Password should be more then 8 characters, and should contains one or more special characters."
                    } else {
                        null
                    }
                }
            }
        }
    }

    /**
     * Setup password matcher
     *
     */
    private fun setupRePasswordInput() {
        binding.registerPasswordMatches.apply {
            editText?.let {
                it.addTextChangedListener {
                    binding.registerButton.isEnabled = userInputPassword == userInputPasswordMatches
                }

                it.setOnFocusChangeListener { _, hasFocus ->
                    binding.registerPasswordMatches.error = if (!hasFocus && (userInputPassword != userInputPasswordMatches)) {
                        "Password does not match!"
                    } else null
                }
            }
        }
    }

    /**
     * Toast Extension for showing long-last message.
     *
     * @param message Message to show.
     */
    private fun Toast.showLongToast(message: CharSequence) {
        this.cancel()
        this.setText(message)
        this.duration = Toast.LENGTH_LONG
        this.show()
    }
}