package com.dicoding.glucopal.ui.glycemicindex

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.dicoding.glucopal.databinding.ActivityGycemicIndexBinding

class GycemicIndexActivity : AppCompatActivity() {

    lateinit var binding :ActivityGycemicIndexBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGycemicIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        binding.toolbar.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }
}