package com.navi.file.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.navi.file.databinding.ActivityMainBinding
import com.navi.file.view.fragment.OptionBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// The View
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    protected val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var optionBottomSheetFragment : OptionBottomSheetFragment

    private val bottomSheetList: List<BottomSheetDialogFragment> by lazy {
        listOf(optionBottomSheetFragment)
    }

    private fun dismissAllDialog() {
        bottomSheetList.forEach {
            if (it.isAdded) it.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        activityMainBinding.apply {
            mainOption.setOnClickListener {
                dismissAllDialog()
                optionBottomSheetFragment.show(supportFragmentManager, OptionBottomSheetFragment.fragmentTag)
            }
            mainSearchBar.setOnClickListener{
                val intent = Intent(applicationContext, SearchActivity::class.java)
                startActivity(intent)
            }
            mainMyfile.setOnClickListener {
                val intent = Intent(applicationContext, MyFileActivity::class.java)
                startActivity(intent)
            }
        }
    }
}