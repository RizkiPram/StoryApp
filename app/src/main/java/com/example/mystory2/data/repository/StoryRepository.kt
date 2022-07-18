package com.example.mystory2.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.mystory2.data.StoryRemoteMediator
import com.example.mystory2.data.api.ApiService
import com.example.mystory2.database.StoryDatabase
import com.example.mystory2.response.ListStoryItem

class StoryRepository (private val storyDatabase: StoryDatabase, private val context: Context, private val apiService: ApiService) {
    @OptIn(ExperimentalPagingApi::class)
    fun getUserStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService,context),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}