package com.example.mystory2.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mystory2.R
import com.example.mystory2.data.local.UserPreferences
import com.example.mystory2.databinding.ActivityLoginBinding
import com.example.mystory2.ui.ViewModelFactory
import com.example.mystory2.ui.main.MainActivity
import com.example.mystory2.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var sharedPreference: UserPreferences? = null
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory(this))[LoginViewModel::class.java]
        sharedPreference = UserPreferences(this)


        binding.myBtnLogin.setOnClickListener {
            val strEmail = binding.myEtEmailLogin.text.toString()
            val strPassword = binding.myEtPasswordLogin.text.toString()

            if (strEmail == "" || strPassword == "") {
                Toast.makeText(this@LoginActivity, "Please Enter Details.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                sharedPreference?.saveString("login_status", "1")
                Log.e("LoginActivity","Save status success")
                viewModel.userLogin(strEmail, strPassword)
            }
        }

        binding.tvMoveToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        setupViewModel()
        setupEditText()
        settingBtn()
    }
    private fun setupViewModel(){
        viewModel.login.observe(this) {
            login(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.toast.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }
    }
    private fun setupEditText(){
        with(binding){
            myEtEmailLogin.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    settingBtn()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
            myEtPasswordLogin.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    settingBtn()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
        }
    }
    private fun settingBtn(){
        with(binding) {
            val email = myEtEmailLogin.text
            val password = myEtPasswordLogin.text
            myBtnLogin.isEnabled =
                email != null && password != null && binding.myEtPasswordLogin.text.toString().length > 6
        }
    }
    private fun showToast(isToast: Boolean) {
        val caution = "Failed Cause No Connection"
        if (isToast) {
            Toast.makeText(this@LoginActivity, caution, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun login(value: Boolean) {
        if (value) {

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

            Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this, R.string.failed, Toast.LENGTH_SHORT
            ).show()
        }
    }

}