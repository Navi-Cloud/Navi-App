package com.navi.file.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.navi.file.R
import com.navi.file.helper.ViewModelFactory
import com.navi.file.hilt.ViewModelFactoryModule
import com.navi.file.view.fragment.AccountMainBottom
import com.navi.file.view.fragment.MyFileFragment
import com.navi.file.view.fragment.ViewModelTestHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.Assert.assertTrue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.internal.matchers.InstanceOf
import org.mockito.internal.matchers.Null
import org.mockito.kotlin.any
import org.mockito.kotlin.isNotNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import javax.inject.Singleton

@UninstallModules(ViewModelFactoryModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q], manifest = Config.NONE, application = HiltTestApplication::class)
class MyFileActivityTest : ViewModelTestHelper() {

    // Application Context
    private lateinit var context: Context

    private lateinit var mockmyFileFragment : MyFileFragment

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Module
    @InstallIn(SingletonComponent::class)
    inner class ViewModelFactoryTestModule {
        @Provides
        @Singleton
        fun provideTestFactory(): ViewModelFactory {
            return mock {
            }
        }
    }

    @Before
    fun createInitialMock() {
        context = ApplicationProvider.getApplicationContext()
        hiltRule.inject()
    }

    @Test
    fun is_myFileActivity_is_alive(){
        launchActivity<MyFileActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
        }.also {
            Assert.assertEquals(Lifecycle.State.RESUMED, it.state)
        }
    }

    @Test
    fun is_myFileActivity_shows_myFileFragment_well(){

        val activityScenario = launchActivity<MyFileActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
        }.also {
            Assert.assertEquals(Lifecycle.State.RESUMED, it.state)
        }

        val mockTransaction = mock<FragmentTransaction>()
        activityScenario.onActivity { myFileActivity ->
            myFileActivity.supportFragmentManager.findFragmentById(R.id.myfile_framelayout).also {
                assertTrue(it is MyFileFragment)
            }
        }
    }
}