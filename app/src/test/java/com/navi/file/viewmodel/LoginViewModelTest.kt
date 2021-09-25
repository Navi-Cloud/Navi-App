package com.navi.file.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.navi.file.model.UserLoginRequest
import com.navi.file.model.UserLoginResponse
import com.navi.file.model.intercommunication.ExecutionResult
import com.navi.file.model.intercommunication.ResultType
import com.navi.file.repository.server.user.UserRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class LoginViewModelTest: ViewModelHelper() {
    // Rule that every android-thread should launched in single thread
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private var mockUserRepository: UserRepository = mock()
    private var loginViewModel: LoginViewModel = LoginViewModel(
        userRepository = mockUserRepository,
        dispatcherInfo = DispatcherInfo(
            uiDispatcher = Dispatchers.Unconfined,
            ioDispatcher = Dispatchers.Unconfined,
            backgroundDispatcher = Dispatchers.Unconfined
        )
    )

    // Check whether default constructor creates its instance well.
    @Test
    fun is_default_constructor_works_well() {
        val loginViewModel = LoginViewModel(mockUserRepository)
        Assert.assertNotNull(loginViewModel)
    }

    // Check whether requestUserLogin sets livedata well.
    @Test
    fun is_requestUserLogin_sets_loginResult_well() {
        // Setup Mock
        whenever(mockUserRepository.loginUser(any()))
            .thenReturn(ExecutionResult(ResultType.Success, null, ""))

        // Do
        loginViewModel.requestUserLogin("", "")

        // Check
        loginViewModel.loginUser.getOrAwaitValue().also {
            assertEquals(ResultType.Success, it.resultType)
        }
    }
}