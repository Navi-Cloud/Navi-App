package com.navi.file.view.fragment

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.navi.file.databinding.BottomRegisterBinding
import com.navi.file.helper.ViewModelFactory
import com.navi.file.hilt.ViewModelFactoryModule
import com.navi.file.model.SingleLiveEvent
import com.navi.file.model.UserLoginResponse
import com.navi.file.model.intercommunication.DisplayScreen
import com.navi.file.model.intercommunication.ExecutionResult
import com.navi.file.model.intercommunication.ResultType
import com.navi.file.viewmodel.AccountViewModel
import com.navi.file.viewmodel.LoginViewModel
import com.navi.file.viewmodel.RegisterViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.Assert.*
import okhttp3.ResponseBody
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
class AccountRegisterBottomTest: ViewModelTestHelper() {
    private lateinit var mockAccountViewModel: AccountViewModel
    private lateinit var mockRegisterViewModel: RegisterViewModel
    private lateinit var registerResultMock: SingleLiveEvent<ExecutionResult<ResponseBody>>

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
                on {registerViewModelFactory} doReturn(createViewModelFactory(mockRegisterViewModel))
            }
        }
    }

    @Before
    fun setupMock() {
        registerResultMock = SingleLiveEvent()
        mockAccountViewModel = mock()
        mockRegisterViewModel = mock() {
            on {registerResult} doReturn(registerResultMock)
        }
        hiltRule.inject()
    }

    // View: check whether displayLiveData changes to login when gotoLoginFromRegister clicked
    @Test
    fun is_displayLiveData_changes_to_login_gotoLoginFromRegister_clicked() {
        val mockLiveData = MutableLiveData<DisplayScreen>()
        whenever(mockAccountViewModel.displayLiveData).thenReturn(mockLiveData)
        launchFragmentInHiltContainer<AccountRegisterBottom> {
            getBinding<BottomRegisterBinding, AccountRegisterBottom>(this, "binding").also {
                it.gotoLoginFromRegister.performClick().also {res -> assertTrue(res)}

                mockLiveData.getOrAwaitValue().also {res ->
                    assertEquals(DisplayScreen.Login, res)
                }
            }
        }
    }

    // EmailInput: Check email checker function works well(INTGR)
    @Test
    fun is_emailInput_works_well() {
        launchFragmentInHiltContainer<AccountRegisterBottom> {
            getBinding<BottomRegisterBinding, AccountRegisterBottom>(this, "binding").also {
                it.registerButton.isEnabled = true // force set to true

                // Wrong input - for checking button disabled
                assertTrue(it.registerEmail.requestFocus()) // focus ON
                it.registerEmail.editText!!.setText("")
                assertFalse(it.registerButton.isEnabled)

                // focus to other one. check for error.
                assertTrue(it.registerPassword.requestFocus())
                assertNotNull(it.registerEmail.error)

                // Focus to email again, correct it
                assertTrue(it.registerEmail.requestFocus()) // focus ON
                it.registerEmail.editText!!.setText("kangdroid@test.com")

                // Focus to other one and check error is gone.
                assertTrue(it.registerPassword.requestFocus())
                assertNull(it.registerEmail.error)
            }
        }
    }

    // PasswordInput: Check password checker function works well(INTGR)
    @Test
    fun is_password_works_well() {
        launchFragmentInHiltContainer<AccountRegisterBottom> {
            getBinding<BottomRegisterBinding, AccountRegisterBottom>(this, "binding").also {
                it.registerButton.isEnabled = true // force set to true

                // Wrong input - for checking button disabled
                assertTrue(it.registerPassword.requestFocus()) // focus ON
                it.registerPassword.editText!!.setText("")
                assertFalse(it.registerButton.isEnabled)

                // focus to other one. check for error.
                assertTrue(it.registerEmail.requestFocus())
                assertNotNull(it.registerPassword.error)

                // Focus to email again, correct it
                assertTrue(it.registerPassword.requestFocus()) // focus ON
                it.registerPassword.editText!!.setText("kangdroid@test.com")

                // Focus to other one and check error is gone.
                assertTrue(it.registerEmail.requestFocus())
                assertNull(it.registerPassword.error)
            }
        }
    }

    // PasswordInput: Check password matcher function works well(INTGR)
    @Test
    fun is_passwordMatcher_works_well() {
        launchFragmentInHiltContainer<AccountRegisterBottom> {
            getBinding<BottomRegisterBinding, AccountRegisterBottom>(this, "binding").also {
                it.registerButton.isEnabled = true // force set to true
                it.registerPassword.editText!!.setText("testPassword@")

                // Wrong input - for checking button disabled
                assertTrue(it.registerPasswordMatches.requestFocus()) // focus ON
                it.registerPasswordMatches.editText!!.setText("asdf")
                assertFalse(it.registerButton.isEnabled)

                // focus to other one. check for error.
                assertTrue(it.registerEmail.requestFocus())
                assertNotNull(it.registerPasswordMatches.error)

                // Focus to email again, correct it
                assertTrue(it.registerPasswordMatches.requestFocus()) // focus ON
                it.registerPasswordMatches.editText!!.setText("testPassword@")

                // Focus to other one and check error is gone.
                assertTrue(it.registerEmail.requestFocus())
                assertNull(it.registerPasswordMatches.error)
            }
        }
    }

    // Register: When succeeds, it should set live data to login
    @Test
    fun is_register_redirects_to_login() {
        val mockDisplayLiveData = MutableLiveData<DisplayScreen>()
        whenever(mockAccountViewModel.displayLiveData).thenReturn(mockDisplayLiveData)

        whenever(mockRegisterViewModel.requestUserRegister(any(), any(), any())).then {
            registerResultMock.value = ExecutionResult(ResultType.Success, null, "")
            null
        }

        launchFragmentInHiltContainer<AccountRegisterBottom> {
            getBinding<BottomRegisterBinding, AccountRegisterBottom>(this, "binding").also {
                assertTrue(it.registerButton.performClick())
                mockDisplayLiveData.getOrAwaitValue().also {res ->
                    assertEquals(DisplayScreen.Login, res)
                }
            }
        }
    }

    // Register: When Conflicts, it should show toast string.
    @Test
    fun is_register_shows_conflict_error() {
        val mockDisplayLiveData = MutableLiveData<DisplayScreen>()
        whenever(mockAccountViewModel.displayLiveData).thenReturn(mockDisplayLiveData)

        whenever(mockRegisterViewModel.requestUserRegister(any(), any(), any())).then {
            registerResultMock.value = ExecutionResult(ResultType.Conflict, null, "")
            null
        }

        launchFragmentInHiltContainer<AccountRegisterBottom> {
            getBinding<BottomRegisterBinding, AccountRegisterBottom>(this, "binding").also {
                assertTrue(it.registerButton.performClick())

                ShadowToast.getTextOfLatestToast().also {res ->
                    assertNotNull(res)
                    assertEquals("User already exists!", res)
                }
            }
        }
    }

    // Register: When Unknown, it should show toast string.
    @Test
    fun is_register_shows_unknown_error() {
        val mockDisplayLiveData = MutableLiveData<DisplayScreen>()
        whenever(mockAccountViewModel.displayLiveData).thenReturn(mockDisplayLiveData)

        whenever(mockRegisterViewModel.requestUserRegister(any(), any(), any())).then {
            registerResultMock.value = ExecutionResult(ResultType.Unknown, null, "")
            null
        }

        launchFragmentInHiltContainer<AccountRegisterBottom> {
            getBinding<BottomRegisterBinding, AccountRegisterBottom>(this, "binding").also {
                assertTrue(it.registerButton.performClick())

                ShadowToast.getTextOfLatestToast().also {res ->
                    assertNotNull(res)
                    assertEquals("Unknown Error occurred!", res)
                }
            }
        }
    }

}