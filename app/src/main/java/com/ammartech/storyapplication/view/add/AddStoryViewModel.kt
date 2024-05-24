package com.ammartech.storyapplication.view.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ammartech.storyapplication.data.pref.UserModel
import com.ammartech.storyapplication.data.repository.UserRepository
import java.io.File

class AddStoryViewModel(private val repository: UserRepository) : ViewModel()  {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun addStory(token: String, file: File, description: String) = repository.addStory(token, file, description)
}