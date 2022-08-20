package com.example.spotify_clone.Models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val email:String, val password: String,
    val birth:Timestamp?,
    val gender:String,
    val username:String,
    var userRef:String?) : Parcelable{
    fun setuserReference(userRef: String) {
        this.userRef=userRef
    }
}