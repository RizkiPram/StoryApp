package com.example.mystory2.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystory2.data.api.ApiConfig
import com.example.mystory2.data.local.UserPreferences
import com.example.mystory2.helper.Event
import com.example.mystory2.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val context: Context) : ViewModel() {


    private val _login = MutableLiveData<Boolean>()
    val login: LiveData<Boolean> = _login
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toast = MutableLiveData<Event<Boolean>>()
    val toast: LiveData<Event<Boolean>> = _toast

    private var sharedPreference: UserPreferences? = null

    companion object {
        private const val TAG = "LoginViewModel"
    }

    fun userLogin(email: String?, password: String?) {
        sharedPreference = UserPreferences(context)
        _isLoading.value = true
        viewModelScope.launch {
            val service = ApiConfig.getApiService().login(
                email,
                password
            )

            service.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        sharedPreference?.saveString(
                            "token",
                            "bearer " + (responseBody?.loginResult?.token)
                        )

                        if (responseBody?.error == false && responseBody.message == "success") {
                            _login.value = true
                            _toast.value = Event(false)
                        }

                        if (responseBody?.error == true) {
                            _isLoading.value = false
                            _login.value = false
                        }


                    } else {
                        Log.e(TAG, "onResponse: ${response.message()}")
                        _isLoading.value = false
                        _login.value = false
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _login.value = false
                    _isLoading.value = false
                    _toast.value = Event(true)
                }
            })
        }
    }


}