package com.example.spotify_clone.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.spotify_clone.Models.UserModel
import com.example.spotify_clone.Repository.DataRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.delay
import java.util.*

class SignupViewModel() : ViewModel() {

    private lateinit var email:String
    private lateinit var password:String
    private lateinit var birthMonth:String
    private lateinit var birthYear:String
    private lateinit var birthDay:String
    private lateinit var gender:String
    private lateinit var username:String
    private lateinit var repo:DataRepository
    private var isUserCreated:MutableLiveData<UserModel?> = MutableLiveData()
    private var emailIsExistOrNot:MutableLiveData<Boolean?> = MutableLiveData()
    init {
        repo=DataRepository.getInstance()
//        isEncrypted.value=null
    }

    fun setBirthDate(dayOfMonth: Int, month: Int, year: Int) {
        this.birthDay=dayOfMonth.toString()
        this.birthMonth=month.toString()
        this.birthYear=year.toString()
    }
    fun setEmail(email: String) { this.email=email }
    fun setPassword(password: String) { this.password=password }
    fun setGender(gender: String) { this.gender=gender }

    suspend fun createUser() {

        val hashedPassword:String=BCrypt.withDefaults().hashToString(6,password.toCharArray())
        val user=UserModel(email,hashedPassword, Timestamp(Date(birthYear.toInt(),birthMonth.toInt(),birthDay.toInt())),gender,username,null)
        repo.createUser(user)
        delay(2000)
        GetUser()

    }
    fun GetUser(): MutableLiveData<UserModel?> {

        this.isUserCreated.postValue(repo.GetUser())
        return isUserCreated
    }

    fun setUsername(username: String) {
        this.username=username
    }

    fun isEncryptionDone(): MutableLiveData<UserModel?> {
        return isUserCreated
    }

    suspend fun isEmailAlreadyExist(email: String) {
        repo.isEmailAlreadyExist(email)
        delay(1000)
        emailIsExistOrNot.postValue(repo.GetIsEmailExists())
//<<<<<<< HEAD
//=======
        //url = git@github.com:vickatGit/Spotify_Clone.git
//>>>>>>> e04c1ed (conflicts removed)
    }
    fun GetEmailExists(): MutableLiveData<Boolean?> {
        return emailIsExistOrNot
    }


}
