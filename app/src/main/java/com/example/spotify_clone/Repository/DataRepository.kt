package com.example.spotify_clone.Repository

import android.util.Log
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.spotify_clone.Models.UserModel
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source


class DataRepository {
    val db=FirebaseFirestore.getInstance()
    var user:UserModel?=null
    var isExist:Boolean=false
    var some: DocumentSnapshot? =null




    companion object{

        private var dataRepository= DataRepository()
        fun getInstance(): DataRepository {
            return dataRepository
        }
    }
    fun createUser(user: UserModel) {


        val docRef=db.collection("Users").document()
        user.setuserReference(docRef.toString())
        db.collection("Users").document().set(user).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("TAG", "createUser: Successful")
            }
        }.addOnFailureListener {
            Log.d("TAG", "createUser: failed")
        }
        isUserExist(user.email,user.password)
    }

    fun isUserExist(email: String, password: String) {


        db.collection("Users").whereEqualTo("email",email).limit(1).get().addOnCompleteListener {
            if(it.isSuccessful){

                it.result.documents.forEach {

                    some=it


                }
            }
        }


    }
    fun GetUser():UserModel?{
        if(some!=null) {
            user = UserModel(
                some?.get("email").toString(),
                some?.get("password").toString(),
                null,
                some?.get("gender").toString(),
                some?.get("username").toString(),
                some?.getString("userRef")
            )
        }
        return user
    }
    fun isEmailAlreadyExist(email: String) {


        db.collection("Users").whereEqualTo("email",email).get(Source.SERVER).addOnCompleteListener {

            if(it.isSuccessful) {
                if (it.result.size() == 0) {
                    isExist = false
                }
            }
        }
        Log.d("TAG", "isEmailAlreadyExist:after $isExist")

    }
    fun GetIsEmailExists(): Boolean {
        return isExist
    }
}