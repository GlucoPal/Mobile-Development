package com.dicoding.glucopal.ui.scan

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dicoding.glucopal.databinding.ActivityUploadBinding
import com.dicoding.glucopal.ui.ViewModelFactory
import com.dicoding.glucopal.utils.getImageUri
import com.dicoding.glucopal.utils.reduceFileImage
import com.dicoding.glucopal.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadActivity : AppCompatActivity() {
    private lateinit var uploadBinding: ActivityUploadBinding
    private var currentImageUri: Uri? = null
    private val uploadImageViewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var categoryId = 0
    private var foodName: String? = null
    private var resultGI = 0.0F

    companion object {
        const val EXTRA_CATEGORY = "CATEGORY_ID"
        const val EXTRA_FOOD = "FOOD_NAME"
        const val EXTRA_IMAGE = "IMAGE"
        const val EXTRA_GI = "GI"
        const val EXTRA_ID = "USER_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uploadBinding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(uploadBinding.root)

        supportActionBar?.hide()

        uploadBinding.cameraButton.setOnClickListener {
            startCamera()
        }

        uploadBinding.galleryButton.setOnClickListener {
            startGallery()
        }

        uploadBinding.uploadButton.setOnClickListener {
            if (TextUtils.isEmpty(uploadBinding.descEditText.text.toString().trim())) {
                Toast.makeText(this, "Please enter the name of food!", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage()
            }
        }

        categoryId = intent.getIntExtra(EXTRA_CATEGORY, 0)
        foodName = intent.getStringExtra(EXTRA_FOOD)
        resultGI = intent.getFloatExtra(EXTRA_GI, 0.0F)

        uploadBinding.categoryEditText.text = foodName
        uploadBinding.GIEditText.text = resultGI.toString()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            uploadBinding.imageView.setImageURI(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentImageUri?.let { uri ->
            outState.putString("currentImageUri", uri.toString())
        }
    }

    @SuppressLint("NewApi")
    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val userId = intent.getStringExtra(EXTRA_ID)
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val nameFood = uploadBinding.descEditText.text.toString()
            val idFood = categoryId.toString()
            val GI = resultGI.toString()

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part =
                MultipartBody.Part.createFormData("image", imageFile.name, requestImageFile)
            val requestNameFood = nameFood.toRequestBody("text/plain".toMediaType())
            val requestIdFood = idFood.toRequestBody("text/plain".toMediaType())
            val requestGI = GI.toRequestBody("text/plain".toMediaType())

            uploadImageViewModel.upload(userId!!, imageMultipart, requestNameFood, requestIdFood, requestGI)

            uploadImageViewModel.uploadResponse.observe(this) { uploadScanResponse ->
                if (uploadScanResponse != null) {
                    if (uploadScanResponse.success == 1){
                        val responseData = uploadScanResponse.data
                        if (responseData != null) {
                            val foodName = responseData.foodName
                            val carbohydrate = responseData.charbo
                            val protein = responseData.protein
                            val fat = responseData.fat
                            val calorie = responseData.calorie
                            val servingSize = responseData.servingSize
                            val glValue = responseData.gL
                            val giValue = intent.getFloatExtra(EXTRA_GI, 0.0F)
                            val imageCategory = intent.getStringExtra(EXTRA_IMAGE)

                            Toast.makeText(this, "Scanning Success", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, ScanResultActivity::class.java)
                            intent.putExtra("FOOD_NAME", foodName)
                            intent.putExtra("CHARBO", carbohydrate)
                            intent.putExtra("PROTEIN", protein)
                            intent.putExtra("FAT", fat)
                            intent.putExtra("CALORIE", calorie)
                            intent.putExtra("SERVING_SIZE", servingSize)
                            intent.putExtra("GL_VALUE", glValue)
                            intent.putExtra("GI_VALUE", giValue)
                            intent.putExtra("IMAGE", imageCategory)
                            startActivity(intent)
                        }
                    } else {
                        Log.d("Anin - CategoryActivity", "Error Data")
                    }
                }
            }
        }
    }
}