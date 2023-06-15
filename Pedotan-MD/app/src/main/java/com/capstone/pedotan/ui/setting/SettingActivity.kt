package com.capstone.pedotan.ui.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.capstone.pedotan.R
import com.capstone.pedotan.databinding.ActivitySettingBinding
import com.capstone.pedotan.ui.ViewModelFactory
import com.capstone.pedotan.ui.login.LoginActivity
import com.capstone.pedotan.ui.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var viewModel: SettingViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[SettingViewModel::class.java]
    }

    private fun setupAction() {
        val settings = viewModel.getCurrentSettings()
        when (settings.isDarkMode) {
            0 -> {
                binding.tvTheme.text = "On"
            }
            1 -> {
                binding.tvTheme.text = "Off"
            }
            2 -> {
                binding.tvTheme.text = "Default"
            }
        }

        binding.linearLayoutAccount.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.linearLayoutTheme.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.change_theme))
            val styles = arrayOf("On","Off","System default")
            val checkedItem = settings.isDarkMode

            builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->
                when (which) {
                    0 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        viewModel.setDarkMode(0)
                        dialog.dismiss()
                    }
                    1 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        viewModel.setDarkMode(1)
                        dialog.dismiss()
                    }
                    2 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        viewModel.setDarkMode(2)
                        dialog.dismiss()
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.linearLayoutLogout.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Logout")
                setMessage("Apakah Anda Yakin Ingin Keluar?")
                setPositiveButton("Ya") { _, _ ->
                    auth.signOut()
                    viewModel.removeSession()
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }
    }
}