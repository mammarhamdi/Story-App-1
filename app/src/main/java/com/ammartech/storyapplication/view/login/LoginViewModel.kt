package com.ammartech.storyapplication.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammartech.storyapplication.data.repository.UserRepository
import com.ammartech.storyapplication.data.pref.UserModel
import com.ammartech.storyapplication.data.remote.response.LoginResponse
import com.ammartech.storyapplication.data.repository.ResultApi

import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun loginUser(email: String, password: String): LiveData<ResultApi<LoginResponse>> {
        return userRepository.loginUser(email, password)
    }
}