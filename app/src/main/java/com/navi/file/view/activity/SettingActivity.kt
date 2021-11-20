package com.navi.file.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.navi.file.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    // Log Tag
    private val logTag: String = this::class.java.simpleName

    // View binding
    private val settingBingind: ActivitySettingBinding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(settingBingind.root)

        settingBingind.apply {
            settingBack.setOnClickListener {
                onBackPressed()
            }
        }
    }
}