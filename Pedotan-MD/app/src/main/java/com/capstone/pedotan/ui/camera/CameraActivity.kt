package com.capstone.pedotan.ui.camera

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.capstone.pedotan.databinding.ActivityCameraBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.Serializable

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200
        const val GALLERY_RESULT = 201

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setupAction()
    }

    private fun setupAction() {
        binding.cameraXButton.setOnClickListener {
            val intent = Intent(this, CameraXActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }

        binding.uploadButton.setOnClickListener { uploadImage() }
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

            binding.previewImageView.setImageBitmap(result)

        } else if (it.resultCode == GALLERY_RESULT) {
            val selectedImg = it.data?.getSerializableExtra("gallery") as File
            Log.d("url image hasil", selectedImg.toString())
            getFile = selectedImg

            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                false
            )
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val description = "Ini adalah deksripsi gambar".toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

//            val service = ApiConfig().getApiService().uploadImage(imageMultipart, description)
//
//            service.enqueue(object : Callback<FileUploadResponse> {
//                override fun onResponse(
//                    call: Call<FileUploadResponse>,
//                    response: Response<FileUploadResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val responseBody = response.body()
//                        if (responseBody != null && !responseBody.error) {
//                            Toast.makeText(this@CameraActivity, responseBody.message, Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        Toast.makeText(this@CameraActivity, response.message(), Toast.LENGTH_SHORT).show()
//                    }
//                }
//                override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
//                    Toast.makeText(this@CameraActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
//                }
//            })

        } else {
            Toast.makeText(this@CameraActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

}