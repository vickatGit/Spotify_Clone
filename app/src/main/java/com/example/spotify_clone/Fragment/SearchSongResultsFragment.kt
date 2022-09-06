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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Track
import com.example.spotify_clone.Adapters.SearchSongAdapter
import com.example.spotify_clone.R
import com.example.spotify_clone.ViewModels.SearchSongFragmentViewModel


class SearchSongResultsFragment : Fragment() {

    private lateinit var searchedResultsRecycler:RecyclerView
    private lateinit var searchedResultesAdapter:SearchSongAdapter
    private lateinit var viewModel:SearchSongFragmentViewModel
    var searchedResults=ArrayList<Track>(1)
    var searchQuery:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance=true
        viewModel= ViewModelProvider(this).get(SearchSongFragmentViewModel::class.java)
        searchQuery= arguments?.getString("searched_query").toString()
        Log.d("TAG", "onCreateView: searchQuery $searchQuery")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("TAG", "fetchResults: added ")
        val view=inflater.inflate(R.layout.fragment_search_song_results, container, false)
        searchedResultsRecycler=view?.findViewById(R.id.searched_song_results)!!
        searchedResultsRecycler.layoutManager= LinearLayoutManager(this.requireContext())
        searchedResultesAdapter= SearchSongAdapter(searchedResults,viewModel,this)
        searchedResultsRecycler.adapter=searchedResultesAdapter


        if(searchQuery!="") {

            viewModel.searchSong(searchQuery).observe(this.viewLifecycleOwner, Observer {
                if (it != null && it.size > 0) {
                    Log.d("TAG", "onCreateView: observing searches" + it)

                    searchedResultsRecycler.layoutManager =
                        GridLayoutManager(this.requireContext(), 1)
                    searchedResults.clear()
                    searchedResults.addAll(it!!)
                    searchedResultsRecycler.visibility = View.VISIBLE
                    searchedResultesAdapter.notifyDataSetChanged()

                }
            })
        }

        return view
    }

}