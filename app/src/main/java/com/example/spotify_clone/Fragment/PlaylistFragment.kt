package com.example.spotify_clone.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.PlaylistTrack
import com.bumptech.glide.Glide
import com.example.spotify_clone.Adapters.PlaylistTracksAdapter
import com.example.spotify_clone.R
import com.example.spotify_clone.ViewModels.PlaylistActivityViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout


class PlaylistFragment : Fragment() {

    private lateinit var id:String
    private lateinit var type:String
    private lateinit var viewmodel: PlaylistActivityViewModel

    private lateinit var playlistDescription: TextView
    private lateinit var playlistThumbnail: ImageView
    private lateinit var toolbar: CollapsingToolbarLayout
    private lateinit var allTrackesRecycler: RecyclerView
    private lateinit var tracksAdapter: PlaylistTracksAdapter
    private var allTracks= ArrayList<PlaylistTrack>()
    private lateinit var progress: ProgressBar
    private lateinit var playlistView: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.id= arguments?.getString("id").toString()
        this.type= arguments?.getString("type").toString()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_playlist, container, false)
        initialiseViews(view)
        ViewCompat.setNestedScrollingEnabled(allTrackesRecycler,false)
        tracksAdapter= PlaylistTracksAdapter(allTracks)
        allTrackesRecycler.layoutManager= LinearLayoutManager(this.requireContext())
        allTrackesRecycler.adapter=tracksAdapter
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            progress.visibility= View.GONE
            playlistView.visibility=View.VISIBLE
        },2000)
        viewmodel= ViewModelProvider(this).get(PlaylistActivityViewModel::class.java)
        viewmodel.setId(id)
        viewmodel.getPlaylistInfo().observe(this.viewLifecycleOwner, Observer {
            Glide.with(this).load(it.images.get(0).url).into(playlistThumbnail)
            playlistDescription.text=it.description
            toolbar.title=it.name
        })
        viewmodel.fetchTracks()
        viewmodel.getFetchedTracks().observe(this.viewLifecycleOwner, Observer {
            allTracks.clear()
            Log.d("TAG", "onCreate: fetchedtracks $it")
            allTracks.addAll(it)
            tracksAdapter.notifyDataSetChanged()

        })
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    private fun initialiseViews(view: View) {
        playlistDescription=view.findViewById(R.id.playlist_description)
        playlistThumbnail=view.findViewById(R.id.playlist_thumbnail)
        allTrackesRecycler=view.findViewById(R.id.all_tracks)
        toolbar=view.findViewById(R.id.collapsing_toolbar)
        progress=view.findViewById(R.id.progress)
        playlistView=view.findViewById(R.id.playlist_activity)
    }


}