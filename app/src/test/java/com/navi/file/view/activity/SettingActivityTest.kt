package com.navi.file.view.activity

import android.content.Context
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.navi.file.helper.ViewModelFactory
import com.navi.file.hilt.ViewModelFactoryModule
import com.navi.file.view.fragment.ViewModelTestHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.annotation.Config
import javax.inject.Singleton

@UninstallModules(ViewModelFactoryModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q], manifest = Config.NONE, application = HiltTestApplication::class)
class SettingActivityTest : ViewModelTestHelper(){
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
            }
        }
    }

    @Before
    fun createInitialMock() {
        context = ApplicationProvider.getApplicationContext()
        hiltRule.inject()
    }
}