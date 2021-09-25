package com.navi.file.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.ACTION_FOCUS
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.navi.file.databinding.BottomLoginBinding
import com.navi.file.helper.ViewModelFactory
import com.navi.file.hilt.RepositoryModule
import com.navi.file.hilt.ViewModelFactoryModule
import com.navi.file.model.UserLoginResponse
import com.navi.file.model.intercommunication.ExecutionResult
import com.navi.file.model.intercommunication.ResultType
import com.navi.file.repository.server.user.UserRepository
import com.navi.file.viewmodel.AccountViewModel
import com.navi.file.viewmodel.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import javax.inject.Singleton

@UninstallModules(ViewModelFactoryModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q], manifest = Config.NONE, application = HiltTestApplication::class)
class AccountLoginBottomTest: ViewModelTestHelper() {
    private lateinit var mockAccountViewModel: AccountViewModel
    private lateinit var mockLoginViewModel: LoginViewModel
    private lateinit var mockLiveData: MutableLiveData<ExecutionResult<UserLoginResponse>>

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Module
    @InstallIn(SingletonComponent::class)
    inner class ViewModelFactoryTestModule {
        @Provides
        @Singleton
        fun provideTestFactory(): ViewModelFactory {
            return mock {
                on {accountViewModelFactory} doReturn(createViewModelFactory(mockAccountViewModel))
                on {loginViewModelFactory} doReturn(createViewModelFactory(mockLoginViewModel))
            }
        }
    }

    @Before
    fun setupMock() {
        mockAccountViewModel = mock()
        mockLoginViewModel = mock()
        mockLiveData = MutableLiveData()
        hiltRule.inject()
    }

    // Check whether email input is properly validated(correctly validated)
    @Test
    fun is_signInButton_disabled_when_only_input_email() {
        whenever(mockLoginViewModel.loginUser).thenReturn(mockLiveData)
        launchFragmentInHiltContainer<AccountLoginBottom> {
            getBinding<BottomLoginBinding, AccountLoginBottom>(this, "binding").also {
                assertNotNull(it.loginEmailInput.editText)

                // Force set signIn button to true
                it.signInButton.isEnabled = true

                // Do
                it.loginEmailInput.editText!!.setText("kangdroid")

                // Check
                assertFalse(it.signInButton.isEnabled)
            }
        }
    }

    // Check error is shown if validate email is failed AND user is out of scope[email]
    @Test
    fun is_email_error_shown_when_validate_failed_and_user_outOfScope() {
        whenever(mockLoginViewModel.loginUser).thenReturn(mockLiveData)
        launchFragmentInHiltContainer<AccountLoginBottom> {
            getBinding<BottomLoginBinding, AccountLoginBottom>(this, "binding").also {
                assertNotNull(it.loginEmailInput.editText)

                // Do
                it.loginEmailInput.requestFocus().also {res -> assertTrue(res)}
                it.loginEmailInput.editText!!.setText("kangdroid")

                // Move focus to other
                it.loginPasswordInput.requestFocus().also {res -> assertTrue(res)}

                // Check
                assertNotNull(it.loginEmailInput.error)
            }
        }
    }

    // Check error is nullified if validating email succeeds.
    @Test
    fun is_email_error_nullified_when_validate_succeed_and_user_outOfScope() {
        whenever(mockLoginViewModel.loginUser).thenReturn(mockLiveData)
        launchFragmentInHiltContainer<AccountLoginBottom> {
            getBinding<BottomLoginBinding, AccountLoginBottom>(this, "binding").also {
                assertNotNull(it.loginEmailInput.editText)

                // Do
                it.loginEmailInput.editText!!.setText("kangdroid@test.com")

                // Move focus to other
                it.loginPasswordInput.requestFocus().also {res -> assertTrue(res)}

                // Check
                assertNull(it.loginEmailInput.error)
            }
        }
    }

    // Check whether button is disabled when password validation failed.
    @Test
    fun is_signInButton_disabled_when_password_validation_failed() {
        whenever(mockLoginViewModel.loginUser).thenReturn(mockLiveData)
        launchFragmentInHiltContainer<AccountLoginBottom> {
            getBinding<BottomLoginBinding, AccountLoginBottom>(this, "binding").also {
                assertNotNull(it.loginPasswordInput.editText)

                // Force set signIn button to true
                it.signInButton.isEnabled = true

                // Do
                it.loginPasswordInput.editText!!.setText("test")

                // Check
                assertFalse(it.signInButton.isEnabled)
            }
        }
    }

