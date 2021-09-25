package com.navi.file.view.activity

import android.content.Context
import android.os.Build
import android.view.MotionEvent
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.core.view.MotionEventBuilder
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.navi.file.R
import com.navi.file.databinding.ActivityAccountBinding
import com.navi.file.helper.ViewModelFactory
import com.navi.file.hilt.ViewModelFactoryModule
import com.navi.file.model.SingleLiveEvent
import com.navi.file.model.intercommunication.DisplayScreen
import com.navi.file.view.fragment.AccountLoginBottom
import com.navi.file.view.fragment.AccountMainBottom
import com.navi.file.view.fragment.AccountRegisterBottom
import com.navi.file.view.fragment.ViewModelTestHelper
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.mockito.verification.VerificationMode
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowGestureDetector
import javax.inject.Singleton

@UninstallModules(ViewModelFactoryModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q], manifest = Config.NONE, application = HiltTestApplication::class)
class AccountActivityTest: ViewModelTestHelper() {
    // AccountActivity uses AccountViewModel though, so we need to mock it.
    private lateinit var mockAccountViewModel: AccountViewModel

    // AccountActivity calls All View Model[because of fragment]
    private lateinit var mockLoginViewModel: LoginViewModel
    private lateinit var mockRegisterViewModel: RegisterViewModel

