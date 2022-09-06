package com.example.spotify_clone.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify_clone.Adapters.SearchPlaylistAdapter
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.R
import com.example.spotify_clone.ViewModels.SearchSongFragmentViewModel

class SearchPlaylistResultsFragment : Fragment() {


    private lateinit var searchedPlaylisRecycler:RecyclerView
    private lateinit var viewModel:SearchSongFragmentViewModel
    private lateinit var searchedPlaylisAdapter:SearchPlaylistAdapter
    var searchedResults=ArrayList<Thumbnail>(1)
    var searchQuery:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= ViewModelProvider(this).get(SearchSongFragmentViewModel::class.java)
        searchQuery= arguments?.getString("searched_query").toString()
        Log.d("TAG", "onCreateView: searchQuery $searchQuery")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_search_playlist_results, container, false)
        searchedPlaylisRecycler=view.findViewById(R.id.searched_playlist_results)
        searchedPlaylisRecycler.layoutManager= GridLayoutManager(this.requireContext(),2)
        searchedPlaylisAdapter= SearchPlaylistAdapter(searchedResults)
        searchedPlaylisRecycler.adapter=searchedPlaylisAdapter


        if(searchQuery!="") {

            viewModel.searchPlaylist(searchQuery).observe(this.viewLifecycleOwner, Observer {
                if (it != null && it.size > 0) {
                    Log.d("TAG", "onCreateView: observing searches" + it)

                    searchedPlaylisRecycler.layoutManager =
                        GridLayoutManager(this.requireContext(), 2)
                    searchedResults.clear()
                    searchedResults.addAll(it!!)
                    searchedPlaylisRecycler.visibility = View.VISIBLE
                    searchedPlaylisAdapter.notifyDataSetChanged()

                }
            })
        }

        return view
    }

}