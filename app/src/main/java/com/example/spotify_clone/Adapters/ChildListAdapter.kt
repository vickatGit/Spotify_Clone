package com.example.spotify_clone.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.R

class ChildListAdapter(val allThumbs: List<Thumbnail>, val requireActivity: FragmentActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var thumbs=ArrayList<Thumbnail>(1)
    private lateinit var adapter: ChildListAdapter
    lateinit var recycler:RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        thumbs.addAll(allThumbs)
        val view:View
        if(viewType==0) {

            view= LayoutInflater.from(parent.context).inflate(R.layout.generic_thumb_layout, parent, false)
            return GenricThumbHolder(view)

        }else{
            view= LayoutInflater.from(parent.context).inflate(R.layout.artist_layout, parent, false)
            return ArtistHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(allThumbs.get(position).type.equals("artist"))
            return 1
        else
            return 0
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recycler=recyclerView
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val thumb=allThumbs.get(position)
        if(holder.itemViewType==1) {
            var holder=holder as ArtistHolder
            Log.d("TAG", "onBindViewHolder: allThumbs $thumb")

            Glide.with(holder.artistProfile.context).load(thumb.imageUrl).into(holder.artistProfile)
            Log.d("TAG", "onBindViewHolder: " + thumb.name)
            holder.artistName.text = thumb.name
        }else{
            val CLIENT_ID="a7ade92373684af7b78d8382b1031827"
            val CLIENT_SECRET="4b8c41c29cfd497298b910335d9599a1"

            var holder=holder as GenricThumbHolder
            Glide.with(holder.coverImage.context).load(thumb.imageUrl).into(holder.coverImage)
            Log.d("TAG", "onBindViewHolder: " + thumb.name)
            holder.artistsName.text = thumb.name
            holder.cover.setOnClickListener {
                if(thumb.type.equals("playlist")){
                    val intent= Intent()
                    intent.putExtra("id",thumb.next)
                    intent.putExtra("type","playlist")
                    intent.setAction("launchPlaylist")
                    LocalBroadcastManager.getInstance(requireActivity).sendBroadcast(intent)
                }
                else if(thumb.type.equals("album")){
                    val intent= Intent()
                    intent.putExtra("id",thumb.next)
                    intent.putExtra("type","album")
                    intent.setAction("launchPlaylist")
                    LocalBroadcastManager.getInstance(requireActivity).sendBroadcast(intent)
                }
            }
        }
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
        var cover:View=itemView

    }
    inner class GenricThumbHolder(view: View) : RecyclerView.ViewHolder(view){
        var coverImage:ImageView=view.findViewById(R.id.cover_image)
        var artistsName:TextView=view.findViewById(R.id.artist_names)
        var cover:View=view

    }


}