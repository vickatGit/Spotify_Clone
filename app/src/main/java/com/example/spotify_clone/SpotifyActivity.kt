package com.example.spotify_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.adamratzman.spotify.*
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.Token
import com.example.spotify_clone.Fragment.HomeFragment
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SpotifyActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)

        val homeFragment=HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.spotify_frame,homeFragment).commit()




    }
}