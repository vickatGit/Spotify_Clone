package com.example.spotify_clone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.adamratzman.spotify.*
import com.bumptech.glide.Glide
import com.example.spotify_clone.Fragment.HomeFragment
import com.example.spotify_clone.Fragment.PlaylistFragment
import com.example.spotify_clone.Fragment.SearchFragment
import com.example.spotify_clone.Fragment.SearchSongFragment
import com.example.spotify_clone.Models.ApiRelatedModels.TrackModel
import com.example.spotify_clone.R.id.search_song_fragment
import com.example.spotify_clone.ViewModels.SpotifyViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.extractor.mp4.Track
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.cache.DefaultContentMetadata
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.grpc.Metadata
import kotlin.time.ExperimentalTime
import kotlin.time.minutes
import kotlin.time.seconds

class SpotifyActivity : AppCompatActivity() {

    private lateinit var controllerSongName:TextView
    private lateinit var songThumb:ImageView
    private lateinit var songArtist:TextView
    private lateinit var playPause:ToggleButton
    private lateinit var songProgress:ProgressBar
    private lateinit var bottomNavigationView:BottomNavigationView
    private lateinit var viewModel:SpotifyViewModel
    private lateinit var playerview:PlayerControlView
    private var playlistTracks=ArrayList<TrackModel>(1)
    companion object{
        public lateinit var exo:ExoPlayer
        public val TAG="tag"
        public val FRAG_RECIEVER="launchPlaylist"
    }
    private val listeners = object:Player.Listener{
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            Log.d(TAG, "onMediaItemTransition: ")
            super.onMediaItemTransition(mediaItem, reason)
            val pos=playerview.player?.currentMediaItemIndex
            if(playlistTracks.size>0) {
                Glide.with(songThumb).load(playlistTracks.get(pos!!).image).into(songThumb)
                songArtist.text = playlistTracks.get(pos!!)?.artist
                controllerSongName.text = playlistTracks.get(pos!!)?.name
            }
        }


        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)



        }
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
            val action=bundle?.getString("clear_and_play")
            exo.clearMediaItems()
            playerview.player=exo
            exo.playWhenReady=true
            justAddSong(track)

        }
    }
    var playlistReciever:BroadcastReceiver=object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            exo.clearMediaItems()
            playlistTracks.clear()
            playerview.player=exo
            Log.d(TAG, "onReceive: playlist")
            val trackLists=intent?.getParcelableArrayListExtra<TrackModel>("playlist")

            trackLists?.forEach {
                if(it.previewUrl!=null) {
                    val song = MediaItem.Builder().setUri(it?.previewUrl).setTag("1").build()
                    exo.addMediaItem(song)
                    playlistTracks.add(it)
                }
            }
            exo.prepare()
            exo.playWhenReady=true
            exo.seekToNext()
            exo.play()
        }

    }

    private fun justAddSong(track: TrackModel?) {
        val song=MediaItem.Builder().setUri(track?.previewUrl).build()
        exo.addMediaItem(song)
//        exo.seekToNextMediaItem()
        val totPercent=29000
        val onpercent=(totPercent/100).toInt()
        playerview.setProgressUpdateListener { position, bufferedPosition ->
            Log.d(TAG, "justAddSong: "+totPercent)
            val progress=(position/ onpercent!!).toInt()
            songProgress.setProgress(progress,true)
        }
        Glide.with(songThumb).load(track?.image).into(songThumb)
        songArtist.text=track?.artist
        controllerSongName.text=track?.name
        exo.prepare()
        exo.playWhenReady=true
        exo.play()
        playPause.isChecked=true

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)
        initialise()

        viewModel=ViewModelProvider(this).get(SpotifyViewModel::class.java)
        exo.addListener(listeners)
        LocalBroadcastManager.getInstance(this).registerReceiver(fragReciever, IntentFilter(FRAG_RECIEVER))
        LocalBroadcastManager.getInstance(this).registerReceiver(trackReciever, IntentFilter("recieveTrack"))
        LocalBroadcastManager.getInstance(this).registerReceiver(playlistReciever, IntentFilter(PlaylistFragment.RECIEVE_PLAYLIST))





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
        playPause.setOnClickListener {
            if(!playPause.isChecked){
                if(exo.isPlaying)
                    exo.pause()
            }else{
                if(!exo.isPlaying)
                    exo.play()
            }
        }



    }
    fun initialise(){
        exo=ExoPlayer.Builder(this).build()
        songThumb=findViewById(R.id.song_controller_thumbnail)
        songArtist=findViewById(R.id.song_artist)
        controllerSongName=findViewById(R.id.song_name)
        songProgress = findViewById(R.id.song_progress)
        songProgress.progressTintList= ColorStateList.valueOf(Color.WHITE)
        bottomNavigationView=findViewById(R.id.bottomNavigationView)
        playerview=findViewById(R.id.playerview)
        playPause=findViewById(R.id.play_pause_song)
    }


    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount >0)
            supportFragmentManager.popBackStack()
        else
            super.onBackPressed()
    }

}