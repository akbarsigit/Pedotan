package com.capstone.pedotan.ui

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.pedotan.R
import com.capstone.pedotan.databinding.ActivityMainBinding
import com.capstone.pedotan.ui.camera.CameraActivity
import com.capstone.pedotan.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[MainActivityViewModel::class.java]
        val settings = viewModel.getCurrentSettings()

//        // Jika Sudah ada API
//        if (!settings.isLogin) {
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }
    }
}