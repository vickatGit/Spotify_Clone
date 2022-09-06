package com.example.spotify_clone.MusicService.NotificationService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.spotify_clone.MusicService.MusicPlayerService

class NotificationRecieverService: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent!!)
        }
    }
}