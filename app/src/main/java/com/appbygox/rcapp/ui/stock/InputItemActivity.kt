package com.appbygox.rcapp.ui.stock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.appbygox.rcapp.R
import com.appbygox.rcapp.databinding.ActivityInputItemBinding

class InputItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputItemBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}