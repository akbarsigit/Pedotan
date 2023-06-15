package com.capstone.pedotan.ui.checkfield

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.capstone.pedotan.R
import com.capstone.pedotan.databinding.ActivityAddFieldBinding
import com.capstone.pedotan.databinding.ActivityCheckFieldBinding
import com.capstone.pedotan.model.request.AddFieldRequest
import com.capstone.pedotan.model.request.CheckFieldRequest
import com.capstone.pedotan.ui.MainActivity
import com.capstone.pedotan.ui.ViewModelFactory
import com.capstone.pedotan.ui.addfield.AddFieldActivityViewModel
import com.capstone.pedotan.ui.camera.CameraActivity
import com.capstone.pedotan.ui.camera.CameraXActivity
import com.capstone.pedotan.ui.history.HistoryActivity
import com.capstone.pedotan.ui.login.LoginActivity
import com.capstone.pedotan.ui.reduceFileImage
import com.capstone.pedotan.ui.rotateBitmap
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CheckFieldActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckFieldBinding
    private lateinit var viewModel: CheckFieldViewModel
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckFieldBinding.inflate(layoutInflater)
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
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[CheckFieldViewModel::class.java]
    }

    private fun setupAction() {
        val fieldId = intent.getStringExtra(EXTRA_FIELD_ID)
        Log.d("id", fieldId.toString())
        val settings = viewModel.getSettings()

        val dropdownItems = listOf("Cukup", "Kurang")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dropdownItems)
        binding.apply {
            nitrogenEditText.setAdapter(adapter)
            phospatEditText.setAdapter(adapter)
            kaliumEditText.setAdapter(adapter)

            npkButton.setOnClickListener {
                val intent = Intent(this@CheckFieldActivity, CameraXActivity::class.java)
                launcherIntentCameraX.launch(intent)
            }

            sendButton.setOnClickListener {
                val nitrogen = binding.nitrogenEditText.text.toString()
                val phospat = binding.phospatEditText.text.toString()
                val kalium = binding.kaliumEditText.text.toString()
                val rainfall = binding.rainEditText.text.toString()
                val pH = binding.phEditText.text.toString()
                val humidity = binding.humidityEditText.text.toString()
                val temperature = binding.temperatureEditText.text.toString()

                val sendNitrogen = when (nitrogen) {
                    "Cukup" -> 80F
                    else -> 20F
                }
                val sendPhospat = when (phospat) {
                    "Cukup" -> 80F
                    else -> 20F
                }
                val sendKalium = when (kalium) {
                    "Cukup" -> 80F
                    else -> 20F
                }

                when {
                    nitrogen.isEmpty() -> {
                        binding.nitrogenEditTextLayout.error = "Masukkan kondisi nitrogen"
                    }
                    phospat.isEmpty() -> {
                        binding.phospatEditTextLayout.error = "Masukkan kondisi phospat"
                    }
                    kalium.isEmpty() -> {
                        binding.kaliumEditTextLayout.error = "Masukkan kondisi kalium"
                    }
                    rainfall.isEmpty() -> {
                        binding.kaliumEditTextLayout.error = "Masukkan kondisi curah hujan"
                    }
                    pH.isEmpty() -> {
                        binding.kaliumEditTextLayout.error = "Masukkan kandungan pH"
                    }
                    humidity.isEmpty() -> {
                        binding.kaliumEditTextLayout.error = "Masukkan kondisi kelembapan"
                    }
                    temperature.isEmpty() -> {
                        binding.kaliumEditTextLayout.error = "Masukkan kondisi suhu"
                    }
                    else -> {
                        val checkData = CheckFieldRequest(settings.email, fieldId!!, sendNitrogen, sendPhospat, sendKalium, temperature.toFloat(), humidity.toFloat(), pH.toFloat(), rainfall.toFloat())
                        addField("Bearer ${settings.token}", checkData)
                    }
                }
            }
        }
    }

    private fun addField(token: String, checkFieldRequest: CheckFieldRequest) {
        val loginLiveData = viewModel.checkField(token, checkFieldRequest)
        loginLiveData.observe(this) { success ->
            if (success) {
//              showLoading(false)
                AlertDialog.Builder(this).apply {
                    setTitle("Berhasil")
                    setMessage("Kondisi Kebun berhasil disimpan")

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
//              showLoading(false)
                val toast: Toast = Toast.makeText(this, "Kebun tidak berhasil ditambahkan", Toast.LENGTH_SHORT)
                toast.show()
            }
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
            getFile = myFile
        } else if (it.resultCode == GALLERY_RESULT) {
            val selectedImg = it.data?.getSerializableExtra("gallery") as File
            getFile = selectedImg
        }
        uploadImage()
    }

    private fun uploadImage() {
        if (getFile != null) {
            viewModel.uploadImage(getFile!!) { responseBody ->

                if (responseBody != null) {
                    // Process the responseBody as needed

                    if (responseBody.n == 80F) {
                        binding.nitrogenEditText.setText("Cukup", false)
                    } else {
                        binding.nitrogenEditText.setText("Kurang", false)
                    }

                    if (responseBody.p == 80F) {
                        binding.phospatEditText.setText("Cukup", false)
                    } else {
                        binding.phospatEditText.setText("Kurang", false)
                    }

                    if (responseBody.k == 80F) {
                        binding.kaliumEditText.setText("Cukup", false)
                    } else {
                        binding.kaliumEditText.setText("Kurang", false)
                    }

                } else {
                    // Handle the case when the response is null
                }
            }
        }
    }

    companion object {
        private const val TAG = "CheckField Activity"
        const val EXTRA_FIELD_ID = "field_id"

        const val CAMERA_X_RESULT = 200
        const val GALLERY_RESULT = 201

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}