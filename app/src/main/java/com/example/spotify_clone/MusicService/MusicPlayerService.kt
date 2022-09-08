package com.example.spotify_clone.MusicService

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.spotify_clone.Fragment.PlaylistFragment
import com.example.spotify_clone.MusicService.NotificationService.NotificationRecieverService
import com.example.spotify_clone.R
import com.example.spotify_clone.SpotifyActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView

class MusicPlayerService : Service() {

    var playerView: PlayerControlView?=null

    val musicBinder=MusicBinder()
    var pos:Int=0
    var image:Bitmap?=null
    lateinit var mediaSession:MediaSessionCompat


    companion object{
        var exo=ExoPlayer.Builder(SpotifyActivity.playerview.context).build()
        val ACTION_PLAY="play_pause_song"
        val ACTION_NEXT="seek_to_next_song"
        val ACTION_PREV="seek_to_previous_song"
    }

    public inner class MusicBinder: Binder(){
        fun getInstanceOfMusicBinder(): MusicPlayerService {
            return this@MusicPlayerService
        }
    }
    val playPauseBroadCastReciever:BroadcastReceiver=object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if(exo.isPlaying)
                pause()
            else
                play()
        }

    }
    val nextSongBroadCastReciever:BroadcastReceiver=object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            exo.seekToNext()
        }

    }
    val prevSongBroadCastReciever:BroadcastReceiver=object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            exo.seekToPrevious()
        }

    }

    override fun onCreate() {
        LocalBroadcastManager.getInstance(this).registerReceiver(playPauseBroadCastReciever, IntentFilter(ACTION_PLAY))
        LocalBroadcastManager.getInstance(this).registerReceiver(nextSongBroadCastReciever, IntentFilter(ACTION_NEXT))
        LocalBroadcastManager.getInstance(this).registerReceiver(prevSongBroadCastReciever, IntentFilter(ACTION_PREV))
        mediaSession=MediaSessionCompat(this,"music_notificataion_player")
        super.onCreate()
        initPlayer()
    }
    override fun onBind(intent: Intent): IBinder {
        return musicBinder

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
    private fun initPlayer(){
        exo.release()
        exo = ExoPlayer.Builder(SpotifyActivity.playerview.context).build()
        SpotifyActivity.exo.addListener(listeners)
        this.playerView?.player= exo
        exo.addListener(listeners)
        if(this.playerView!=null) {
            val totPercent = 29000
            val onpercent = (totPercent / 100).toInt()
            this.playerView!!.setProgressUpdateListener { position, bufferedPosition ->
                Log.d(SpotifyActivity.TAG, "justAddSong: " + totPercent)
                val progress = (position / onpercent!!).toInt()
//            songProgress.setProgress(progress,true)
                val intent = Intent()
                intent.putExtra(SpotifyActivity.CURRENT_SONG_Duration, progress)
                intent.setAction(SpotifyActivity.UPDATE_SONG_POSITION)
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
        }
    }

    fun pause(){
        if(exo.isPlaying) {
            exo.pause()
//            showNotification(R.drawable.pause_icon)
        }
    }
    fun play(){
        if(!exo.isPlaying) {
            exo.play()
//            showNotification(R.drawable.play_icon)
        }
    }
    fun addAllSOngsAndPlay(mediaList: ArrayList<MediaItem>, songPosition: Int, autoPlay: Boolean?) {
        initPlayer()
        exo.setMediaItems(mediaList)
        exo.seekTo(songPosition,0)
        exo.prepare()
        if(autoPlay==true) {
            exo.playWhenReady = true
            exo.play()
        }
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }
    private val listeners = object: Player.Listener{
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            Log.d(SpotifyActivity.TAG, "onMediaItemTransition: ")
            super.onMediaItemTransition(mediaItem, reason)
            pos = exo.currentMediaItemIndex
            SpotifyActivity.currentSong=SpotifyActivity.playlistTracks.get(pos)
            Log.d("TAG", "onMediaItemTransition: the exo${exo.currentMediaItemIndex} and playlis is ${PlaylistFragment.allTracksInfos.size}")
            if(SpotifyActivity.playlistTracks.size>0) {
                image=BitmapFactory.decodeResource(resources,R.drawable.spotify_logo)
                Glide.with(applicationContext).asBitmap().load(SpotifyActivity.playlistTracks.get(pos).image)
                    .into(object :CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            image=resource
                            val intent=Intent()
                            intent.putExtra(SpotifyActivity.CURRENT_SONG_POSITION,pos)
                            intent.setAction(SpotifyActivity.SET_CURRENT_SONG_INFORMATION)

                            LocalBroadcastManager.getInstance(this@MusicPlayerService).sendBroadcast(intent)
                            showNotification(R.drawable.play_icon)

                        }
                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })

            }
        }



        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            val intent=Intent()
            intent.putExtra("isPlaying",isPlaying)
            intent.setAction("updatePlayPauseButton")
            LocalBroadcastManager.getInstance(playerView!!.context).sendBroadcast(intent)
            if(isPlaying)
                showNotification(R.drawable.play_icon)
            else
                showNotification(R.drawable.pause_icon)
        }

    }
    fun setPlayerview(playerview: PlayerControlView) {

        this.playerView=playerview
        this.playerView!!.player=exo
        Log.d("TAG", "setPlayerview: "+this.playerView)
        val totPercent=29000
        val onpercent=(totPercent/100).toInt()
        this.playerView!!.setProgressUpdateListener { position, bufferedPosition ->
            val progress=(position/ onpercent!!).toInt()
//            songProgress.setProgress(progress,true)
            Log.d(SpotifyActivity.TAG, "justAddSong: "+progress)
            val intent = Intent()
            intent.putExtra(SpotifyActivity.CURRENT_SONG_Duration,progress)
            intent.setAction(SpotifyActivity.UPDATE_SONG_POSITION)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }

    }
    fun showNotification(playPauseIcon: Int) {
        val track=SpotifyActivity.playlistTracks.get(pos)



        val intent:Intent=Intent(this,SpotifyActivity::class.java)
        val pendingIntent:PendingIntent= PendingIntent.getActivity(this,0,intent,0)

        val playIntent:Intent= Intent(this,NotificationRecieverService::class.java).setAction(ACTION_PLAY)
        val playPendingIntent:PendingIntent=PendingIntent.getBroadcast(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent:Intent= Intent(this,NotificationRecieverService::class.java).setAction(ACTION_NEXT)
        val nextPendingIntent:PendingIntent=PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val prevIntent:Intent= Intent(this,NotificationRecieverService::class.java).setAction(ACTION_PREV)
        val prevPendingIntent:PendingIntent=PendingIntent.getBroadcast(this,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT)


        val songTitle:Spannable=SpannableString(track.name)
        songTitle.setSpan(StyleSpan(Typeface.BOLD), 0, songTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val musicNotification:Notification=androidx.core.app.NotificationCompat.Builder(this,"music_channel_id")
            .setSmallIcon(R.drawable.spotify_logo)
            .setContentTitle(songTitle)
            .setLargeIcon(image)
            .setColorized(true)
            .addAction(R.drawable.song_prev_icon,"previous",prevPendingIntent)
            .addAction(playPauseIcon,"play",playPendingIntent)
            .addAction(R.drawable.next_song_icon,"next",nextPendingIntent)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setContentText(track.artist)
            .setPriority(Notification.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
        val notificationManager:NotificationManager= getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0,musicNotification)



    }

    fun seekToThisPosition(songPosition: Int) {
        exo.seekTo(songPosition,0)
        play()
    }
}