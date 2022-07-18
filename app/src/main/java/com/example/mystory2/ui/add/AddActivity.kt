package com.example.mystory2.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mystory2.data.api.ApiConfig
import com.example.mystory2.data.local.UserPreferences
import com.example.mystory2.databinding.ActivityAddBinding
import com.example.mystory2.helper.reduceFileImage
import com.example.mystory2.helper.rotateBitmap
import com.example.mystory2.helper.uriToFile
import com.example.mystory2.response.AddResponse
import com.example.mystory2.ui.ViewModelFactory
import com.example.mystory2.ui.camera.CameraActivity
import com.example.mystory2.ui.main.MainActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private  var sharedPreference: UserPreferences? = null
    private var getFile: File? = null
    private lateinit var viewModel:AddViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = UserPreferences(this)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        with(binding){
            cameraButton.setOnClickListener { startCameraX() }
            galleryButton.setOnClickListener { startGallery() }
            uploadButton.setOnClickListener { upload() }
        }
        setupViewModel()
    }
    //batas on create
    private fun setupViewModel(){
        viewModel =ViewModelProvider(this, ViewModelFactory(this))[AddViewModel::class.java]
        viewModel.isLoading.observe(this){
            showLoading(it)
        }
        viewModel.add.observe(this){
            moveIntoStory(it)
        }
    }
    private fun upload(){
        viewModel.getDescriptionResult(binding.editTextTextMultiLine.text.toString())
        viewModel.uploadImage()
    }
    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
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
            viewModel.getFileResult(getFile)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@AddActivity)

            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
            viewModel.getFileResult(getFile)
        }
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ){
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
    private fun moveIntoStory(value: Boolean){
            if (value) {
                val intent = Intent(this@AddActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
    }
    companion object{
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}