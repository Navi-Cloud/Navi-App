package com.navi.file.view.activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.navi.file.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    // Log Tag
    private val logTag: String = this::class.java.simpleName

    // View binding
    private val searchBinding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(searchBinding.root)
    }
}