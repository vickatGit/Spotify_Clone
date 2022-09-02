package com.example.spotify_clone.Services

import android.app.PendingIntent
import android.media.MediaSession2Service
import android.media.session.MediaSession
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.example.spotify_clone.Services.Callbacks.MusicNotificationListener
import com.google.android.exoplayer2.ExoPlayer

import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector

import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class MusicNotificationService {

//    lateinit var dataSourceFactory: DefaultDataSourceFactory
//    lateinit var exoPlayer: ExoPlayer
//    private  var job= Job()
//    private val scope= CoroutineScope(Dispatchers.Main+job)
//    private lateinit var mediaSession:MediaSessionCompat
//    private lateinit var mediaSessionConnector:MediaSessionConnector
//    private lateinit var musicNotificationManager:SongNotificationManager
//    var isForeground=false
//
//
//
//    override fun onCreate() {
//        super.onCreate()
//        exoPlayer=ExoPlayer.Builder(this)
//        val actitvityIntent=packageManager.getLaunchIntentForPackage(packageName)?.let {
//            PendingIntent.getActivity(this,0,it,0  )
//        }
//        mediaSession= MediaSessionCompat(this,"music service").apply {
//            setSessionActivity(actitvityIntent)
//            isActive=true
//        }
//        sessionToken=mediaSession.sessionToken
//        mediaSessionConnector= MediaSessionConnector(mediaSession)
//        mediaSessionConnector.setPlayer(exoPlayer,null,null,null)
//        musicNotificationManager=
//            SongNotificationManager(this,mediaSession.sessionToken,MusicNotificationListener(this)){
//
//            }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        scope.cancel()
//    }
//    override fun onGetRoot(
//        clientPackageName: String,
//        clientUid: Int,
//        rootHints: Bundle?
//    ): BrowserRoot? {
//        return null
//    }
//
//    override fun onLoadChildren(
//        parentId: String,
//        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
//    ) {
//    }
}