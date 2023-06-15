package com.capstone.pedotan.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.capstone.pedotan.databinding.ActivityCameraBinding
import com.capstone.pedotan.ui.MainActivity
import com.capstone.pedotan.ui.ViewModelFactory
import com.capstone.pedotan.ui.checkfield.CheckFieldActivity
import com.capstone.pedotan.ui.checkfield.CheckFieldViewModel
import com.capstone.pedotan.ui.reduceFileImage
import com.capstone.pedotan.ui.rotateBitmap
import com.capstone.pedotan.ui.updateprofile.UpdateProfileActivity.Companion.CAMERA_X_RESULT
import com.capstone.pedotan.ui.updateprofile.UpdateProfileActivity.Companion.GALLERY_RESULT
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var getFile: File? = null
    private lateinit var viewModel: CameraActivityViewModel

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

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[CameraActivityViewModel::class.java]
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
        val settings = viewModel.getSettings()
        val fieldId = intent.getStringExtra(EXTRA_FIELD_ID)

        if (getFile != null) {
            viewModel.uploadImage("Bearer ${settings.token}", settings.email, fieldId!!, getFile!!) { responseBody ->
                if (responseBody != null) {
                    AlertDialog.Builder(this).apply {
                        setTitle("Scan Selesai")
                        setMessage("Tanaman anda terdeteksi\n\n${responseBody.predict}")

                        val dialog = create()
                        dialog.show()

                        val titleView: TextView? = dialog.findViewById(android.R.id.title)
                        titleView?.gravity = Gravity.CENTER

                        val messageView: TextView? = dialog.findViewById(android.R.id.message)
                        messageView?.gravity = Gravity.CENTER

                        setPositiveButton("OK") { _, _ ->
                            finish()
                        }
                    }.show()
                } else {
                    // Handle the case when the response is null
                }
            }

        } else {
            Toast.makeText(this@CameraActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_FIELD_ID = "field_id"

        const val CAMERA_X_RESULT = 200
        const val GALLERY_RESULT = 201

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}