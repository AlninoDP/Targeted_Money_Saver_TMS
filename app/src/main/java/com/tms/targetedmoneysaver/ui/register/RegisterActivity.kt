package com.tms.targetedmoneysaver.ui.register

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
import com.tms.targetedmoneysaver.MainActivity
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.data.Result.Failure
import com.tms.targetedmoneysaver.data.Result.Loading
import com.tms.targetedmoneysaver.data.Result.Success
import com.tms.targetedmoneysaver.databinding.ActivityRegisterBinding
import com.tms.targetedmoneysaver.ui.ViewModelFactory
import com.tms.targetedmoneysaver.ui.home.HomeActivity
import com.tms.targetedmoneysaver.ui.login.LoginActivity
import com.tms.targetedmoneysaver.ui.login.LoginViewModel
import es.dmoral.toasty.Toasty

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewModelFactory = ViewModelFactory.getInstance(this)
        registerViewModel = viewModels<RegisterViewModel> { viewModelFactory }.value

        registerViewModel.registerResult.observe(this@RegisterActivity) { result ->
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

                if (validateInput(email, password, passwordConfirmation)){
                    registerViewModel.registerUser(email,password)
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

    // Validate email and password
    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty()) {
            binding.etRegisterEmail.error = getString(R.string.invalid_email)
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etRegisterEmail.error = getString(R.string.invalid_email)
            return false
        }
        if (password.isEmpty() || password.length < 8) {
            binding.etRegisterPassword.error = getString(R.string.invalid_password)
            binding.registerPasswordLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
            return false
        }

        if (confirmPassword != password){
            binding.etRegisterPasswordConfirmation.error = getString(R.string.password_not_match)
            binding.registerPasswordConfirmationLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
            return false
        }
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.registerProgressBar.visibility = View.VISIBLE
            binding.btnSignUp.visibility = View.GONE
        } else {
            binding.registerProgressBar.visibility = View.GONE
            binding.btnSignUp.visibility = View.VISIBLE
        }
    }
}