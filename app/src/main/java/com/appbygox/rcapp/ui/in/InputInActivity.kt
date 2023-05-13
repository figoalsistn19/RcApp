package com.appbygox.rcapp.ui.`in`

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.appbygox.rcapp.R
import com.appbygox.rcapp.databinding.ActivityInputInBinding

class InputInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityInputInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}