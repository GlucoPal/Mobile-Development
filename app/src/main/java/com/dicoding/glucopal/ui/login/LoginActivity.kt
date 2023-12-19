package com.dicoding.glucopal.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.dicoding.glucopal.MainActivity
import com.dicoding.glucopal.R
import com.dicoding.glucopal.data.response.LoginResult
import com.dicoding.glucopal.databinding.ActivityLoginBinding
import com.dicoding.glucopal.ui.ViewModelFactory
import com.dicoding.glucopal.ui.register.RegisterActivity

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
        playAnimation()
    }

    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 0f, 1f)
        title.duration = 500
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f)
        email.duration = 500
        val boxEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f)
        email.duration = 500
        val pass = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f)
        pass.duration = 500
        val boxPass = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 0f, 1f)
        pass.duration = 500
        val btn = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f)
        btn.duration = 500
        val text = ObjectAnimator.ofFloat(binding.toLoginText, View.ALPHA, 0f, 1f)
        btn.duration = 500
        val text2 = ObjectAnimator.ofFloat(binding.toLogin, View.ALPHA, 0f, 1f)
        btn.duration = 500

        val animatorSetSequential = AnimatorSet()
        animatorSetSequential.playSequentially(
            title,
            email,
            boxEmail,
            pass,
            boxPass,
            btn,
            text,
            text2
        )

        animatorSetSequential.start()
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

                        val message = loginResponse.message ?: "Server not responding"
                        customDialogFailed(message)

                    } else {
                        val loginResult = loginResponse.loginResult
                        Log.d("ILHAN", "Login Result: $loginResult")

                        viewModel.saveSession(LoginResult(
                            loginResponse.loginResult?.userId,
                            loginResponse.loginResult?.username,
                            loginResponse.loginResult?.token,

                            )
                        )

                        val message = loginResponse.message ?: "Server not responding"
                        customDialogSuccess(message)

                    }
                } else {
                    val message = loginResponse?.message ?: "Server not responding"
                    customDialogFailed(message)
                }

            }
        }
    }

    private fun customDialogSuccess(message: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.success_dialog)

        val messageTextView = dialog.findViewById<TextView>(R.id.message)
        messageTextView.text = message

        val btnOk = dialog.findViewById<Button>(R.id.ok_btn_id)
        btnOk.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun customDialogFailed(message: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.failed_dialog)

        val messageTextView = dialog.findViewById<TextView>(R.id.message)
        messageTextView.text = message

        val btnOk = dialog.findViewById<Button>(R.id.ok_btn_id)
        btnOk.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            dialog.dismiss()
        }
        dialog.show()
    }

    fun toRegisterClick (view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}