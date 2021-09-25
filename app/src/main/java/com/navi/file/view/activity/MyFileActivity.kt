package com.navi.file.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.navi.file.R
import com.navi.file.databinding.ActivityMyfileBinding
import com.navi.file.view.fragment.MyFileFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFileActivity : AppCompatActivity() {

    private val activityMyFileBinding: ActivityMyfileBinding by lazy {
        ActivityMyfileBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var myFileFragment : MyFileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMyFileBinding.root)


        activityMyFileBinding.apply {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.myfile_framelayout, myFileFragment)
                commit()
            }
        }
    }
}