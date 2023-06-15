package com.capstone.pedotan.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.capstone.pedotan.databinding.ActivityRegisterBinding
import com.capstone.pedotan.model.request.RegisterRequest
import com.capstone.pedotan.ui.ViewModelFactory
import com.capstone.pedotan.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[RegisterActivityViewModel::class.java]
    }

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val username = binding.fullnameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                username.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan nama lengkap"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                else -> {
                    val newUser = RegisterRequest(email, username, password)
                    val registerLiveData = viewModel.register(newUser)
                    registerLiveData.observe(this) { success ->
                        if (success) {
//                            showLoading(false)
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        } else {
//                            showLoading(false)
                            val toast: Toast = Toast.makeText(this, "Register Gagal", Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                }
            }
        }

        binding.tvGoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}