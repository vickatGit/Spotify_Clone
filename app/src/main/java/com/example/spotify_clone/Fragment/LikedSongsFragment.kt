package com.example.spotify_clone.Fragment

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Track
import com.example.spotify_clone.Adapters.SearchSongAdapter
import com.example.spotify_clone.R
import com.example.spotify_clone.SpotifyActivity
import com.example.spotify_clone.ViewModels.LikedSongsViewModel


class LikedSongsFragment : Fragment() {

    private lateinit var likedSongsRecycler:RecyclerView
    private lateinit var likedSongsAdapter:SearchSongAdapter
    private lateinit var viewModel:LikedSongsViewModel
    private lateinit var progress:ProgressBar
    var likedSongs=ArrayList<Track>(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= ViewModelProvider(this).get(LikedSongsViewModel::class.java)
        val userId=arguments?.getString("userId").toString()
        viewModel.setUserId(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(this.resources.configuration.orientation== Configuration.ORIENTATION_LANDSCAPE){
            SpotifyActivity.landHomeFragment.setBackgroundColor(resources.getColor(R.color.spotify_background))
            SpotifyActivity.landSearchFragment.setBackgroundColor(resources.getColor(R.color.spotify_background))
            SpotifyActivity.landLibraryFragment.setBackgroundColor(resources.getColor(R.color.secondaryLightColor))
        }else {
            SpotifyActivity.bottomNavigationView.menu.findItem(R.id.library).setChecked(true)
        }
        val view=inflater.inflate(R.layout.fragment_liked_songs, container, false)
        progress=view.findViewById(R.id.likes_songs_progress)
        likedSongsRecycler=view.findViewById(R.id.liked_songs)
        likedSongsRecycler.layoutManager=LinearLayoutManager(this.requireContext())
        likedSongsAdapter=SearchSongAdapter(likedSongs,null,this)
        likedSongsRecycler.adapter=likedSongsAdapter
        likedSongsRecycler.visibility=View.INVISIBLE
        progress.visibility=View.VISIBLE

        viewModel.getLikedSongsIds().observe(this.viewLifecycleOwner, Observer {
            viewModel.getLikedSongs(it).observe(this.viewLifecycleOwner, Observer {
                likedSongs.clear()
                it?.forEach {
                    likedSongs.add(it!!)
                }
                likedSongsAdapter.notifyDataSetChanged()
                likedSongsRecycler.visibility=View.VISIBLE
                progress.visibility=View.INVISIBLE
            })
        })



        return view
    }

}