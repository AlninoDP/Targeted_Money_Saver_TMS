package com.tms.targetedmoneysaver.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.data.Result.*
import com.tms.targetedmoneysaver.databinding.ActivityLoginBinding
import com.tms.targetedmoneysaver.ui.ViewModelFactory
import com.tms.targetedmoneysaver.ui.home.HomeActivity
import com.tms.targetedmoneysaver.ui.register.RegisterActivity
import es.dmoral.toasty.Toasty

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewModelFactory = ViewModelFactory.getInstance(this)
        loginViewModel = viewModels<LoginViewModel> { viewModelFactory }.value

        loginViewModel.loginResult.observe(this@LoginActivity) { result ->
            when (result) {
                is Loading -> {
                    showLoading(result.state)
                }

                is Success -> {
                    val message = result.data
                    Toasty.success(this, message, Toast.LENGTH_SHORT, true).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }

                is Failure -> {
                    Toasty.error(
                        this,
                        result.throwable.message.toString(),
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                }
            }
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
                val email = binding.etLoginEmail.text.toString()
                val password = binding.etLoginPassword.text.toString()
                if (validateInput(email, password)) {
                    loginViewModel.loginUser(email, password)
                }
            }

            R.id.btn_google_login -> {
                // TODO: Google Login Logic
                loginViewModel.getToken()
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

    // Validate email and password
    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etLoginEmail.error = getString(R.string.invalid_email)
            return false
        }
        if (password.isEmpty() || password.length < 8) {
            binding.etLoginPassword.error = getString(R.string.invalid_password)
            binding.loginPasswordLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
            return false
        }
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loginProgressBar.visibility = View.VISIBLE
            binding.btnSignIn.visibility = View.GONE
        } else {
            binding.loginProgressBar.visibility = View.GONE
            binding.btnSignIn.visibility = View.VISIBLE
        }
    }
}