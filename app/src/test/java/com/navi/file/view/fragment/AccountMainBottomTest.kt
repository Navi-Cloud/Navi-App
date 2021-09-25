package com.navi.file.view.fragment

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.navi.file.databinding.BottomMainBinding
import com.navi.file.helper.ViewModelFactory
import com.navi.file.hilt.ViewModelFactoryModule
import com.navi.file.model.intercommunication.DisplayScreen
import com.navi.file.viewmodel.AccountViewModel
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
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import javax.inject.Singleton

@UninstallModules(ViewModelFactoryModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q], manifest = Config.NONE, application = HiltTestApplication::class)
class AccountMainBottomTest: ViewModelTestHelper() {
    private lateinit var mockAccountViewModel: AccountViewModel

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
            }
        }
    }

    @Before
    fun initTest() {
        // Create Initial Mock
        mockAccountViewModel = mock()
    }

    // Check whether bottomsheet creates
    @Test
    fun is_mainBottom_creates_well() {
        // Setup LiveData
        val mockLiveData = MutableLiveData<DisplayScreen>()
        whenever(mockAccountViewModel.displayLiveData).thenReturn(mockLiveData)

        // Do & Assert
        launchFragmentInHiltContainer<AccountMainBottom> {
            assertNotNull(this)
        }
    }

    // Check displayLiveData changed to login when mainSignInButton clicked
    @Test
    fun is_displayLiveData_changes_to_login_when_mainSignInButton_Clicked() {
        // Setup LiveData
        val mockLiveData = MutableLiveData<DisplayScreen>()
        whenever(mockAccountViewModel.displayLiveData).thenReturn(mockLiveData)

        // Do & Assert
        launchFragmentInHiltContainer<AccountMainBottom> {
            // Click it
            getBinding<BottomMainBinding, AccountMainBottom>(this, "binding").also {
                it.mainSignInButton.performClick().also {clickResult ->
                    assertTrue(clickResult)
                }
            }

            // Check for live data changes
            mockLiveData.getOrAwaitValue().also {
                assertEquals(DisplayScreen.Login, it)
            }
        }
    }

    // Check displayLiveData changed to register when mainRegisterButton clicked
    @Test
    fun is_displayLiveData_changes_to_register_when_mainRegisterButton_clicked() {
        // Setup LiveData
        val mockLiveData = MutableLiveData<DisplayScreen>()
        whenever(mockAccountViewModel.displayLiveData).thenReturn(mockLiveData)

        // Do & Assert
        launchFragmentInHiltContainer<AccountMainBottom> {
            // Click it
            getBinding<BottomMainBinding, AccountMainBottom>(this, "binding").also {
                it.mainRegisterButton.performClick().also {clickResult ->
                    assertTrue(clickResult)
                }
            }

            // Check for live data changes
            mockLiveData.getOrAwaitValue().also {
                assertEquals(DisplayScreen.Register, it)
            }
        }
    }
}