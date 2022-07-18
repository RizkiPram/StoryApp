package com.example.mystory2.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystory2.data.api.ApiConfig
import com.example.mystory2.response.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel:ViewModel() {
    private val _register = MutableLiveData<Boolean>()
    val register: LiveData<Boolean> = _register

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val service = ApiConfig.getApiService().register(
                name,
                email,
                password,
            )
            service.enqueue(object : Callback<RegisterResponse> {

                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            if (!responseBody.error && responseBody.message == "User created") {
                                _register.value = true
                            }
                        }
                    } else {
                        Log.e(TAG, "onResponse: ${response.message()}")
                        _isLoading.value = false
                        _register.value = false
                    }
                }
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _isLoading.value = false
                    _register.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }
    companion object {
        private const val TAG = "RegisterViewModel"
    }
}