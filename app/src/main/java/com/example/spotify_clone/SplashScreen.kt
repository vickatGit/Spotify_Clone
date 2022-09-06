package com.example.spotify_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.example.spotify_clone.LocalDatabase.SqlUserEntity
import com.example.spotify_clone.LocalDatabase.UserLoginSignUpDatabase

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val user: SqlUserEntity? = UserLoginSignUpDatabase.getInstance(this)?.getUserLoginDao()?.getUser()
        Log.d("tag", "onCreate: checking")
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if(user?.userReference!=null){
                Log.d("TAG", "onCreate: the user is $user")
                val intent= Intent(this,SpotifyActivity::class.java)
                intent.putExtra(SignUpLoginActivity.USER_REF,user?.userReference)
                startActivity(intent)
                finish()
            }else{
                val intent= Intent(this,SignUpLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        },1500)

    }
}