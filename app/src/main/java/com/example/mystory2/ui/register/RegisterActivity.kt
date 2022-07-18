package com.example.mystory2.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.mystory2.R
import com.example.mystory2.data.local.UserPreferences
import com.example.mystory2.databinding.ActivityRegisterBinding
import com.example.mystory2.ui.login.LoginActivity
import kotlinx.coroutines.NonCancellable.start

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var sharedPreference: UserPreferences? = null
    private val viewModel:RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreference = UserPreferences(this)

        setupViewModel()
        setupBtnLogin()
        setupEditText()
        settingButton()
        animation()
    }
    private fun animation(){
        with(binding){
            val email=ObjectAnimator.ofFloat(myEtEmailRegister, View.ALPHA, 1f).setDuration(500)
            val name=ObjectAnimator.ofFloat(myEtNameRegister, View.ALPHA, 1f).setDuration(500)
            val password=ObjectAnimator.ofFloat(myEtPasswordRegister, View.ALPHA, 1f).setDuration(500)
            AnimatorSet().apply {
                playSequentially(email,name,password)
                start()
            }
        }

    }
    private fun setupViewModel(){
        viewModel.register.observe(this) {
            moveToLogin(it)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }
    private fun setupBtnLogin(){
        with(binding) {
            myBtnRegister.setOnClickListener {
                val name = myEtNameRegister.text.toString()
                val email = myEtEmailRegister.text.toString()
                val password = myEtPasswordRegister.text.toString()

                viewModel.register(name, email, password)

            }
        }

    }
    private fun settingButton() {
        with(binding) {
            val email = myEtEmailRegister.text
            val name = myEtNameRegister.text
            val password = myEtPasswordRegister.text
            myBtnRegister.isEnabled =
                email != null && password != null && name != null && binding.myEtPasswordRegister.text.toString().length > 6
        }
    }
    private fun setupEditText() {
        with(binding) {
            myEtEmailRegister.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    settingButton()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            myEtNameRegister.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    settingButton()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
            myEtPasswordRegister.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    settingButton()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
        }
    }
    private fun moveToLogin(data:Boolean){
        if (data) {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@RegisterActivity, R.string.failed, Toast.LENGTH_SHORT)
                .show()
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}