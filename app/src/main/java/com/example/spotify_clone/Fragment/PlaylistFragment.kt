package com.example.spotify_clone.Fragment

import android.content.Context
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
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.PlaylistTrack
import com.bumptech.glide.Glide
import com.example.spotify_clone.Adapters.PlaylistTracksAdapter
import com.example.spotify_clone.Models.ApiRelatedModels.TrackModel
import com.example.spotify_clone.R
import com.example.spotify_clone.SpotifyActivity
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
    private lateinit var playPausePlaylist:ToggleButton
    private lateinit var tracksAdapter: PlaylistTracksAdapter
    private var allTracks= ArrayList<PlaylistTrack>()

    private lateinit var progress: ProgressBar
    private lateinit var playlistView: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance=true
        Log.d(TAG, "onCreate: ")
        this.id= arguments?.getString("id").toString()
        this.type= arguments?.getString("type").toString()
        Log.d("TAG", "onCreate: search $id and ")

    }
    companion object{
        var allTracksInfos= ArrayList<TrackModel>()
        val RECIEVE_PLAYLIST: String="recieve_playlist"
        val TAG="playlist_fragment"
        val ADD_SONG="add_song"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ")

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
            var trackModel:TrackModel
            allTracksInfos.clear()
            it.forEach {
                val track=it?.track
                trackModel=TrackModel(track?.id,track?.asTrack?.album?.images?.get(track?.asTrack?.album?.images?.size!!-1)?.url,track?.asTrack?.name,
                track?.asTrack?.artists?.get(0)?.name,track?.asTrack?.durationMs,track?.asTrack?.linkedTrack?.id,track?.asTrack?.previewUrl)
                allTracksInfos.add(trackModel)
            }
            tracksAdapter.notifyDataSetChanged()

        })
        playPausePlaylist.setOnClickListener {
            val intent=Intent()
            intent.setAction(RECIEVE_PLAYLIST)
            if(!playPausePlaylist.isChecked){
                intent.putExtra("action","pause")
            }
            else{
                Log.d("TAG", "onCreateView: in else"+playPausePlaylist.isChecked)
                intent.putExtra("playlist",allTracksInfos)
            }
                LocalBroadcastManager.getInstance(this.requireContext()).sendBroadcast(intent)
        }
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: ")
        super.onViewCreated(view, savedInstanceState)

    }
    private fun initialiseViews(view: View) {
        playlistDescription=view.findViewById(R.id.playlist_description)
        playlistThumbnail=view.findViewById(R.id.playlist_thumbnail)
        allTrackesRecycler=view.findViewById(R.id.all_tracks)
        toolbar=view.findViewById(R.id.collapsing_toolbar)
        progress=view.findViewById(R.id.progress)
        playlistView=view.findViewById(R.id.playlist_activity)
        playPausePlaylist=view.findViewById(R.id.playlist_pause_icon)
    }

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach: ")
        super.onAttach(context)
    }

    override fun onStart() {
        Log.d(TAG, "onStart: ")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause: ")
        super.onPause()

    }

    override fun onStop() {
        Log.d(TAG, "onStop: ")
        super.onStop()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewStateRestored: ")
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach: ")
        super.onDetach()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: ")
        super.onDestroyView()
    }
}