package com.example.spotify_clone.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Track
import com.bumptech.glide.Glide
import com.example.spotify_clone.R


class SearchSongAdapter(val searchedTracks: ArrayList<Track>) : RecyclerView.Adapter<SearchSongAdapter.SearchResultViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.track_layout,parent,false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val track=searchedTracks.get(position)
        Glide.with(holder.songCover).load(track?.album.images.get(track?.album?.images.size-1).url).into(holder.songCover)
        holder.songName.text=track.name
//        holder.songArtist.text=track.artists.get(0).name
    }

    override fun getItemCount(): Int {
        return searchedTracks.size
    }
    inner class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val songCover: ImageView=view.findViewById(R.id.song_thumbnail)
        val songName:TextView=view.findViewById(R.id.song_name)
//        val songArtist:TextView=view.findViewById(R.id.song_artist)
    }

}