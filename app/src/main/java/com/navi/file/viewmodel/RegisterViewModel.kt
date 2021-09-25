package com.navi.file.viewmodel

import androidx.lifecycle.MutableLiveData
import com.navi.file.helper.FormValidator.validateModel
import com.navi.file.model.SingleLiveEvent
import com.navi.file.model.UserRegisterRequest
import com.navi.file.model.intercommunication.ExecutionResult
import com.navi.file.model.intercommunication.ResultType
import com.navi.file.repository.server.user.UserRepository
import okhttp3.ResponseBody

class RegisterViewModel(
    private val userRepository: UserRepository,
    dispatcherInfo: DispatcherInfo = DispatcherInfo()
): ViewModelExtension(dispatcherInfo) {
    // Register Result
    val registerResult: SingleLiveEvent<ExecutionResult<ResponseBody>> = SingleLiveEvent()

    /**
     * Request User Registration to serverRepository.
     * Since all input validation is held on UI, so we just handle communication.
     *
     * @param userRegisterRequest User Register Request Model
     */
    fun requestUserRegister(email: String, name: String, password: String) {
        // Valid. Do Dispatch.
        dispatchIo {
            registerResult.postValue(
                userRepository.registerUser(
                    UserRegisterRequest(email, name, password)
                )
            )
        }
    }
}