package com.navi.file.view.activity

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.navi.file.R
import com.navi.file.databinding.ActivityAccountBinding
import com.navi.file.helper.ViewModelFactory
import com.navi.file.model.intercommunication.DisplayScreen
import com.navi.file.view.fragment.*
import com.navi.file.viewmodel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountActivity: AppCompatActivity() {
    // ViewModel Factory
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    // Main Account BottomSheet
    @Inject
    lateinit var accountMainBottom: AccountMainBottom

    // Login Account BottomSheet
    @Inject
    lateinit var accountLoginBottom: AccountLoginBottom

    // Register Account BottomSheet
    @Inject
    lateinit var accountRegisterBottom: AccountRegisterBottom

    private val bottomSheetList: List<BottomSheetDialogFragment> by lazy {
        listOf(accountMainBottom, accountLoginBottom, accountRegisterBottom)
    }

    // Account ViewModel[Or Display ViewModel]
    private val accountViewModel: AccountViewModel by viewModels { viewModelFactory.accountViewModelFactory }

    // UI Binding
    private val activityAccountBinding: ActivityAccountBinding by lazy {
        ActivityAccountBinding.inflate(layoutInflater)
    }

    private fun dismissAllDialog() {
        bottomSheetList.forEach {
            if (it.isAdded) it.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set UI
        setContentView(activityAccountBinding.root)

        // Observe
        accountViewModel.displayLiveData.observe(this) {
            when (it) {
                DisplayScreen.Login -> {
                    // Display Transition to Login
                    Log.d(this::class.java.simpleName, "Login Requested")
                    dismissAllDialog()
                    accountLoginBottom.show(supportFragmentManager, AccountLoginBottom.fragmentTag)
                }
                DisplayScreen.Register -> {
                    // Display transition to Registration
                    dismissAllDialog()
                    accountRegisterBottom.show(supportFragmentManager, AccountRegisterBottom.fragmentTag)
                }
                else -> {}
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            accountMainBottom.show(supportFragmentManager, AccountMainBottom.fragmentTag)
        } else {
            if (accountMainBottom.isAdded) accountMainBottom.dismiss()
        }
        return true
    }
}