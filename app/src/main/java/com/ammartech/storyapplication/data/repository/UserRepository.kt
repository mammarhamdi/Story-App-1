package com.ammartech.storyapplication.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.ammartech.storyapplication.data.pref.UserModel
import com.ammartech.storyapplication.data.pref.UserPreference
import com.ammartech.storyapplication.data.remote.response.AddStoryResponse
import com.ammartech.storyapplication.data.remote.response.DetailResponse
import com.ammartech.storyapplication.data.remote.response.ErrorResponse
import com.ammartech.storyapplication.data.remote.response.ListStoryItem
import com.ammartech.storyapplication.data.remote.response.LoginResponse
import com.ammartech.storyapplication.data.remote.response.RegisterResponse
import com.ammartech.storyapplication.data.remote.response.Story
import com.ammartech.storyapplication.data.remote.response.StoryResponse
import com.ammartech.storyapplication.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    private val _detail = MutableLiveData<Story>()
    val detail: LiveData<Story> = _detail

    fun registerUser(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultApi<RegisterResponse>> = liveData {
        emit(ResultApi.Loading)
        try {
            val response = apiService.registerUser(name, email, password)
            emit(ResultApi.Success(response))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string()?.let { errorBody ->
                Gson().fromJson(errorBody, ErrorResponse::class.java).message
            } ?: e.message()
            emit(ResultApi.Error(errorMessage))
        }
    }

    fun loginUser(
        email: String,
        password: String
    ): LiveData<ResultApi<LoginResponse>> = liveData {
        emit(ResultApi.Loading)
        try {
            val response = apiService.loginUser(email, password)
            emit(ResultApi.Success(response))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string()?.let { errorBody ->
                Gson().fromJson(errorBody, ErrorResponse::class.java).message
            } ?: e.message()
            emit(ResultApi.Error(errorMessage))
        }
    }

    fun getStory(token: String) {
        val client = apiService.getStory("Bearer $token")
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>
            ) {
                if (response.isSuccessful) {
                    _listStory.value = response.body()?.listStory
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun detailStory(token: String, id: String) {
        val client = apiService.detailStory("Bearer $token", id)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>
            ) {
                if (response.isSuccessful) {
                    _detail.value = response.body()?.story!!
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun addStory(token: String, imageFile: File, description: String) = liveData {
        emit(ResultApi.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("photo", imageFile.name, requestImageFile)
        try {
            val successResponse = apiService.addStory("Bearer $token", multipartBody, requestBody)
            emit(ResultApi.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
            emit(ResultApi.Error(errorResponse.message))
        }
    }


    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        private const val TAG = "ViewModel"
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}