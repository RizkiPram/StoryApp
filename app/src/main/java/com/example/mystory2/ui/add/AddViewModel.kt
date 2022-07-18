package com.example.mystory2.ui.add

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystory2.data.api.ApiConfig
import com.example.mystory2.data.local.UserPreferences
import com.example.mystory2.helper.reduceFileImage
import com.example.mystory2.response.AddResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddViewModel(private val context: Context): ViewModel() {

    private val _add = MutableLiveData<Boolean>()
    val add: LiveData<Boolean> = _add

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "AddStoryUserViewModel"
    }


    var description : String? = null
    var getFile: File? = null

    private  var sharedPreference: UserPreferences? = null

    fun getFileResult(getFiles: File?){
        getFile = getFiles
    }

    fun getDescriptionResult(descriptions : String?){
        description = descriptions
    }

    fun uploadImage() {
        _isLoading.value = true
        sharedPreference = UserPreferences(context)
        viewModelScope.launch {
            if (getFile != null) {

                val file = reduceFileImage(getFile as File)

                val description = description?.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                val service = ApiConfig.getApiService().addStories(
                    sharedPreference?.getPreferenceString("token"),
                    imageMultipart,
                    description
                )

                service.enqueue(object : Callback<AddResponse> {
                    override fun onResponse(
                        call: Call<AddResponse>,
                        response: Response<AddResponse>
                    ) {
                        _isLoading.value = false

                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                _add.value = true
                                _isLoading.value = false
                            }

                        } else {
                            _add.value = false
                            _isLoading.value = false
                            Log.e(TAG, "onResponse: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<AddResponse>, t: Throwable) {

                        _isLoading.value = false
                        Log.e(TAG, "onResponse: ${t.message}")
                    }
                })
            } else {

                _isLoading.value = false
            }
        }

    }
}