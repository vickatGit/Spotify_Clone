package com.example.spotify_clone.Fragment

import android.graphics.drawable.GradientDrawable
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Artist
import com.example.spotify_clone.Adapters.PopularArtistAdapter
import com.example.spotify_clone.R
import com.example.spotify_clone.ViewModels.HomeFragmentViewModel

class HomeFragment : Fragment() {
    private lateinit var viewModel:HomeFragmentViewModel
    private lateinit var artists:RecyclerView
    private lateinit var allArtists:ArrayList<Artist>
    private lateinit var artistsAdapter:PopularArtistAdapter
    private lateinit var actionBarView:View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=ViewModelProvider(this.requireActivity()).get(HomeFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_home, container, false)
        initialiseViews(view)

        artistsAdapter=PopularArtistAdapter(allArtists)
        artists.adapter=artistsAdapter
        viewModel.fetchPopularArtists().observe(this.requireActivity(), Observer {
            if(it!=null){
                allArtists.clear()
                allArtists.addAll(it)
                artistsAdapter.notifyDataSetChanged()
            }
        })
        return view
    }

    private fun initialiseViews(view: View?) {
        artists= view?.findViewById(R.id.popular_artists)!!
        actionBarView=view.findViewById(R.id.action_bar_gradient)
        artists.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        allArtists= ArrayList<Artist>()

    }
}