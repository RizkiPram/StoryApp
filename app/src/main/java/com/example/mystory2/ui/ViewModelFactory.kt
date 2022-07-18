package com.example.mystory2.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystory2.di.Injection
import com.example.mystory2.ui.add.AddViewModel
import com.example.mystory2.ui.login.LoginViewModel
import com.example.mystory2.ui.main.MainViewModel
import com.example.mystory2.ui.maps.MapViewModel


class ViewModelFactory(private val context: Context): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(context) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) ->{
                MainViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(AddViewModel::class.java) ->{
                AddViewModel(context) as T
            }
            modelClass.isAssignableFrom(MapViewModel::class.java) ->{
                MapViewModel(context) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}