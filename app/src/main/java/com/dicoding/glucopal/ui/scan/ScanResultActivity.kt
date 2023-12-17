package com.dicoding.glucopal.ui.scan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.glucopal.MainActivity
import com.dicoding.glucopal.databinding.ActivityScanResultBinding
class ScanResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResultBinding

    private var foodName: String? = null
    private var carbohydrate: String? = null
    private var protein: String? = null
    private var fat: String? = null
    private var calorie: String? = null
    private var servingSize: String? = null
    private var glValue = 0.0F
    private var giValue = 0.0F
    private var imageCategory: String? = null

    companion object {
        const val EXTRA_NAME = "FOOD_NAME"
        const val EXTRA_CHARBO = "CHARBO"
        const val EXTRA_PROTEIN = "PROTEIN"
        const val EXTRA_FAT = "FAT"
        const val EXTRA_CALORIE = "CALORIE"
        const val EXTRA_SERVING_SIZE = "SERVING_SIZE"
        const val EXTRA_GL_VALUE = "GL_VALUE"
        const val EXTRA_GI_VALUE = "GI_VALUE"
        const val EXTRA_IMAGE = "IMAGE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodName = intent.getStringExtra(EXTRA_NAME)
        carbohydrate = intent.getStringExtra(EXTRA_CHARBO)
        protein = intent.getStringExtra(EXTRA_PROTEIN)
        fat = intent.getStringExtra(EXTRA_FAT)
        calorie = intent.getStringExtra(EXTRA_CALORIE)
        servingSize = intent.getStringExtra(EXTRA_SERVING_SIZE)
        giValue = intent.getFloatExtra(EXTRA_GI_VALUE, 0.0F)
        glValue = intent.getFloatExtra(EXTRA_GL_VALUE, 0.0F)
        imageCategory = intent.getStringExtra(EXTRA_IMAGE)

        binding.tvMerk.text = foodName
        binding.tvResultGI.text = giValue.toString()
        binding.tvResultGL.text = glValue.toString()
//        binding.tvResultClassification.text
        binding.resultCharbo.text = carbohydrate
        binding.resultProtein.text = protein
        binding.resultFat.text = fat
        binding.resultCalorie.text = calorie
        binding.resultServingSize.text = servingSize

        imageCategory?.let { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .into(binding.photoFood)
        }

        binding.ivBack.setOnClickListener {
            navigateToHome()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateToHome()
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}