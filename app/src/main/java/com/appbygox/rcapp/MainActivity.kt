package com.appbygox.rcapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.appbygox.rcapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.button.setOnClickListener {
//            val intent = Intent(this@MainActivity, LoginActivity::class.java)
//            startActivity(intent)
//        }
        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        with(binding.navView) {
            setupWithNavController(navController)
        }

        val selectedFragmentId = intent.getIntExtra("selected_fragment", R.id.navigation_stock)
        setSelectedBottomNavId(selectedFragmentId)
    }

    fun setSelectedBottomNavId(selectedBottomNavId: Int) {
        val view: View = binding.navView.findViewById(selectedBottomNavId)
        view.performClick()
    }
}