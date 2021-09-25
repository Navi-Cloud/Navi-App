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
import com.navi.file.databinding.BottomLoginBinding
import com.navi.file.databinding.BottomMainBinding
import com.navi.file.helper.FormValidator
import com.navi.file.helper.FormValidator.validateEmail
import com.navi.file.helper.FormValidator.validateModel
import com.navi.file.helper.FormValidator.validatePassword
import com.navi.file.helper.ViewModelFactory
import com.navi.file.model.intercommunication.ResultType
import com.navi.file.repository.server.user.UserRepository
import com.navi.file.viewmodel.AccountViewModel
import com.navi.file.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountLoginBottom @Inject constructor(): BottomSheetDialogFragment() {
    companion object {
        val fragmentTag = "AccountLoginBottom"
    }
    private var _binding: BottomLoginBinding? = null
    private val binding: BottomLoginBinding get() = _binding!!

    // View Model Factory
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    // Account View Model[Or DisplayViewModel]
    private val accountViewModel: AccountViewModel by activityViewModels { viewModelFactory.accountViewModelFactory }

    // Login View Model
    private val loginViewModel: LoginViewModel by viewModels { viewModelFactory.loginViewModelFactory }

    // Private getter for email and password
    private val userInputEmail: String get() = binding.loginEmailInput.editText?.text.toString()
    private val userInputPassword: String get() = binding.loginPasswordInput.editText?.text.toString()

    // Toast object
    private val toast: Toast by lazy {
        Toast(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup View
        setupView()

        // Setup observer
        observeLiveData()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    /**
     * Observe LoginViewModel's live data.
     * Mostly it defines what this fragment have to do login result arrived.
     *
     */
    private fun observeLiveData() {
        loginViewModel.loginUser.observe(viewLifecycleOwner) {
            when (it.resultType) {
                ResultType.Success -> {} // Do nothing for now
                ResultType.Forbidden -> toast.showLongToast("Please check your ID or password!")
                else -> {
                    Log.e(this::class.java.simpleName, "Error occurred when user tried to log-in.")
                    Log.e(this::class.java.simpleName, "Result Type: ${it.resultType.name}, Error Message: ${it.message}")
                    toast.showLongToast("Unknown error occurred. Please try again or contact administrator for more details.")
                }
            }
        }
    }

    /**
     * Setup View, like setting up listener, etc.
     *
     */
    private fun setupView() {
        // Setup Email Input
        setupEmailInput()

        // Setup Password Input
        setupPasswordInput()

        // Setup sign-in button
        binding.signInButton.setOnClickListener {
            loginViewModel.requestUserLogin(userInputEmail, userInputPassword)
        }
    }

    /**
     * Setup Email Input[like listener.]
     *
     */
    private fun setupEmailInput() {
        binding.loginEmailInput.apply {
            editText?.let {
                // Add TextChangedListener
                it.addTextChangedListener { _ ->
                    binding.signInButton.isEnabled = validateModel(userInputEmail, userInputPassword)
                }

                // Setup FocusChangedListener for Email Input
                it.setOnFocusChangeListener { _, hasFocus ->
                    Log.e(this::class.java.simpleName, "FocusOnListener Executed[Email]")
                    binding.loginEmailInput.error = if (!hasFocus && !validateEmail(userInputEmail)) {
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
        binding.loginPasswordInput.apply {
            editText?.let {
                // Add TextChangedListener
                it.addTextChangedListener { _ ->
                    binding.signInButton.isEnabled = validateModel(userInputEmail, userInputPassword)
                }

                // Setup FocusChangedListener for Email Input
                it.setOnFocusChangeListener { _, hasFocus ->
                    binding.loginPasswordInput.error = if (!hasFocus && !validatePassword(userInputPassword)) {
                        "Password should be more then 8 characters, and should contains one or more special characters."
                    } else {
                        null
                    }
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