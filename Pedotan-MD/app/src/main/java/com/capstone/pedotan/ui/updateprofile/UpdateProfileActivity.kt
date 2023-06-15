package com.capstone.pedotan.ui.updateprofile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.capstone.pedotan.R
import com.capstone.pedotan.api.ApiConfig
import com.capstone.pedotan.databinding.ActivityCameraBinding
import com.capstone.pedotan.databinding.ActivityUpdateProfileBinding
import com.capstone.pedotan.model.request.CheckFieldRequest
import com.capstone.pedotan.model.response.FileUploadResponse
import com.capstone.pedotan.ui.ViewModelFactory
import com.capstone.pedotan.ui.camera.CameraXActivity
import com.capstone.pedotan.ui.profile.ProfileActivity
import com.capstone.pedotan.ui.reduceFileImage
import com.capstone.pedotan.ui.register.RegisterActivityViewModel
import com.capstone.pedotan.ui.rotateBitmap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: UpdateProfileViewModel
    private lateinit var binding: ActivityUpdateProfileBinding
    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200
        const val GALLERY_RESULT = 201

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[UpdateProfileViewModel::class.java]
    }

    private fun setupAction() {
        binding.ktpImage.setOnClickListener {
            val intent = Intent(this, CameraXActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }

        binding.btnUpdateProfile.setOnClickListener {
            uploadImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.ktpImage.setImageBitmap(result)

        } else if (it.resultCode == GALLERY_RESULT) {
            val selectedImg = it.data?.getSerializableExtra("gallery") as File
            Log.d("url image hasil", selectedImg.toString())
            getFile = selectedImg

            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                false
            )
            binding.ktpImage.setImageBitmap(result)
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val settings = viewModel.getSettings()
            val token = "Bearer ${settings.token}"
            val email = settings.email
            val name = binding.nameEditText.text.toString()
            val noHandphone = binding.phoneNumberEditText.text.toString()
            val nik = binding.nikEditText.text.toString()
            val location = binding.locationEditText.text.toString()

            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = "Masukkan nama lengkap"
                }
                noHandphone.isEmpty() -> {
                    binding.phoneNumberEditTextLayout.error = "Masukkan no handphone"
                }
                nik.isEmpty() -> {
                    binding.nikEditTextLayout.error = "Masukkan NIK"
                }
                location.isEmpty() -> {
                    binding.locationEditTextLayout.error = "Masukkan alamat"
                }
                else -> {
                    showLoading(true)
                    viewModel.updateProfile(token, file, name, email, noHandphone, nik, location) { responseBody ->
                        if (responseBody != null) {
                            Toast.makeText(this@UpdateProfileActivity, "Profile berhasil diperbarui.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, ProfileActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@UpdateProfileActivity, "Profile gagal diperbarui\nSilahkan coba kembali", Toast.LENGTH_SHORT).show()
                        }
                        showLoading(false)
                    }
                }
            }
        } else {
            showLoading(false)
            Toast.makeText(this@UpdateProfileActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.pbUser.visibility = if (state) View.VISIBLE else View.GONE
    }
}