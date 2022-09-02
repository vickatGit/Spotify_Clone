package com.example.spotify_clone.Services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.IntRange
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.spotify_clone.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class SongNotificationManager(
    private val context:Context,
    val sessionToken:MediaSessionCompat.Token,
    notificationListener:PlayerNotificationManager.NotificationListener,
    private val newSongCallback:() ->Unit
) {
//    private val notificationManager:PlayerNotificationManager
//    companion object{
//        val NOTIFICATION_ID:Int=2
//        val NOTIFICATION_CHANNEL_ID:String="music_channel"
//    }
//    init {
//        val mediacontroller=MediaControllerCompat(context,sessionToken)
//
//        notificationManager=PlayerNotificationManager.createWithNotificationChannel(context,
//            NOTIFICATION_CHANNEL_ID ,
//            R.string.channel_name,
//            R.string.notification_description,
//            NOTIFICATION_ID,
//            DescriptionAdapter(mediacontroller),
//            notificationListener
//        ).apply {
//            setSmallIcon(R.drawable.sample_artist)
//            setMediaSessionToken(sessionToken)
//        }
//    }
//
//    fun showNotification(player: Player){
//        notificationManager.setPlayer(player)
//    }
//
//    inner class DescriptionAdapter(private val mediaController:MediaControllerCompat):PlayerNotificationManager.MediaDescriptionAdapter{
//        override fun getCurrentContentTitle(player: Player): CharSequence {
//            return mediaController.metadata.description.title.toString()
//        }
//
//        override fun createCurrentContentIntent(player: Player): PendingIntent? {
//            return mediaController.sessionActivity
//        }
//
//        override fun getCurrentContentText(player: Player): CharSequence? {
//            return mediaController.metadata.description.subtitle.toString()
//        }
//
//        override fun getCurrentLargeIcon(
//            player: Player,
//            callback: PlayerNotificationManager.BitmapCallback
//        ): Bitmap? {
//            Glide.with(context).asBitmap().load(mediaController.metadata.description.iconUri).into( object :
//                CustomTarget<Bitmap>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    callback.onBitmap(resource)
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) = Unit
//
//            })
//            return null
//        }

//    }
}
