package com.example.spotify_clone.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.SpotifyImage
import com.bumptech.glide.Glide
import com.example.spotify_clone.R

class PopularArtistAdapter(val allArtists: ArrayList<Artist>) : RecyclerView.Adapter<PopularArtistAdapter.ArtistHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.artist_layout,parent,false)
        return ArtistHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistHolder, position: Int) {
        val artist=allArtists.get(position)
        val image:SpotifyImage=artist.images.get(0)
        Glide.with(holder.artistProfile.context).load(image.url).into(holder.artistProfile)
        Log.d("TAG", "onBindViewHolder: "+artist.name)
        holder.artistName.text=artist.name
    }

    override fun getItemCount(): Int {
        return allArtists.size
    }

    inner class ArtistHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var artistProfile:ImageView=itemView.findViewById(R.id.artist_profile)
        var artistName:TextView=itemView.findViewById(R.id.artist_name)
    }


}