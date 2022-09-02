package com.example.spotify_clone

import android.content.*
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.adamratzman.spotify.*
import com.bumptech.glide.Glide
import com.example.spotify_clone.Fragment.*
import com.example.spotify_clone.Models.ApiRelatedModels.TrackModel
import com.example.spotify_clone.MusicService.MusicPlayerService
import com.example.spotify_clone.ViewModels.SpotifyViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.material.bottomnavigation.BottomNavigationView

class SpotifyActivity : AppCompatActivity() {

    private lateinit var controllerSongName:TextView
    private lateinit var songThumb:ImageView
    private lateinit var songArtist:TextView
    private lateinit var playPause:ToggleButton
    private lateinit var songProgress:ProgressBar
    private lateinit var bottomNavigationView:BottomNavigationView
    private lateinit var viewModel:SpotifyViewModel

    private lateinit var fragmentContainer:FrameLayout
    private lateinit var musicServiceConnection:MusicPlayerService
    companion object{
        var currentSong:TrackModel?=null
        private var intent:Intent=Intent()
        lateinit var playerview:PlayerControlView
        val UPDATE_SONG_POSITION="update_song_duration"
        val CURRENT_SONG_Duration="current_song_duaration"
        val SET_CURRENT_SONG_INFORMATION="update_song_info"
        val CURRENT_SONG_POSITION="current_song_position"
        var playlistTracks=ArrayList<TrackModel>(1)
        public lateinit var exo:ExoPlayer
        public val TAG="tag"
        public val FRAG_RECIEVER="launchPlaylist"
    }

    private val MusicPlayerServiceConnection=object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val musicBinder:MusicPlayerService.MusicBinder= service as MusicPlayerService.MusicBinder
            musicServiceConnection=musicBinder.getInstanceOfMusicBinder()
            musicServiceConnection.setPlayerview(playerview)
            Log.d(TAG, "onServiceConnected: ")

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected: ")
        }

    }


    var removeFragHistory:BroadcastReceiver=object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.popFragmentHistoty()
        }

    }

    var fragReciever:BroadcastReceiver=object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val id=intent?.getStringExtra("id")
            val type=intent?.getStringExtra("type")
            Log.d(TAG, "onReceive: fragment type"+type)
            if(type=="search_fragment"){
                val searchFrag=SearchSongFragment()

                if(!supportFragmentManager.isDestroyed) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.spotify_frame, searchFrag).addToBackStack(null).commit()
                    viewModel.setCurrentFragment(searchFrag)
                }
            }
            else if(type=="album"){
                val albumFrag=AlbumFragment()
                val bundle=Bundle()
                bundle.putString("id",id)
                bundle.putString("type",type)
                albumFrag.setArguments(bundle)
                if(!supportFragmentManager.isDestroyed) {
                    supportFragmentManager.beginTransaction().replace(R.id.spotify_frame, albumFrag)
                        .addToBackStack(null).commit()
                    viewModel.setCurrentFragment(albumFrag)
                }
            }
            else if(type=="playlist") {
                val playFrag=PlaylistFragment()
                val bundle=Bundle()
                bundle.putString("id",id)
                bundle.putString("type",type)
                playFrag.setArguments(bundle)
                if(!supportFragmentManager.isDestroyed) {
                    supportFragmentManager.beginTransaction().replace(R.id.spotify_frame, playFrag)
                        .addToBackStack(null).commit()
                }
                viewModel.setCurrentFragment(playFrag)
            }
        }

    }
    var trackReciever:BroadcastReceiver=object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val bundle=intent?.getBundleExtra("track")
            val track: TrackModel? =bundle?.getParcelable("track")
            val action=bundle?.getString("clear_and_play")
//            initPlayer()
            justAddSong(track)


        }
    }
    var updateSongDuration:BroadcastReceiver=object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val songDuration=intent?.getIntExtra(CURRENT_SONG_Duration,0)
            songProgress.setProgress(songDuration!!,true)
        }

    }
    var updateCurrentSongInfo:BroadcastReceiver=object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val songInfo=intent?.getIntExtra(CURRENT_SONG_POSITION,0)
            viewModel.setCurrentSong(songInfo)
