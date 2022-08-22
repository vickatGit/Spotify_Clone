package com.example.spotify_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.PlaylistTrack
import com.example.spotify_clone.Adapters.PlaylistTracksAdapter
import com.example.spotify_clone.ViewModels.PlaylistActivityViewModel

class PlaylistActivity : AppCompatActivity() {

    private lateinit var viewmodel:PlaylistActivityViewModel

    private lateinit var playlistDescription: TextView
    private lateinit var playlistThumbnail: ImageView
    private lateinit var allTrackesRecycler:RecyclerView
    private lateinit var tracksAdapter: PlaylistTracksAdapter
    private var allTracks= ArrayList<PlaylistTrack>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        val id=intent.getStringExtra("id")
        initialiseViews()
        tracksAdapter= PlaylistTracksAdapter(allTracks)
        viewmodel=ViewModelProvider(this).get(PlaylistActivityViewModel::class.java)
        viewmodel.setId(id)
        viewmodel.getPlaylistInfo().observe(this, Observer {

        })
        viewmodel.fetchTracks()
        viewmodel.getFetchedTracks().observe(this, Observer {
            allTracks.clear()
            allTracks.addAll(it)
            tracksAdapter.notifyDataSetChanged()

        })

    }

    private fun initialiseViews() {
        playlistDescription=findViewById(R.id.playlist_description)
        playlistThumbnail=findViewById(R.id.playlist_thumbnail)
        allTrackesRecycler=findViewById(R.id.all_tracks)
    }


}