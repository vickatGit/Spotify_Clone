package com.example.spotify_clone.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.R

class SearchPlaylistAdapter(val searchedResults: ArrayList<Thumbnail>) : RecyclerView.Adapter<SearchPlaylistAdapter.PlaylistHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.search_playlist_thumbnail_layout,parent,false)
        return PlaylistHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        val thumb=searchedResults.get(position)
        Glide.with(holder.playlistCover).load(thumb.imageUrl).into(holder.playlistCover)
        holder.playlistName.text=thumb.name
        sendFragmentRequest(thumb)
        holder.view.setOnClickListener {
            LocalBroadcastManager.getInstance(holder.view.context).sendBroadcast(sendFragmentRequest(thumb))
        }
    }

    private fun sendFragmentRequest(thumb: Thumbnail): Intent {
        var intent= Intent()
        intent.putExtra("id",thumb.next)
        intent.putExtra("type",thumb.type)
        intent.setAction("launchPlaylist")
        return intent
    }
    override fun getItemCount(): Int {
        return searchedResults.size
    }

    inner class PlaylistHolder(view: View) : RecyclerView.ViewHolder(view){
        var playlistCover: ImageView =view.findViewById(R.id.cover_image)
        var playlistName: TextView =view.findViewById(R.id.playlist_name)
        var view:View=view
    }


}