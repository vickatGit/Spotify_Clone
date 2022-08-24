package com.example.spotify_clone.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify_clone.R
import kotlin.random.Random

class SearchGenresAdapter(val allGenres: ArrayList<String>) : RecyclerView.Adapter<SearchGenresAdapter.GenresViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.search_genres_layout,parent,false)
        return GenresViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {

        val color=Color.argb(255, Random.nextInt(120,255),Random.nextInt(100,255),Random.nextInt(150,255))
        holder.genresCard.setCardBackgroundColor(color)
        holder.genresTitle.text=allGenres.get(position)
    }

    override fun getItemCount(): Int {
        return allGenres.size
    }
    inner class GenresViewHolder(view: View) :RecyclerView.ViewHolder(view){
        var genresCard:CardView=view.findViewById(R.id.genres_card)
        var genresTitle: TextView =view.findViewById(R.id.genres)
    }
}