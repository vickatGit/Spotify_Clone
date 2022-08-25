package com.example.spotify_clone

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    toolbar.setTitleTextColor(Color.WHITE)
    toolbar.title = "Toolbar"
    setSupportActionBar(toolbar)
    findViewById<AppBarLayout>(R.id.appbar).addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
      if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
        Toast.makeText(this,"collapsed",Toast.LENGTH_SHORT).show()
        toolbar.setBackgroundResource(R.drawable.home_gradient)
      } else if (verticalOffset == 0) {
        toolbar.setBackgroundResource(0)
        Toast.makeText(this,"expzndes",Toast.LENGTH_SHORT).show()
      }
      else if(verticalOffset==370){
        toolbar.setBackgroundResource(0)
        val anim=AnimationUtils.loadAnimation(this,R.anim.old_slide_in_animation)
//                toolbar.setBackgroundResource(R.drawable.home_gradient).startAnimation(anim)

        Log.d("TAG", "onCreate: "+verticalOffset.toString())
      }
    })


  }

}