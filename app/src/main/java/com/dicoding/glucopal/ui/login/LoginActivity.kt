package com.dicoding.glucopal.ui.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.dicoding.glucopal.MainActivity
import com.dicoding.glucopal.data.response.LoginResult
import com.dicoding.glucopal.databinding.ActivityLoginBinding
import com.dicoding.glucopal.ui.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this@LoginActivity)

        setupView()
        setupAction()
        //playAnimation()
    }

    private fun obtainViewModel(activity: AppCompatActivity): LoginViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(LoginViewModel::class.java)
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
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.login(email, password)

            viewModel.loginResponse.observe(this) {loginResponse ->
                if (loginResponse != null) {
                    if (loginResponse.success== "0") {

                        Toast.makeText(this, "Anda gagal Login", Toast.LENGTH_SHORT).show()

                    } else {
                        val loginResult = loginResponse.loginResult
                        Log.d("ILHAN", "Login Result: $loginResult")

                        viewModel.saveSession(LoginResult(
                            loginResponse.loginResult?.userId,
                            loginResponse.loginResult?.username,
                            loginResponse.loginResult?.token,

                            )
                        )
                        Log.d("ILHAN", "userId: ${loginResponse.loginResult?.userId}}, username: ${loginResponse.loginResult?.username}, token: ${loginResponse.loginResult?.token}")
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
                            setMessage("Anda berhasil login. Sudah tidak sabar untuk bercerita ya?")
                            setPositiveButton("Lanjut") { _, _ ->
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                Log.d("ILHAN", "intent: ${intent}")
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                } else {

                    Toast.makeText(this, "Akun belum terdaftar", Toast.LENGTH_SHORT).show()
                }

            }
        }

        /*binding.signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }*/
    }
}