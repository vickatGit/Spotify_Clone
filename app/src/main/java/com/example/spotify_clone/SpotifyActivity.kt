package com.example.spotify_clone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.adamratzman.spotify.*
import com.bumptech.glide.Glide
import com.example.spotify_clone.Fragment.HomeFragment
import com.example.spotify_clone.Fragment.PlaylistFragment
import com.example.spotify_clone.Fragment.SearchFragment
import com.example.spotify_clone.Fragment.SearchSongFragment
import com.example.spotify_clone.Models.ApiRelatedModels.TrackModel
import com.example.spotify_clone.R.id.search_song_fragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class SpotifyActivity : AppCompatActivity() {

    private lateinit var controllerSongName:TextView
    private lateinit var songThumb:ImageView
    private lateinit var songArtist:TextView
    private lateinit var songState:ToggleButton
    private lateinit var songProgress:ProgressBar
    private lateinit var bottomNavigationView:BottomNavigationView
    companion object{
        public lateinit var exo:ExoPlayer
        public val FRAG_RECIEVER="launchPlaylist"
    }

    var fragReciever:BroadcastReceiver=object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val id=intent?.getStringExtra("id")
            val type=intent?.getStringExtra("type")
            if(type=="search_fragment"){
                val searchFrag=SearchSongFragment()
                supportFragmentManager.beginTransaction().replace(R.id.spotify_frame,searchFrag).addToBackStack(null).commit()
            }
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
    var trackReciever:BroadcastReceiver=object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val bundle=intent?.getBundleExtra("track")
            val track: TrackModel? =bundle?.getParcelable("track")
            Glide.with(this@SpotifyActivity).load(track?.image).into(songThumb)
            Log.d("TAG", "onReceive: track"+track)
            controllerSongName.text=track?.name
            songArtist.text=track?.artist
            exo.playWhenReady=true
            val song=MediaItem.Builder().setUri(track?.previewUrl).build()
            val song2=MediaItem.Builder().setUri("https://p.scdn.co/mp3-preview/f1266c8f300a0b5a08107834a41cd069d4fb36e1?cid=a7ade92373684af7b78d8382b1031827").build()
            exo.addMediaItem(song)
            exo.addMediaItem(song2)
            exo.prepare()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)
        initialise()
        LocalBroadcastManager.getInstance(this).registerReceiver(fragReciever, IntentFilter(FRAG_RECIEVER))
        LocalBroadcastManager.getInstance(this).registerReceiver(trackReciever, IntentFilter("recieveTrack"))

        val pos=bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    val homeFragment=HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.spotify_frame,homeFragment).addToBackStack(null).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.search -> {
                    val searchFragment=SearchFragment(this)
                    supportFragmentManager.beginTransaction().replace(R.id.spotify_frame,searchFragment).addToBackStack(null).commit()
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }

        val homeFragment=HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.spotify_frame,homeFragment).addToBackStack(null).commit()


    }
    fun initialise(){
        exo=ExoPlayer.Builder(this).build()
        songThumb=findViewById(R.id.song_controller_thumbnail)
        songArtist=findViewById(R.id.song_artist)
        controllerSongName=findViewById(R.id.song_name)
        songProgress = findViewById(R.id.song_progress)
        songProgress.progressTintList= ColorStateList.valueOf(Color.WHITE)
        bottomNavigationView=findViewById(R.id.bottomNavigationView)
    }


    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount >0)
            supportFragmentManager.popBackStack()
        if(supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount)== supportFragmentManager.findFragmentById(search_song_fragment)) {
            if (!findViewById<androidx.appcompat.widget.SearchView>(R.id.search_song).isIconified)
                findViewById<androidx.appcompat.widget.SearchView>(R.id.search_song).visibility = View.INVISIBLE
        }
        else
            super.onBackPressed()
    }
}