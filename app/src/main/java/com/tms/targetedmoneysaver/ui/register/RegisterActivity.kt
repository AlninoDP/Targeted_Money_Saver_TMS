package com.tms.targetedmoneysaver.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.databinding.ActivityRegisterBinding
import com.tms.targetedmoneysaver.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            btnSignUp.setOnClickListener(this@RegisterActivity)
            btnGoogleRegister.setOnClickListener(this@RegisterActivity)
            tvGotoLogin.setOnClickListener(this@RegisterActivity)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_sign_up -> {
                // TODO: Register Logic
                val email = binding.etRegisterEmail.text.toString()
                val password = binding.etRegisterPassword.text.toString()
                val passwordConfirmation = binding.etRegisterPasswordConfirmation.text.toString()
                if (email.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()) {
                    return
                }

                // check password confirmation
                if (password != passwordConfirmation) {
                    return
                }

            }
            R.id.btn_google_register -> {
                // TODO: Google Login Logic

            }
            R.id.tv_goto_login ->{
                val intent = Intent(this, LoginActivity::class.java)
                // Prevents the creation of multiple instances
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }
    }
}