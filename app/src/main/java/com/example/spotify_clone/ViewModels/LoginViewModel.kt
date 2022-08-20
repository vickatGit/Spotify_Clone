package com.example.spotify_clone.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.spotify_clone.Models.UserModel
import com.example.spotify_clone.Repository.DataRepository
import com.example.spotify_clone.SignUpActivity


class LoginViewModel : ViewModel() {

    private lateinit var repo: DataRepository
    private var isUser: MutableLiveData<UserModel> = MutableLiveData()
    init {
        repo= DataRepository.getInstance()
    }
    suspend fun isUserExist(email: String, password: String) {
        repo.isUserExist(email, password)
//        delay(3000)
        isUser.postValue(repo.GetUser())
        Log.d("TAG", "isUserExist: "+repo.GetUser().toString())


    }

    fun getUser(): MutableLiveData<UserModel> {
        return isUser
    }
}