    // Check whether error is shown when password validation failed AND out-of-focus
    @Test
    fun is_password_error_shown_when_password_validation_failed_and_outOfFocus() {
        whenever(mockLoginViewModel.loginUser).thenReturn(mockLiveData)
        launchFragmentInHiltContainer<AccountLoginBottom> {
            getBinding<BottomLoginBinding, AccountLoginBottom>(this, "binding").also {
                assertNotNull(it.loginPasswordInput.editText)

                // Do
                it.loginPasswordInput.requestFocus().also {res -> assertTrue(res)}
                it.loginPasswordInput.editText!!.setText("aa")

                // Move focus to other
                it.loginEmailInput.requestFocus().also {res -> assertTrue(res)}

                // Check
                assertNotNull(it.loginPasswordInput.error)
            }
        }
    }

    // Check whether error is nullified when password validation succeed AND out-of-focus
    @Test
    fun is_password_error_nullified_when_password_validation_succeed_and_outOfFocus() {
        whenever(mockLoginViewModel.loginUser).thenReturn(mockLiveData)
        launchFragmentInHiltContainer<AccountLoginBottom> {
            getBinding<BottomLoginBinding, AccountLoginBottom>(this, "binding").also {
                assertNotNull(it.loginPasswordInput.editText)

                // Do
                it.loginPasswordInput.editText!!.setText("testPassword@")

                // Move focus to other
                it.loginEmailInput.requestFocus().also {res -> assertTrue(res)}

                // Check
                assertNull(it.loginPasswordInput.error)
            }
        }
    }

    // Check function is doing nothing when login succeeds
    @Test
    fun is_class_does_nothing_when_login_succeeds() {
        // Mock Setup
        whenever(mockLoginViewModel.loginUser).thenReturn(mockLiveData)
        whenever(mockLoginViewModel.requestUserLogin(any(), any())).then {
            mockLiveData.value = ExecutionResult(ResultType.Success, null, "")
            null
        }

        launchFragmentInHiltContainer<AccountLoginBottom> {
            getBinding<BottomLoginBinding, AccountLoginBottom>(this, "binding").also {
                it.signInButton.performClick().also {res -> assertTrue(res)}
                ShadowToast.getLatestToast().also {res -> assertNull(res)}
            }
        }
    }

    // Check error toast is shown when forbidden.
    @Test
    fun is_class_shows_toast_when_forbidden() {
        // Mock Setup
        whenever(mockLoginViewModel.loginUser).thenReturn(mockLiveData)
        whenever(mockLoginViewModel.requestUserLogin(any(), any())).then {
            mockLiveData.value = ExecutionResult(ResultType.Forbidden, null, "")
            null
        }

        launchFragmentInHiltContainer<AccountLoginBottom> {
            getBinding<BottomLoginBinding, AccountLoginBottom>(this, "binding").also {
                it.signInButton.performClick().also {res -> assertTrue(res)}
                ShadowToast.getLatestToast().also {res -> assertNotNull(res) }
                ShadowToast.getTextOfLatestToast().also {res -> assertEquals("Please check your ID or password!", res)}
            }
        }
    }

    // Check error toast is shown when unknown.
    @Test
    fun is_class_shows_toast_when_unknown() {
        // Mock Setup
        whenever(mockLoginViewModel.loginUser).thenReturn(mockLiveData)
        whenever(mockLoginViewModel.requestUserLogin(any(), any())).then {
            mockLiveData.value = ExecutionResult(ResultType.Unknown, null, "")
            null
        }

        launchFragmentInHiltContainer<AccountLoginBottom> {
            getBinding<BottomLoginBinding, AccountLoginBottom>(this, "binding").also {
                it.signInButton.performClick().also {res -> assertTrue(res)}
                ShadowToast.getLatestToast().also {res -> assertNotNull(res) }
                ShadowToast.getTextOfLatestToast().also {res -> assertEquals("Unknown error occurred. Please try again or contact administrator for more details.", res)}
            }
        }
    }
}