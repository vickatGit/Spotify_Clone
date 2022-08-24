package com.example.spotify_clone.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Track
import com.example.spotify_clone.Adapters.SearchSongAdapter
import com.example.spotify_clone.R
import com.example.spotify_clone.ViewModels.SearchSongFragmentViewModel

class SearchSongFragment : Fragment() {

    private lateinit var searchCard:CardView
    private lateinit var searchClick:TextView
    private lateinit var searchSong:androidx.appcompat.widget.SearchView
    private lateinit var viewModel:SearchSongFragmentViewModel
    private lateinit var searchedResults:RecyclerView
    private lateinit var searchedResultesAdapter: SearchSongAdapter
    private  var searchedTracks=ArrayList<Track>(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=ViewModelProvider(this).get(SearchSongFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_search_song, container, false)
        initialise(view)
        searchedResults.layoutManager=LinearLayoutManager(this.requireContext())
        searchedResultesAdapter=SearchSongAdapter(searchedTracks)
        searchedResults.adapter=searchedResultesAdapter


        viewModel.search(null).observe(this.viewLifecycleOwner, Observer {
            searchedTracks.clear()
            searchedTracks.addAll(it)
            searchedResultesAdapter.notifyDataSetChanged()
        })
        searchCard.setOnClickListener {
            searchSong.visibility=View.VISIBLE
            searchSong.isIconified=false
            searchClick.visibility=View.GONE
        }
        searchSong.setOnCloseListener {
            searchSong.visibility=View.GONE
            searchClick.visibility=View.VISIBLE
            return@setOnCloseListener true
        }
        searchSong.setOnQueryTextListener(object:SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText)
                return true
            }


        })

        return view
    }

    private fun initialise(view: View?) {
        searchCard=view?.findViewById(R.id.search_card)!!
        searchSong=view?.findViewById(R.id.search_song)!!
        searchClick=view?.findViewById(R.id.search_click)!!
        searchedResults=view?.findViewById(R.id.searched_results)!!
    }


}