package com.ammartech.storyapplication.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ammartech.storyapplication.data.remote.response.RegisterResponse
import com.ammartech.storyapplication.data.repository.ResultApi
import com.ammartech.storyapplication.data.repository.UserRepository

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun registerUser(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultApi<RegisterResponse>> {
        return userRepository.registerUser(name, email, password)
    }
}