package com.capstone.pedotan.ui.login

import android.content.Intent
import android.graphics.drawable.Animatable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.capstone.pedotan.databinding.ActivityLoginBinding
import com.capstone.pedotan.model.request.LoginRequest
import com.capstone.pedotan.ui.MainActivity
import com.capstone.pedotan.ui.ViewModelFactory
import com.capstone.pedotan.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        (binding.loginImage.drawable as? Animatable)?.start()

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[LoginActivityViewModel::class.java]
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val username = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                username.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                else -> {
                    val newLogin = LoginRequest(username, password)
                    val loginLiveData = viewModel.login(newLogin)
                    loginLiveData.observe(this) { success ->
                        if (success) {
//                            showLoading(false)
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        } else {
//                            showLoading(false)
                            val toast: Toast = Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                }
            }
        }

        binding.tvGoRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}