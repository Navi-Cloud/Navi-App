package com.navi.file.viewmodel

import androidx.lifecycle.MutableLiveData
import com.navi.file.helper.FormValidator.validateModel
import com.navi.file.model.UserLoginRequest
import com.navi.file.model.UserLoginResponse
import com.navi.file.model.intercommunication.ExecutionResult
import com.navi.file.model.intercommunication.ResultType
import com.navi.file.repository.server.user.UserRepository

class LoginViewModel(
    private val userRepository: UserRepository,
    dispatcherInfo: DispatcherInfo = DispatcherInfo()
): ViewModelExtension(dispatcherInfo) {
    // Login Result
    val loginUser: MutableLiveData<ExecutionResult<UserLoginResponse>> = MutableLiveData()

    /**
     * Request Login procedure to server.
     * All input-validation is held on UI-side, so in this view model, we do not have to handle
     * additional input validation.(we could do, but why?)
     *
     * @param email input-verified email
     * @param password input-verified password
     */
    fun requestUserLogin(email: String, password: String) {
        dispatchIo {
            userRepository.loginUser(UserLoginRequest(email, password)).also {
                // Send login result to loginUser[live data]
                loginUser.postValue(it)
            }
        }
    }
}