//            currentSong= playlistTracks.get(exo.currentMediaItemIndex)
                Glide.with(applicationContext).load(playlistTracks.get(songInfo!!).image).into(songThumb)
                songArtist.text = playlistTracks.get(songInfo!!)?.artist
                controllerSongName.text =playlistTracks.get(songInfo!!)?.name
        }

    }

    var playlistReciever:BroadcastReceiver=object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
//            initPlayer()
//            val curpos=intent?.getIntExtra("curpos",0)
            playlistTracks.clear()
            val playlist=intent?.getParcelableArrayListExtra<TrackModel>("playlist")
            val mediaList=ArrayList<MediaItem>(1)
            playlist?.forEach {
                if(it.previewUrl!=null) {
                    mediaList.add(MediaItem.fromUri(it.previewUrl!!))
                    playlistTracks.add(it)
                }
                viewModel.setPlaylistTracks(playlistTracks)
            }
            musicServiceConnection.addAllSOngsAndPlay(mediaList)
        }
    } 

    private fun justAddSong(track: TrackModel?) {
        currentSong=track!!
        val song=MediaItem.Builder().setUri(track?.previewUrl).build()
        exo.addMediaItem(song)

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
        if(viewModel.getCurrentSongPosition()!=null){
            var songInfo=viewModel.getCurrentSongPosition()
            Glide.with(applicationContext).load(playlistTracks.get(songInfo!!).image).into(songThumb)
            songArtist.text = playlistTracks.get(songInfo!!)?.artist
            controllerSongName.text =playlistTracks.get(songInfo!!)?.name
        }
        Log.d("backstack", "onCreate: spotifyactivity"+supportFragmentManager.backStackEntryCount)
        playlistTracks.addAll(viewModel.getPlaylistTracks())
        bindService(intent,MusicPlayerServiceConnection,Context.BIND_AUTO_CREATE)


        LocalBroadcastManager.getInstance(this).registerReceiver(fragReciever, IntentFilter(FRAG_RECIEVER))
        LocalBroadcastManager.getInstance(this).registerReceiver(trackReciever, IntentFilter("recieveTrack"))
        LocalBroadcastManager.getInstance(this).registerReceiver(playlistReciever, IntentFilter(PlaylistFragment.RECIEVE_PLAYLIST))
        LocalBroadcastManager.getInstance(this).registerReceiver(updateSongDuration, IntentFilter(UPDATE_SONG_POSITION))
        LocalBroadcastManager.getInstance(this).registerReceiver(updateCurrentSongInfo, IntentFilter(SET_CURRENT_SONG_INFORMATION))





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

        if(supportFragmentManager.backStackEntryCount==0) {
            val homeFragment: HomeFragment = HomeFragment()
            supportFragmentManager.beginTransaction().replace(R.id.spotify_frame, homeFragment)
                .addToBackStack(null).commit()
            viewModel.setCurrentFragment(homeFragment)
        }
        playPause.setOnClickListener {
            if(!playPause.isChecked){
                musicServiceConnection.pause()
            }else{
                musicServiceConnection.play()
            }
        }




    }

    override fun onStop() {
        super.onStop()
//        unbindService(MusicPlayerServiceConnection)
    }
    override fun onStart() {
        super.onStart()
        initialise()
        intent=Intent(this,MusicPlayerService::class.java)
        startService(intent)
        bindService(intent,MusicPlayerServiceConnection,Context.BIND_AUTO_CREATE)

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
        fragmentContainer=findViewById(R.id.spotify_frame)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(intent)
    }

    override fun onBackPressed() {

        var frag: Fragment? =supportFragmentManager.findFragmentById(R.id.spotify_frame)
        val frag2=supportFragmentManager.findFragmentById(R.id.home_fragment)

        if(supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount-1)==frag2)
            Log.d(TAG, "onBackPressed: ")
        else if(supportFragmentManager.backStackEntryCount >0) {
            supportFragmentManager.popBackStack()
            viewModel.popFragmentHistoty()
        }
        else
            super.onBackPressed()
    }

}