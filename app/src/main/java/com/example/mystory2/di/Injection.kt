package com.example.mystory2.di

import android.content.Context
import com.example.mystory2.data.api.ApiConfig
import com.example.mystory2.data.repository.StoryRepository
import com.example.mystory2.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database,context,apiService)
    }
}