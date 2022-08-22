package com.example.spotify_clone.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.PlaylistTrack
import com.bumptech.glide.Glide
import com.example.spotify_clone.R

class PlaylistTracksAdapter(val allTracks: ArrayList<PlaylistTrack>) : RecyclerView.Adapter<PlaylistTracksAdapter.TrackHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.track_layout,parent,false)
        return TrackHolder(view)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.songName.text= allTracks.get(position).track?.asTrack?.name
        holder.songArtists.text=allTracks.get(position).track?.asTrack?.artists.toString()
        Log.d("TAG", "onBindViewHolder: tracks"+allTracks.get(position).track?.asTrack?.name)
    }

    override fun getItemCount(): Int {
        return allTracks.size
    }
    inner class TrackHolder(view: View) : RecyclerView.ViewHolder(view){


            var songThumbnail: ImageView=view.findViewById(R.id.song_thumbnail)
            var songName: TextView=view.findViewById(R.id.song_name)
            var songArtists: TextView=view.findViewById(R.id.artists)


    }

}