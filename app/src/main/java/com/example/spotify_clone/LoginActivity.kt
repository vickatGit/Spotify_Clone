package com.example.spotify_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.spotify_clone.LocalDatabase.SqlUserEntity
import com.example.spotify_clone.LocalDatabase.UserLoginSignUpDatabase
import com.example.spotify_clone.ViewModels.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

  private lateinit var email:EditText
  private lateinit var password:EditText
  private lateinit var acknowledgement:TextView
  private lateinit var login:Button
  private lateinit var viewModel:LoginViewModel

  companion object{
    val USER="user"
  }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    initialiseViews()
    viewModel=ViewModelProvider(this).get(LoginViewModel::class.java)
    email.addTextChangedListener{
      if(password.text.length>0 ){
        login.setBackgroundDrawable(ResourcesCompat.getDrawable(resources,R.drawable.next_button_bg,null))
        login.isEnabled=true
      }else{
        login.setBackgroundDrawable(ResourcesCompat.getDrawable(resources,R.drawable.next_button_error_bg,null))
        login.isEnabled=false
      }
    }
    password.addTextChangedListener{
      if(email.text.length>0){
        login.setBackgroundDrawable(ResourcesCompat.getDrawable(resources,R.drawable.next_button_bg,null))
        login.isEnabled=true
      }else{
        login.setBackgroundDrawable(ResourcesCompat.getDrawable(resources,R.drawable.next_button_error_bg,null))
        login.isEnabled=false
      }
    }
    login.setOnClickListener {

        viewModel.isUserExist(email.text.toString(), password.text.toString()).observe(this@LoginActivity, Observer {
          if(it!=null){
            val bundle=Bundle()
            val intent=Intent(this@LoginActivity,SpotifyActivity::class.java)
            bundle.putParcelable(USER,it)
            intent.putExtra(USER,bundle)
            startActivity(intent)
            UserLoginSignUpDatabase.getInstance(this@LoginActivity)?.getUserLoginDao()?.insertUser(SqlUserEntity(null,it.username,it.userRef!!))
            acknowledgement.visibility= View.INVISIBLE
          }else{
            acknowledgement.visibility= View.VISIBLE
          }
        })

    }

  }

  private fun initialiseViews() {
    email=findViewById(R.id.email)
    password=findViewById(R.id.pass_take_details)
    acknowledgement=findViewById(R.id.acknowledgement)
    acknowledgement.visibility=View.GONE
    login=findViewById(R.id.login)
    login.setBackgroundDrawable(ResourcesCompat.getDrawable(resources,R.drawable.next_button_error_bg,null))
    login.isEnabled=false
  }
}