package com.dicoding.glucopal.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.dicoding.glucopal.R
import com.dicoding.glucopal.databinding.ActivityRegisterBinding
import com.dicoding.glucopal.ui.ViewModelFactory
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.dicoding.glucopal.ui.login.LoginActivity
import com.dicoding.glucopal.ui.welcome.WelcomeActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this@RegisterActivity)


        setupView()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 0f, 1f)
        title.duration = 500
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 0f, 1f)
        name.duration = 500
        val boxName = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 0f, 1f)
        name.duration = 500
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f)
        email.duration = 500
        val boxEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f)
        email.duration = 500
        val pass = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f)
        pass.duration = 500
        val boxPass = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 0f, 1f)
        pass.duration = 500
        val confirmPass = ObjectAnimator.ofFloat(binding.retypePasswordTextView, View.ALPHA, 0f, 1f)
        pass.duration = 500
        val boxConfirmPass = ObjectAnimator.ofFloat(binding.retypePasswordEditTextLayout, View.ALPHA, 0f, 1f)
        pass.duration = 500
        val btn = ObjectAnimator.ofFloat(binding.regisButton, View.ALPHA, 0f, 1f)
        btn.duration = 500
        val text = ObjectAnimator.ofFloat(binding.toRegisterText, View.ALPHA, 0f, 1f)
        btn.duration = 500
        val text2 = ObjectAnimator.ofFloat(binding.toRegister, View.ALPHA, 0f, 1f)
        btn.duration = 500

        val animatorSetSequential = AnimatorSet()
        animatorSetSequential.playSequentially(
            title,
            name,
            boxName,
            email,
            boxEmail,
            pass,
            boxPass,
            confirmPass,
            boxConfirmPass,
            btn,
            text,
            text2
        )

        animatorSetSequential.start()
    }

    private fun obtainViewModel(activity: AppCompatActivity): RegisterViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(RegisterViewModel::class.java)
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
        binding.regisButton.setOnClickListener {
            val username = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword  = binding.retypePasswordEditText.text.toString()

            if (password.length >= 7 && confirmPassword.length >= 7) {

                if (password == confirmPassword) {
                    // Kirim data registrasi ke ViewModel
                    viewModel.register(username, email, password, confirmPassword)
                    //Log.d("ILHAN", "name: ${username}, email: ${email}, pass: ${password}, repeatPassword: $confirmPassword gender:${gender}")
                } else {
                    val message = "Passwords do not match"
                    customDialogFailed(message)
                    //Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                val message = "Invalid email format or password must be at least 7 characters long"
                customDialogFailed(message)
                //Toast.makeText(this, "Invalid email format or password must be at least 7 characters long", Toast.LENGTH_SHORT).show()
            }

            //Log.d("ILHAN", "name: ${username}, email: ${email}, pass: ${password}, gender:${gender}")
            //viewModel.register(username, email, password, confirmPassword)

            viewModel.registerResponse.observe(this) {registerResponse ->
                if (registerResponse != null) {
                    if (registerResponse.success == "0") {

                        val message ="Registration failed"
                        customDialogFailed(message)

                    } else {

                        val message = registerResponse.message ?: "Server not responding"
                        customDialogSuccess(message)

                    }
                } else {

                    val message = "Email is already registered"
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
            startActivity(Intent(this, WelcomeActivity::class.java))
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
            startActivity(Intent(this, RegisterActivity::class.java))
            dialog.dismiss()
        }
        dialog.show()
    }

    fun toLoginClick (view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}