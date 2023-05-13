package com.appbygox.rcapp.ui.out

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.appbygox.rcapp.R
import com.appbygox.rcapp.databinding.ActivityInputOutBinding

class InputOutActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityInputOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

    }
}