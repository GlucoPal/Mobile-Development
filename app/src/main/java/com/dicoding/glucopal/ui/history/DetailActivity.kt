package com.dicoding.glucopal.ui.history

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.glucopal.data.response.DetailResponse
import com.dicoding.glucopal.databinding.ActivityScanResultBinding
import com.dicoding.glucopal.ui.ViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.dicoding.glucopal.R

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResultBinding
    private lateinit var viewModel: DetailViewModel

    private var image: String? = null

    companion object {
        const val EXTRA_HISTORY = "HISTORY_ID"
        const val EXTRA_PHOTO = "PHOTO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = obtainViewModel(this@DetailActivity)

        val historyId = intent.getIntExtra(EXTRA_HISTORY, 0)
        viewModel.getDetailHistory(historyId)

        viewModel.loadingState.observe(this) { isLoading ->
            binding.loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.detailResponse.observe(this) { detailResponse ->
            if (detailResponse != null) {
                Log.d("Anin - DetailActivity", "Response not null")
                if (detailResponse.success == 1) {
                    Log.d("Anin - DetailActivity", "Success")
                    setData(detailResponse)
                } else {
                    Log.d("Anin - DetailActivity", "Error Data")
                }
            } else {
                Log.d("Anin - DetailActivity", "Response Null")
            }
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    private fun setData(detailResponse: DetailResponse?) {
        val item = detailResponse?.data?.firstOrNull()
        if (item != null) {
            val glValue: Float = item.gL ?: 0.0F

            image = intent.getStringExtra(EXTRA_PHOTO)
            image?.let { imageUrl ->
                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.photoFood)
            }

            binding.nameResult.text = item.foodName
            binding.tvMerk.text = item.foodName
            binding.tvResultGI.text = item.gI.toString()
            binding.tvResultGL.text = glValue.toString()

            if (glValue < 10) {
                binding.tvResultClassification.text = "Low"
                binding.imageView3.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.class_low))
                binding.constraintLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.class_low))
            } else if (glValue in 10.0..20.0) {
                binding.tvResultClassification.text = "Medium"
                binding.imageView3.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.class_medium))
                binding.constraintLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.class_medium))
            } else if (glValue > 20) {
                binding.tvResultClassification.text = "High"
                binding.imageView3.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.class_high))
                binding.constraintLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.class_high))
            } else {
                binding.tvResultClassification.text = "Error"
            }

            binding.resultCharbo.text = item.charbo
            binding.resultProtein.text = item.protein
            binding.resultFat.text = item.fat
            binding.resultCalorie.text = item.calorie
            binding.resultServingSize.text = item.servingSize
        }
    }
}