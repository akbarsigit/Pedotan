package com.capstone.pedotan.ui.addfield

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.pedotan.databinding.ActivityAddFieldBinding
import com.capstone.pedotan.model.request.AddFieldRequest
import com.capstone.pedotan.ui.MainActivity
import com.capstone.pedotan.ui.ViewModelFactory

class AddFieldActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFieldBinding
    private lateinit var viewModel: AddFieldActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFieldBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[AddFieldActivityViewModel::class.java]
    }

    private fun setupAction() {
        val dropdownItems = listOf("apel", "kopi", "anggur", "jagung", "padi")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dropdownItems)
        binding.comodityEditText.setAdapter(adapter)

        val settings = viewModel.getSettings()
        binding.addKebunButton.setOnClickListener {
            val comodity = binding.comodityEditText.text.toString()
            val location = binding.locationEditText.text.toString()
            val area = binding.areaEditText.text.toString()

            when {
                comodity.isEmpty() -> {
                    binding.comodityEditTextLayout.error = "Pilih Komoditas"
                }
                location.isEmpty() -> {
                    binding.locationEditTextLayout.error = "Masukkan lokasi kebun anda"
                }
                area.isEmpty() -> {
                    binding.areaEditTextLayout.error = "Masukkan luas kebun anda"
                }
                else -> {
                    addField("Bearer ${settings.token}", settings.email, comodity, location, area)
                }
            }
        }
    }

    private fun addField(token: String, email: String, comodity: String, location: String, area: String) {
        val newField = AddFieldRequest(email, comodity, location, area)
        val loginLiveData = viewModel.addField(token, newField)
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
                val toast: Toast = Toast.makeText(this, "Kebun tidak berhasil ditambahkan", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
    companion object {
        private const val TAG = "LoginActivity"
    }


}