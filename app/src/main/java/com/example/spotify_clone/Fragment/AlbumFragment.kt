package com.example.spotify_clone.Fragment

import android.content.Intent
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
import android.widget.ToggleButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.SimpleTrack
import com.bumptech.glide.Glide
import com.example.spotify_clone.Adapters.AlbumAdapter
import com.example.spotify_clone.Models.ApiRelatedModels.TrackModel
import com.example.spotify_clone.R
import com.example.spotify_clone.ViewModels.AlbumViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout


class AlbumFragment : Fragment() {
    private lateinit var albumId:String
    private lateinit var viewModel:AlbumViewModel


    private lateinit var albumDescription: TextView
    private lateinit var albumThumbnail: ImageView
    private lateinit var toolbar: CollapsingToolbarLayout
    private lateinit var allTrackesRecycler: RecyclerView
    private lateinit var playPauseAlbum: ToggleButton
    private lateinit var tracksAdapter: AlbumAdapter
    private var allTracks= ArrayList<SimpleTrack>()
    private var allTracksInfos= ArrayList<TrackModel>()
    private lateinit var progress: ProgressBar
    private lateinit var albumView: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance=true
        albumId= arguments?.getString("id").toString()
        viewModel=ViewModelProvider(this).get(AlbumViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_album, container, false)
        initialiseViews(view)
        playPauseAlbum.setOnClickListener {
            val intent= Intent()
            intent.setAction(PlaylistFragment.RECIEVE_PLAYLIST)
            if(!playPauseAlbum.isChecked){
                intent.putExtra("action","pause")
            }
            else{
                intent.putExtra("playlist",allTracksInfos)
            }
            LocalBroadcastManager.getInstance(this.requireContext()).sendBroadcast(intent)
        }

        tracksAdapter= AlbumAdapter(allTracks)
        allTrackesRecycler.layoutManager=LinearLayoutManager(this.requireContext())
        allTrackesRecycler.adapter=tracksAdapter
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            progress.visibility= View.GONE
            albumView.visibility=View.VISIBLE
        },2000)
        viewModel.fetchAlbumInfo(albumId).observe(this.viewLifecycleOwner, Observer {
            albumDescription.text=it?.name
            Glide.with(albumThumbnail).load(it?.images?.get(0)?.url).into(albumThumbnail)
            toolbar.title=it?.name
        })
        viewModel.fetchAlbumTracks(albumId).observe(this.viewLifecycleOwner, Observer {
            allTracks.clear()
            allTracks.addAll(it!!)
            var trackModel:TrackModel
            it.forEach {
                val track =it
                trackModel = TrackModel(
                    track?.id,
                    null,
                    it?.name,
                    it?.artists?.get(0)?.name,
                    it?.durationMs,
                    it?.linkedTrack?.id,
                    it?.previewUrl,
                    false
                )
                allTracksInfos.add(trackModel)
            }
            tracksAdapter.notifyDataSetChanged()
        })
        return view
    }


    private fun initialiseViews(view: View?) {
        albumDescription=view?.findViewById(R.id.album_description)!!
        albumThumbnail=view?.findViewById(R.id.album_thumbnail)!!
        toolbar=view?.findViewById(R.id.collapsing_toolbar)!!
        progress=view?.findViewById(R.id.progress)!!
        albumView=view?.findViewById(R.id.album_activity)!!
        playPauseAlbum=view?.findViewById(R.id.album_pause_icon)!!
        allTrackesRecycler=view?.findViewById(R.id.album_all_tracks)!!
    }
}