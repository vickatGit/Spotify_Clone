package com.example.spotify_clone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.adamratzman.spotify.*
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.Token
import com.example.spotify_clone.Fragment.HomeFragment
import com.example.spotify_clone.Fragment.PlaylistFragment
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SpotifyActivity : AppCompatActivity() {


    var fragReciever:BroadcastReceiver=object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val id=intent?.getStringExtra("id")
            val type=intent?.getStringExtra("type")
            if(type=="playlist") {
                val playFrag=PlaylistFragment()
                val bundle=Bundle()
                bundle.putString("id",id)
                bundle.putString("type",type)
                playFrag.setArguments(bundle)
                supportFragmentManager.beginTransaction().replace(R.id.spotify_frame,playFrag).addToBackStack(null).commit()

            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(this).registerReceiver(fragReciever, IntentFilter("launchPlaylist"))
        setContentView(R.layout.activity_spotify)

        val homeFragment=HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.spotify_frame,homeFragment).addToBackStack(null).commit()
        Log.d("TAG", "onCreate: backstack"+supportFragmentManager.backStackEntryCount)
    }

    override fun onBackPressed() {


        Log.d("TAG", "backpressed: backstack"+supportFragmentManager.backStackEntryCount+"and fragments are ${supportFragmentManager.fragments}")
        if(supportFragmentManager.backStackEntryCount >0)
            supportFragmentManager.popBackStack()
        else
            super.onBackPressed()
    }
}