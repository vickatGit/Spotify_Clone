package com.example.spotify_clone.Adapters

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.PlaylistTrack
import com.bumptech.glide.Glide
import com.example.spotify_clone.Models.ApiRelatedModels.TrackModel
import com.example.spotify_clone.R
import com.example.spotify_clone.SpotifyActivity

class PlaylistTracksAdapter(val allTracks: ArrayList<PlaylistTrack>) : RecyclerView.Adapter<PlaylistTracksAdapter.TrackHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.track_layout,parent,false)
        return TrackHolder(view)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        val track=allTracks.get(position).track?.asTrack
        holder.songName.text= track?.asTrack?.name
        holder.songArtists.text=track?.asTrack?.artists?.get(0)?.name
        Glide.with(holder.songThumbnail.context).load(track?.album?.images?.get(track?.album.images.size-1)?.url).into(holder.songThumbnail)
        holder.song.setOnClickListener {
            val trackInfo = TrackModel(
                track?.id,
                track?.album?.images?.get(track?.album?.images?.size - 1)?.url,
                track?.name,
                track?.artists?.get(0)?.name,
                track?.durationMs,
                track?.linkedTrack?.id,
                track?.previewUrl
            )
            Log.d("TAG", "track name: " + track?.name + "and album name is" + track?.album?.name)
            val intent = Intent()
            val bundle = Bundle()
            bundle.putParcelable("track", trackInfo)
            intent.putExtra("track", bundle)
            intent.setAction("recieveTrack")
            LocalBroadcastManager.getInstance(holder.song.context).sendBroadcast(intent)
        }
    }

    override fun getItemCount(): Int {
        return allTracks.size
    }
    inner class TrackHolder(view: View) : RecyclerView.ViewHolder(view){


        var songThumbnail: ImageView=view.findViewById(R.id.song_thumbnail)
        var songName: TextView=view.findViewById(R.id.song_name)
        var songArtists: TextView=view.findViewById(R.id.artists)
        var song:View=view


    }

}

