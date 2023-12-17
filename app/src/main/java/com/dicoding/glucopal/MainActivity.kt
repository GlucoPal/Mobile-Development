package com.dicoding.glucopal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.glucopal.databinding.ActivityMainBinding
import com.dicoding.glucopal.ui.ViewModelFactory
import com.dicoding.glucopal.ui.scan.CategoryActivity
import com.dicoding.glucopal.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = obtainViewModel(this@MainActivity)

        viewModel.getSession().observe(this) { user ->
            if (user.token != null) {
                Log.d("ILHAN", "token nya ${user.token}")
                binding.fabScan.setOnClickListener {
                    val intent = Intent(this, CategoryActivity::class.java)
                    intent.putExtra("USER_ID", user.userId)
                    startActivity(intent)
                }
            } else {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        val navView: BottomNavigationView = binding.navView
        navView.background = null
        navView.menu.getItem(1).isEnabled = false

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_history
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
    }
}