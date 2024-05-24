package com.ammartech.storyapplication.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ammartech.storyapplication.data.pref.UserModel
import com.ammartech.storyapplication.data.repository.UserRepository

class DetailViewModel(private val repository: UserRepository) : ViewModel() {
    val detail = repository.detail

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getDetailStory(
        token: String,
        id: String
    ) = repository.detailStory(token, id)
}