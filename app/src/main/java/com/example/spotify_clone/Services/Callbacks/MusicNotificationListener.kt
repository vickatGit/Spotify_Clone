package com.example.spotify_clone.Services.Callbacks

import android.app.Notification
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.spotify_clone.Services.MusicNotificationService
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class MusicNotificationListener (
    private val musicService:MusicNotificationService
    ):PlayerNotificationManager.NotificationListener{
//    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
//        super.onNotificationCancelled(notificationId, dismissedByUser)
//        musicService.apply {
//            stopForeground(true)
//            isForeground=false
//            stopSelf()
//        }
//    }
//
//    override fun onNotificationPosted(
//        notificationId: Int,
//        notification: Notification,
//        ongoing: Boolean
//    ) {
//        super.onNotificationPosted(notificationId, notification, ongoing)
//        musicService.apply {
//            if(ongoing && !isForeground){
//                ContextCompat.startForegroundService(this, Intent(applicationContext, this::class.java))
//                startForeground(notificationId,notification)
//                isForeground=true
//            }
//        }
//    }

}