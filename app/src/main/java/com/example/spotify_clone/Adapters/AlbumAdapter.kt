package com.example.spotify_clone.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.SimpleTrack
import com.bumptech.glide.Glide
import com.example.spotify_clone.R

class AlbumAdapter(val allTracks: ArrayList<SimpleTrack>) : RecyclerView.Adapter<AlbumAdapter.AlbumTrackHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumTrackHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.album_track_layout,parent,false)
        return AlbumTrackHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumTrackHolder, position: Int) {
        val track=allTracks.get(position)
        holder.artist.text=track.artists.get(0).name
        holder.songName.text=track.name
    }

    override fun getItemCount(): Int {
        return allTracks.size
    }

    inner class AlbumTrackHolder(view: View) : RecyclerView.ViewHolder(view){
        val songName:TextView=view.findViewById(R.id.song_name)
        val artist:TextView=view.findViewById(R.id.artist)
//        val menu:ImageView=view.findViewById(R.id.menu)
    }

}