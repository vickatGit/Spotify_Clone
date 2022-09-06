package com.example.spotify_clone.Adapters

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Track
import com.bumptech.glide.Glide
import com.example.spotify_clone.Fragment.PlaylistFragment
import com.example.spotify_clone.Fragment.SearchSongResultsFragment
import com.example.spotify_clone.R
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.Models.ApiRelatedModels.TrackModel
import com.example.spotify_clone.SpotifyActivity
import com.example.spotify_clone.ViewModels.SearchSongFragmentViewModel


class SearchSongAdapter(
    val searchedResults: ArrayList<Track>,
    val viewModel: SearchSongFragmentViewModel,
    val searchSongFragment: SearchSongResultsFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class layout{
        playlist,album,track,artist
    }
    var layoutType=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("TAG", "onCreateViewHolder: searchSongViewHolder")
        layoutType=viewType
        Log.d("TAG", "onCreateViewHolder: viewType $viewType")
        when(viewType){

//            layout.playlist.ordinal -> {
//                val view=LayoutInflater.from(parent.context).inflate(R.layout.search_playlist_thumbnail_layout,parent,false)
//                return playlistHolder(view)
//            }
//            layout.album.ordinal -> {
//                val view=LayoutInflater.from(parent.context).inflate(R.layout.search_album_thumbnail_layout,parent,false)
//                return albumHolder(view)
//            }
//            layout.artist.ordinal -> {
//                val view=LayoutInflater.from(parent.context).inflate(R.layout.search_artist_thumbnail_layout,parent,false)
//                return artistHolder(view)
//            }
            layout.track.ordinal -> {
                val view=LayoutInflater.from(parent.context).inflate(R.layout.track_layout,parent,false)
                return trackHolder(view)
            }
            else -> {
                val view=LayoutInflater.from(parent.context).inflate(R.layout.track_layout,parent,false)
                return trackHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val thumb=searchedResults?.get(position)
//            if(thumb.type=="track") {

                Log.d("TAG", "onBindViewHolder: title is"+thumb.name)
                val holder = holder as trackHolder
                var track:Track?=null
                var isFavourite: Boolean?=false
                var trackInfo:TrackModel?=null

                isFavourite= SpotifyActivity.userFavouriteSongs.contains(track?.id)
                track=thumb
                trackInfo = TrackModel(
                    track?.id,
                    track?.album?.images?.get(track?.album?.images?.size!! - 1)?.url,
                    track?.name,
                    track?.artists?.get(0)?.name,
                    track?.durationMs,
                    track?.linkedTrack?.id,
                    track?.previewUrl,isFavourite
                )
                if(isFavourite==true)
                    holder.isUsersFavourite.visibility=View.VISIBLE
                else
                    holder.isUsersFavourite.visibility=View.INVISIBLE
                holder.view.setOnClickListener {
                        Log.d("TAG", "track name: " + track?.name + "and album name is" + track?.album?.name)

                        val intent = Intent()
                        val bundle = Bundle()
                        intent.putExtra("song_position",0)
                        intent.putExtra("song_id",trackInfo?.id)
                        intent.putExtra("track", arrayListOf(trackInfo))
                        intent.setAction(PlaylistFragment.ADD_SONG)
                        LocalBroadcastManager.getInstance(searchSongFragment.requireContext()).sendBroadcast(intent)
                }
                    Glide.with(holder.songCover).load(trackInfo.image).override(50,50).placeholder(R.drawable.sample_artist).into(holder.songCover)
                    holder.songName.text = thumb.name
                    holder.songArtist.text = trackInfo.artist


//            }
//            else if(thumb.type=="artist") {
//                val holder = holder as artistHolder
//                Glide.with(holder.artistCover).load(thumb.imageUrl).into(holder.artistCover)
//                holder.artistName.text=thumb.name
//                holder.view.setOnClickListener {
//                    LocalBroadcastManager.getInstance(holder.view.context).sendBroadcast(sendFragmentRequest(thumb))
//                }
//            }
//            else if(thumb.type=="album") {
//                val holder = holder as albumHolder
//                Glide.with(holder.albumCover).load(thumb.imageUrl).into(holder.albumCover)
//                holder.albumName.text=thumb.name
//                holder.albumArtists.text=thumb.title
//                holder.view.setOnClickListener {
//                    LocalBroadcastManager.getInstance(holder.view.context).sendBroadcast(sendFragmentRequest(thumb))
//                }
//            }
//            else if(thumb.type=="playlist") {
//                val holder = holder as playlistHolder
//                Glide.with(holder.playlistCover).load(thumb.imageUrl).into(holder.playlistCover)
//                holder.playlistName.text=thumb.name
//                sendFragmentRequest(thumb)
//                holder.view.setOnClickListener {
//                    LocalBroadcastManager.getInstance(holder.view.context).sendBroadcast(sendFragmentRequest(thumb))
//                }
//            }




    }
    private fun sendFragmentRequest(thumb: Thumbnail): Intent {
        var intent= Intent()
        intent.putExtra("id",thumb.next)
        intent.putExtra("type",thumb.type)
        intent.setAction("launchPlaylist")
        return intent


    }

    override fun getItemViewType(position: Int): Int {

        when(searchedResults.get(position).type){
            "track" -> return layout.track.ordinal
            "playlist" -> return layout.playlist.ordinal
            "album" -> return layout.album.ordinal
            "artist" -> return layout.artist.ordinal
            else -> return -1
        }
    }

    override fun getItemCount(): Int {
        return searchedResults.size
    }
    inner class trackHolder(view: View) : RecyclerView.ViewHolder(view){
        var songCover: ImageView=view.findViewById(R.id.song_thumbnail)
        var songName:TextView=view.findViewById(R.id.song_name)
        var songArtist:TextView=view.findViewById(R.id.artists)
        var isUsersFavourite:ToggleButton=view.findViewById(R.id.is_favourite)
        var view:View=view
    }
    inner class albumHolder(view: View) : RecyclerView.ViewHolder(view){
        var albumCover:ImageView=view.findViewById(R.id.cover_image)
        var albumName:TextView=view.findViewById(R.id.album_name)
        var albumArtists:TextView=view.findViewById(R.id.album_artist_name)
        var view:View=view
    }
    inner class artistHolder(view: View) : RecyclerView.ViewHolder(view){
        var artistCover:ImageView=view.findViewById(R.id.cover_image)
        var artistName:TextView=view.findViewById(R.id.artist_name)
        var view:View=view
    }
    inner class playlistHolder(view: View) : RecyclerView.ViewHolder(view){
        var playlistCover:ImageView=view.findViewById(R.id.cover_image)
        var playlistName:TextView=view.findViewById(R.id.playlist_name)
        var view:View=view
    }

}