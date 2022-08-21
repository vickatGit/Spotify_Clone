package com.example.spotify_clone.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.R

class ChildListAdapter(val allThumbs: List<Thumbnail>, val requireActivity: FragmentActivity) : RecyclerView.Adapter<ChildListAdapter.ArtistHolder>() {
    var thumbs=ArrayList<Thumbnail>(1)
    private lateinit var adapter: ChildListAdapter
    lateinit var recycler:RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistHolder {
        thumbs.addAll(allThumbs)
        val view=LayoutInflater.from(parent.context).inflate(R.layout.artist_layout,parent,false)
        return ArtistHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recycler=recyclerView
    }

    override fun onBindViewHolder(holder: ArtistHolder, position: Int) {
        val thumb=allThumbs.get(position)
        Log.d("TAG", "onBindViewHolder: allThumbs $thumb")

        Glide.with(holder.artistProfile.context).load(thumb.imageUrl).into(holder.artistProfile)
        Log.d("TAG", "onBindViewHolder: "+thumb.name)
        holder.artistName.text=thumb.name
//        thumb.observer.observe(requireActivity, Observer {
//            allThumbs.clear()
//            allThumbs.addAll(it!!)
//            Log.d("TAG", "onBindViewHolder: $it")
//
//            recycler.post {
//                recycler.stopScroll()
//                adapter.notifyDataSetChanged()
//            }
//        })
    }


    override fun getItemCount(): Int {
        return allThumbs.size
    }

    fun setAdapter(childAdapter: ChildListAdapter) {
        this.adapter=childAdapter

    }

    fun updateAdapter(it: List<Thumbnail>, childAdapter: ChildListAdapter) {
        Log.d("TAG", "updateAdapter: $it")
        thumbs.clear()
        thumbs.addAll(it)
        notifyDataSetChanged()
    }


    inner class ArtistHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var artistProfile:ImageView=itemView.findViewById(R.id.artist_profile)
        var artistName:TextView=itemView.findViewById(R.id.artist_name)


    }


}