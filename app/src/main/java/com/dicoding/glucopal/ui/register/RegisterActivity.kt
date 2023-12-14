package com.dicoding.glucopal.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.dicoding.glucopal.R
import com.dicoding.glucopal.databinding.ActivityRegisterBinding
import com.dicoding.glucopal.ui.ViewModelFactory
import com.dicoding.glucopal.utils.Gender

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
        //playAnimation()
    }

    /*private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 0f, 1f)
        title.duration = 500
        val name = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 0f, 1f)
        name.duration = 500
        val email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 0f, 1f)
        email.duration = 500
        val pass = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 0f, 1f)
        pass.duration = 500
        val btn = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 0f, 1f)
        btn.duration = 500

        val animatorSetSequential = AnimatorSet()
        animatorSetSequential.playSequentially(
            title,
            name,
            email,
            pass,
            btn
        )

        animatorSetSequential.start()
    }*/

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
            /*val gender = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.rb_male -> Gender.M
                R.id.rb_female -> Gender.F
                else -> null
            }*/
            if (password == confirmPassword) {
                val gender = when (binding.radioGroup.checkedRadioButtonId) {
                    R.id.rb_male -> Gender.M
                    R.id.rb_female -> Gender.F
                    else -> null
                }

                // Cek apakah gender sudah dipilih
                if (gender != null) {
                    // Kirim data registrasi ke ViewModel
                    viewModel.register(username, email, password, gender)
                    Log.d("ILHAN", "name: ${username}, email: ${email}, pass: ${password}, gender:${gender}")
                } else {
                    Toast.makeText(this, "Pilih jenis kelamin", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show()
            }

            //Log.d("ILHAN", "name: ${username}, email: ${email}, pass: ${password}, gender:${gender}")
            //viewModel.register(username, email, password, gender!!)

            viewModel.registerResponse.observe(this) {registerResponse ->
                if (registerResponse != null) {
                    if (registerResponse.success == "0") {

                        Toast.makeText(this, "${registerResponse.message}", Toast.LENGTH_SHORT).show()

                    } else {

                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
                            setMessage("Akun dengan ${binding.emailEditText.text} sudah jadi nih. Yuk, login dan ceritakan ceritamu.")
                            setPositiveButton("Lanjut") { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                } else {

                    Toast.makeText(this, "Akun sudah terdaftar", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

}