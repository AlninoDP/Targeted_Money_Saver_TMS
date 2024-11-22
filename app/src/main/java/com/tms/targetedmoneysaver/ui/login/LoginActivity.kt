package com.tms.targetedmoneysaver.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.databinding.ActivityLoginBinding
import com.tms.targetedmoneysaver.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            btnSignIn.setOnClickListener(this@LoginActivity)
            btnGoogleLogin.setOnClickListener(this@LoginActivity)
            tvForgetPassword.setOnClickListener(this@LoginActivity)
            tvGotoSignup.setOnClickListener(this@LoginActivity)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_sign_in -> {
                // TODO: Login Logic
                val email = binding.etLoginEmail.text.toString()
                val password = binding.etLoginPassword.text.toString()
                if (email.isEmpty() || password.isEmpty()) {
                    return
                }
            }

            R.id.btn_google_login -> {
                // TODO: Google Login Logic

            }

            R.id.tv_forget_password -> {
                // TODO: Go To Forgot Password Activity
            }

            R.id.tv_goto_signup -> {
                val intent = Intent(this, RegisterActivity::class.java)
                // Prevents the creation of multiple instances
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }
    }
}