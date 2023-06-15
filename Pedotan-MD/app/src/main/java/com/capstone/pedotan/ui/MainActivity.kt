package com.capstone.pedotan.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.pedotan.databinding.ActivityMainBinding
import com.capstone.pedotan.ui.login.LoginActivity
import com.capstone.pedotan.R
import com.capstone.pedotan.ui.market.ShopActivity
import com.capstone.pedotan.ui.profile.ProfileActivity
import com.capstone.pedotan.ui.setting.SettingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_dashboard, R.id.navigation_contract, R.id.navigation_market, R.id.navigation_loan
        ).build()

        setSupportActionBar(binding.myToolbar);
        val toolbar: Toolbar = binding.myToolbar
        toolbar.setTitleTextAppearance(this, R.style.ToolbarTitleTextAppearance_Bold)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setupViewModel()
        setupAction()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[MainActivityViewModel::class.java]
        val settings = viewModel.getCurrentSettings()

        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        if (firebaseUser == null && !settings.isLogin) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupAction() {
        binding.myToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_shop -> {
                    val intent = Intent(this, ShopActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_setting -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}