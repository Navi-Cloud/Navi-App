package com.navi.file.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.navi.file.databinding.FragmentRegisterBinding
import com.navi.file.helper.ViewModelFactory
import com.navi.file.model.UserRegisterRequest
import com.navi.file.model.intercommunication.DisplayScreen
import com.navi.file.model.intercommunication.ExecutionResult
import com.navi.file.model.intercommunication.ResultType
import com.navi.file.viewmodel.AccountViewModel
import com.navi.file.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment: Fragment() {
    // ViewModelFactory
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    // Register ViewModel
    private val registerViewModel: RegisterViewModel by viewModels { viewModelFactory.registerViewModelFactory }

    // Account ViewModel
    private val accountViewModel: AccountViewModel by activityViewModels { viewModelFactory.accountViewModelFactory }

    // Binding
    private var _registerBinding: FragmentRegisterBinding? = null
    private val registerBinding: FragmentRegisterBinding get() = _registerBinding!!

    /**
     * Just create layout-binded object. That is all.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _registerBinding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return registerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe.
        observe()

        // Setup Actions
        with(registerBinding) {
            // Setup Confirm[Register] Button
            confirmButton.setOnClickListener {
                registerViewModel.requestUserRegister(
                    email = emailInputLayout.editText!!.text.toString(),
                    name = inputNameLayout.editText!!.text.toString(),
                    password = inputPasswordLayout.editText!!.text.toString()
                )
            }
        }
    }

    private fun observe() {
        registerViewModel.registerResult.observe(viewLifecycleOwner) {
            when (it.resultType) {
                ResultType.Success -> {
                    accountViewModel.displayLiveData.value = DisplayScreen.Login
                }
                else -> {handleRegisterError(it)}
            }
        }
    }

    /**
     * Handle Registration Error, likely empty-ing text fields and show us an error.
     *
     * @param result Execution Result of Registration.
     */
    private fun handleRegisterError(result: ExecutionResult<ResponseBody>) {
        emptyFields()
        toastError(result.message)
    }

    /**
     * Wrapper for showing toast messages.
     *
     * @param errorMessage Error Messages to show.
     */
    private fun toastError(errorMessage: String) =
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()

    /**
     * Clear out all input registration-fields.
     *
     */
    private fun emptyFields() {
        registerBinding.emailInputLayout.editText!!.setText("")
        registerBinding.inputNameLayout.editText!!.setText("")
        registerBinding.inputPasswordLayout.editText!!.setText("")
    }
}