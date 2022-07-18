package com.example.mystory2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystory2.data.repository.StoryRepository
import com.example.mystory2.response.ListStoryItem

class MainViewModel(userStoryRepository: StoryRepository):ViewModel() {
    val userStory: LiveData<PagingData<ListStoryItem>> =
        userStoryRepository.getUserStory().cachedIn(viewModelScope)
}