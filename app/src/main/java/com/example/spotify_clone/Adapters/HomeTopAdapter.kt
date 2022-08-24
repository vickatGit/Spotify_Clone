package com.example.spotify_clone.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.SimplePlaylist
import com.bumptech.glide.Glide
import com.example.spotify_clone.R

class HomeTopAdapter(val allCategories: ArrayList<SimplePlaylist>) : RecyclerView.Adapter<HomeTopAdapter.GridAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridAdapter {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.home_top_layout,parent,false)
        return GridAdapter(view)
    }

    override fun onBindViewHolder(holder: GridAdapter, position: Int) {
        val category=allCategories.get(position)
        Glide.with(holder.userProfile.context).load(category.images.get(category.images.size-1).url).into(holder.userProfile)

        holder.header.text=category.name
        holder.topCover.setOnClickListener {
            if(category.type.equals("playlist",true)){
                val intent= Intent()
                intent.putExtra("id",category.id)
                intent.putExtra("type","playlist")
                intent.setAction("launchPlaylist")
                LocalBroadcastManager.getInstance(holder.topCover.context).sendBroadcast(intent)

            }
        }
    }

    override fun getItemCount(): Int {
        return  allCategories.size
    }
    inner class GridAdapter(view: View) : RecyclerView.ViewHolder(view){
        val userProfile: ImageView =view.findViewById(R.id.user_profile)
        val header: TextView =view.findViewById(R.id.top_header)
        val topCover:View=view
    }
}