    // Application Context
    private lateinit var context: Context

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
                on {registerViewModelFactory} doReturn(createViewModelFactory(mockRegisterViewModel))
            }
        }
    }

    @Before
    fun createInitialMock() {
        mockAccountViewModel = mock()
        mockLoginViewModel = mock()
        mockRegisterViewModel = mock()
        context = ApplicationProvider.getApplicationContext()
        hiltRule.inject()
    }

    // Check whether accountActivity is created well.
    @Test
    fun is_accountActivity_is_alive() {
        // mock LiveData
        val mockLiveData = MutableLiveData<DisplayScreen>()
        whenever(mockAccountViewModel.displayLiveData).thenReturn(mockLiveData)
        whenever(mockLoginViewModel.loginUser).thenReturn(MutableLiveData())
        whenever(mockRegisterViewModel.registerResult).thenReturn(SingleLiveEvent())

        launchActivity<AccountActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
        }.also {
            assertEquals(Lifecycle.State.RESUMED, it.state)
        }
    }

    // When accountActivity is launched, and it observed 'login', it should show accountLoginBottom
    @Test
    fun is_accountActivity_shows_accountLoginBottom_when_DisplayScreen_Login() {
        // mock LiveData
        val mockLiveData = MutableLiveData<DisplayScreen>()
        whenever(mockAccountViewModel.displayLiveData).thenReturn(mockLiveData)
        whenever(mockLoginViewModel.loginUser).thenReturn(MutableLiveData())
        whenever(mockRegisterViewModel.registerResult).thenReturn(SingleLiveEvent())

        // Create Activity Scenario
        val activityScenario = launchActivity<AccountActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
        }.also { assertEquals(Lifecycle.State.RESUMED, it.state) }

        // Check if login is recognized
        mockLiveData.value = DisplayScreen.Login
        activityScenario.onActivity { accountActivity ->
            accountActivity.supportFragmentManager.findFragmentByTag(AccountLoginBottom.fragmentTag).also {
                assertTrue(it is AccountLoginBottom)
            }
        }
    }

    // Check whether accountRegisterBottom is shown when register displayscreen enum is observed.
    @Test
    fun is_accountActivity_shows_accountRegisterBottom_when_displayscreen_register() {
        // mock LiveData
        val mockLiveData = MutableLiveData<DisplayScreen>()
        whenever(mockAccountViewModel.displayLiveData).thenReturn(mockLiveData)
        whenever(mockLoginViewModel.loginUser).thenReturn(MutableLiveData())
        whenever(mockRegisterViewModel.registerResult).thenReturn(SingleLiveEvent())

        // Create Activity Scenario
        val activityScenario = launchActivity<AccountActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
        }.also { assertEquals(Lifecycle.State.RESUMED, it.state) }

        // Check if login is recognized
        mockLiveData.value = DisplayScreen.Register
        activityScenario.onActivity { accountActivity ->
            accountActivity.supportFragmentManager.findFragmentByTag(AccountRegisterBottom.fragmentTag).also {
                assertTrue(it is AccountRegisterBottom)
            }
        }
    }

    // Check whether accountActivity is doing nothing when displayscreen observed as null.
    @Test
    fun is_accountActivity_does_nothing_when_displayscreen_null() {
        // mock LiveData
        val mockLiveData = MutableLiveData<DisplayScreen>()
        whenever(mockAccountViewModel.displayLiveData).thenReturn(mockLiveData)
        whenever(mockLoginViewModel.loginUser).thenReturn(MutableLiveData())
        whenever(mockRegisterViewModel.registerResult).thenReturn(SingleLiveEvent())

        // Create Activity Scenario
        val activityScenario = launchActivity<AccountActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
        }.also { assertEquals(Lifecycle.State.RESUMED, it.state) }

        // Check if login is recognized
        mockLiveData.value = null
        activityScenario.onActivity { accountActivity ->
            accountActivity.supportFragmentManager.findFragmentByTag(AccountRegisterBottom.fragmentTag).also {
                assertNull(it)
            }

            accountActivity.supportFragmentManager.findFragmentByTag(AccountLoginBottom.fragmentTag).also {
                assertNull(it)
            }

            accountActivity.supportFragmentManager.findFragmentByTag(AccountMainBottom.fragmentTag).also {
                assertNull(it)
            }
        }
    }

    // Check it is showing 'accountMainBottom' when up gesture is detected
    @Test
    fun is_accountActivity_shows_accountMainBottom_when_gesture_up() {
        // mock LiveData
        val mockLiveData = MutableLiveData<DisplayScreen>()
        whenever(mockAccountViewModel.displayLiveData).thenReturn(mockLiveData)
        whenever(mockLoginViewModel.loginUser).thenReturn(MutableLiveData())
        whenever(mockRegisterViewModel.registerResult).thenReturn(SingleLiveEvent())

        // Create Activity Scenario
        val activityScenario = launchActivity<AccountActivity>().apply {
            moveToState(Lifecycle.State.STARTED)
        }.also { assertEquals(Lifecycle.State.STARTED, it.state) }

        // Check if login is recognized
        mockLiveData.value = null
        val mockAccountMainBottom = mock<AccountMainBottom>()
        activityScenario.onActivity { accountActivity ->
            // Inject mainBottomSheet, because there is no way to test onTouchEvent with fully-ran robolectric test.
            accountActivity.accountMainBottom = mockAccountMainBottom
            accountActivity.onTouchEvent(MotionEventBuilder.newBuilder().setAction(MotionEvent.ACTION_UP).build())

            // Make sure verify show method is called.
            verify(mockAccountMainBottom).show(any<FragmentManager>(), any())
        }
    }

    // Check if it dismisses accountMainBottom when it is currently showing.
    @Test
    fun is_accountActivity_dismisses_accountMainBottom_when_showing() {
        whenever(mockAccountViewModel.displayLiveData).thenReturn(MutableLiveData<DisplayScreen>())
        whenever(mockLoginViewModel.loginUser).thenReturn(MutableLiveData())
        whenever(mockRegisterViewModel.registerResult).thenReturn(SingleLiveEvent())

        // Create Activity Scenario
        val activityScenario = launchActivity<AccountActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
        }.also { assertEquals(Lifecycle.State.RESUMED, it.state) }

        // Check if login is recognized
        val mockAccountMainBottom = mock<AccountMainBottom>()
        activityScenario.onActivity { accountActivity ->
            // Inject mainBottomSheet, because there is no way to test onTouchEvent with fully-ran robolectric test.
            accountActivity.accountMainBottom = mockAccountMainBottom

            // Setup isAdded getter
            whenever(mockAccountMainBottom.isAdded).thenReturn(true)

            // Do
            accountActivity.onTouchEvent(MotionEventBuilder.newBuilder().setAction(MotionEvent.ACTION_DOWN).build())

            // Check whether dismiss method is called
            verify(mockAccountMainBottom).dismiss()
        }
    }

    // Check if it dose nothing when touch event is NOT up and mainBottom is already dismissed
    @Test
    fun is_accountActivity_does_nothing_when_mainBottom_already_dismissed() {
        whenever(mockAccountViewModel.displayLiveData).thenReturn(MutableLiveData<DisplayScreen>())
        whenever(mockLoginViewModel.loginUser).thenReturn(MutableLiveData())
        whenever(mockRegisterViewModel.registerResult).thenReturn(SingleLiveEvent())

        // Create Activity Scenario
        val activityScenario = launchActivity<AccountActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
        }.also { assertEquals(Lifecycle.State.RESUMED, it.state) }

        // Check if login is recognized
        val mockAccountMainBottom = mock<AccountMainBottom>()
        activityScenario.onActivity { accountActivity ->
            // Inject mainBottomSheet, because there is no way to test onTouchEvent with fully-ran robolectric test.
            accountActivity.accountMainBottom = mockAccountMainBottom

            // Setup isAdded getter
            whenever(mockAccountMainBottom.isAdded).thenReturn(false)

            // Do
            accountActivity.onTouchEvent(MotionEventBuilder.newBuilder().setAction(MotionEvent.ACTION_DOWN).build())

            // Check whether dismiss method is never called
            verify(mockAccountMainBottom, never()).dismiss()
        }
    }
}