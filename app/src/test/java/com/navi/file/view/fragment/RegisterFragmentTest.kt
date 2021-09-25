//package com.navi.file.view.fragment
//
//import android.os.Build
//import androidx.lifecycle.MutableLiveData
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.navi.file.databinding.FragmentRegisterBinding
//import com.navi.file.helper.ViewModelFactory
//import com.navi.file.hilt.ViewModelFactoryModule
//import com.navi.file.model.SingleLiveEvent
//import com.navi.file.model.intercommunication.DisplayScreen
//import com.navi.file.model.intercommunication.ExecutionResult
//import com.navi.file.model.intercommunication.ResultType
//import com.navi.file.viewmodel.AccountViewModel
//import com.navi.file.viewmodel.RegisterViewModel
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import dagger.hilt.android.testing.HiltTestApplication
//import dagger.hilt.android.testing.UninstallModules
//import dagger.hilt.components.SingletonComponent
//import okhttp3.ResponseBody
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.kotlin.*
//import org.robolectric.annotation.Config
//import org.robolectric.shadows.ShadowToast
//import javax.inject.Singleton
//
//@UninstallModules(ViewModelFactoryModule::class)
//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//@Config(sdk = [Build.VERSION_CODES.Q], manifest = Config.NONE, application = HiltTestApplication::class)
//class RegisterFragmentTest: ViewModelTestHelper() {
//    private lateinit var mockRegisterViewModel: RegisterViewModel
//    private lateinit var mockAccountViewModel: AccountViewModel
//
//    @get:Rule
//    var hiltRule = HiltAndroidRule(this)
//
//    @Module
//    @InstallIn(SingletonComponent::class)
//    inner class ViewModelFactoryTestModule {
//        @Provides
//        @Singleton
//        fun provideTestFactory(): ViewModelFactory {
//            return mock {
//                on {registerViewModelFactory} doReturn(createViewModelFactory(mockRegisterViewModel))
//                on {accountViewModelFactory} doReturn(createViewModelFactory(mockAccountViewModel))
//            }
//        }
//    }
//
//    @Before
//    fun initTest() {
//        // Create Initial Mock
//        mockRegisterViewModel = mock()
//        mockAccountViewModel = mock()
//    }
//
//    @Test
//    fun is_display_changes_to_login_when_login_succeeds() {
//        // Let
//        val mockRegisterResult = SingleLiveEvent<ExecutionResult<ResponseBody>>()
//        val mockDisplayResult = MutableLiveData<DisplayScreen>()
//
//        // Setup Live Data
//        whenever(mockRegisterViewModel.registerResult).thenReturn(mockRegisterResult)
//        whenever(mockAccountViewModel.displayLiveData).thenReturn(mockDisplayResult)
//
//        // Setup Request
//        whenever(mockRegisterViewModel.requestUserRegister(any(), any(), any())).thenAnswer {
//            mockRegisterResult.value = ExecutionResult(ResultType.Success, value = null, "")
//            null
//        }
//
//        // Do And Check
//        launchFragmentInHiltContainer<RegisterFragment> {
//            val binding = getBinding<FragmentRegisterBinding, RegisterFragment>(this, "registerBinding")
//
//            // Click it
//            binding.confirmButton.performClick().also { result ->
//                assertTrue(result)
//            }
//
//            // Check it
//            mockDisplayResult.getOrAwaitValue().also { displayScreen ->
//                assertNotNull(displayScreen)
//                assertEquals(DisplayScreen.Login, displayScreen)
//            }
//        }
//    }
//
//    @Test
//    fun is_toast_showed_when_error_occurred() {
//        // Let
//        val mockRegisterResult = SingleLiveEvent<ExecutionResult<ResponseBody>>()
//
//        // Setup Live Data
//        whenever(mockRegisterViewModel.registerResult).thenReturn(mockRegisterResult)
//
//        // Setup Request
//        whenever(mockRegisterViewModel.requestUserRegister(any(), any(), any())).thenAnswer {
//            mockRegisterResult.value = ExecutionResult(ResultType.Conflict, value = null, "")
//            null
//        }
//
//        // Do And Check
//        launchFragmentInHiltContainer<RegisterFragment> {
//            val binding = getBinding<FragmentRegisterBinding, RegisterFragment>(this, "registerBinding")
//
//            // Click it
//            binding.confirmButton.performClick().also { result ->
//                assertTrue(result)
//            }
//
//            // Check it
//            assertEquals("", binding.emailInputLayout.editText?.text.toString())
//            assertEquals("", binding.inputNameLayout.editText?.text.toString())
//            assertEquals("", binding.inputPasswordLayout.editText?.text.toString())
//            assertEquals("", ShadowToast.getTextOfLatestToast().toString())
//        }
//    }
//}