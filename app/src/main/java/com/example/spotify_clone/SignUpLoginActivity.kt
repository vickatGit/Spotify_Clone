package com.example.spotify_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.spotify_clone.LocalDatabase.SqlUserEntity
import com.example.spotify_clone.LocalDatabase.UserLoginSignUpDatabase

class SignUpLoginActivity : AppCompatActivity() {

  private lateinit var intro:TextView
  private lateinit var signup:Button
  private lateinit var login:Button

  companion object{
    val USER_REF="user reference"
    private val TAG="tag"
  }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_up_login)
    initialiseViews()


    signup.setOnClickListener {
      val intent= Intent(this,SignUpActivity::class.java)
      startActivity(intent)
    }
    login.setOnClickListener {
      val intent= Intent(this,LoginActivity::class.java)
      startActivity(intent)
    }
  }

  private fun initialiseViews() {
    intro=findViewById(R.id.intro)
    signup=findViewById(R.id.signup)
    login=findViewById(R.id.login)
    intro.setTypeface(ResourcesCompat.getFont(this,R.font.circular_std_bold))
    signup.setTypeface(ResourcesCompat.getFont(this,R.font.circular_std_bold))
    login.setTypeface(ResourcesCompat.getFont(this,R.font.circular_std_bold))
  }
}