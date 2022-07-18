package com.example.mystory2.ui.maps

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystory2.data.api.ApiConfig
import com.example.mystory2.data.local.UserPreferences
import com.example.mystory2.response.ListStoryMapsItem
import com.example.mystory2.response.StoryMapsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(private val context: Context): ViewModel() {
    private lateinit var preferences: UserPreferences

    private val _userMap = MutableLiveData<List<ListStoryMapsItem>>()
    val userMap: LiveData<List<ListStoryMapsItem>> = _userMap


    fun getMap(){
        preferences= UserPreferences(context)

        val client=ApiConfig.getApiService().getMaps(preferences.getPreferenceString("token"),1)
        client.enqueue(object : Callback<StoryMapsResponse>{
            override fun onResponse(
                call: Call<StoryMapsResponse>,
                response: Response<StoryMapsResponse>
            ) {
                    val responseBody=response.body()
                if (response.isSuccessful){
                    _userMap.value=responseBody?.listStory
                }else{
                    Log.d(TAG,response.message())
                }
            }
            override fun onFailure(call: Call<StoryMapsResponse>, t: Throwable) {
                t.message?.let { Log.e(TAG, it) }
            }
        })
    }
    companion object{
        const val TAG="MapViewModel"
    }
}