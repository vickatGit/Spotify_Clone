package com.example.spotify_clone.Adapters

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify_clone.Fragment.SearchFragment
import com.example.spotify_clone.Models.ApiRelatedModels.GenresModel
import com.example.spotify_clone.R
import com.example.spotify_clone.ViewModels.SearchGenresViewModel
import kotlin.random.Random

class SearchGenresAdapter(
    val allGenres: ArrayList<GenresModel>,
    val viewmodel: SearchGenresViewModel,
    val requireContext: SearchFragment,
) : RecyclerView.Adapter<SearchGenresAdapter.GenresViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.search_genres_layout,parent,false)
        return GenresViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {

        val color=Color.argb(255, Random.nextInt(120,255),Random.nextInt(100,255),Random.nextInt(150,255))
        holder.genresCard.setCardBackgroundColor(color)
        holder.genresTitle.text=allGenres.get(position).name
        holder.genres.setOnClickListener {
            val intent= Intent()
            intent.putExtra("id",allGenres.get(position).id)
            intent.putExtra("type","genres")
            intent.setAction("launchPlaylist")
            Log.d("genres", "onBindViewHolder: genres is clicked")
            LocalBroadcastManager.getInstance(requireContext.requireContext()).sendBroadcast(intent)
        }
    }

    override fun getItemCount(): Int {
        return allGenres.size
    }
    inner class GenresViewHolder(view: View) :RecyclerView.ViewHolder(view){
        var genresCard:CardView=view.findViewById(R.id.genres_card)
        var genresTitle: TextView =view.findViewById(R.id.genres)
        var genres=view
    }
}