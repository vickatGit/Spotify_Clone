package com.example.spotify_clone.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify_clone.Adapters.SearchSongAdapter
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.R
import com.example.spotify_clone.ViewModels.SearchSongFragmentViewModel
import com.google.android.material.chip.ChipGroup

class SearchSongFragment : Fragment() {

    private lateinit var searchCard:CardView
    private lateinit var searchClick:TextView
    private lateinit var searchSong:androidx.appcompat.widget.SearchView
    private lateinit var viewModel:SearchSongFragmentViewModel
    private lateinit var searchedResultsRecycler:RecyclerView
    private lateinit var searchedResultesAdapter: SearchSongAdapter
    private  var searchedResults=ArrayList<Thumbnail>(1)
    private lateinit var searchFilters:ChipGroup

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
        searchedResultsRecycler.layoutManager=LinearLayoutManager(this.requireContext())
        searchedResultesAdapter=SearchSongAdapter(searchedResults)
        searchedResultsRecycler.adapter=searchedResultesAdapter


        viewModel.searchSong(null).observe(this.viewLifecycleOwner, Observer {
            if(it!=null && it.size>0) {
            Log.d("TAG", "onCreateView: observing searches"+it?.get(0)?.type)
                if (it?.get(0)?.type == "playlist" || it?.get(0)?.type == "album") {
                    searchedResultsRecycler.layoutManager = GridLayoutManager(this.requireContext(),2)
                    searchedResults.clear()
                    searchedResults.addAll(it)
                    searchedResultesAdapter.notifyDataSetChanged()
                } else {
                    searchedResultsRecycler.layoutManager=GridLayoutManager(this.requireContext(),1)
                    searchedResults.clear()
                    searchedResults.addAll(it!!)
                    searchedResultesAdapter.notifyDataSetChanged()
                }
            }
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
        searchFilters.setOnCheckedStateChangeListener { group, checkedIds ->
            fetchResults(searchSong.query.toString())
        }
        searchSong.setOnQueryTextListener(object:SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query!=null && query!="") {
                    fetchResults(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!=null && newText!="") {
                    fetchResults(newText)
                }
                return true
            }
        })
        return view
    }

    private fun fetchResults( text:String){
        Log.d("TAG", "fetchResults: $text")
        when(searchFilters.checkedChipId){
            R.id.song_filter -> viewModel.searchSong(text)
            R.id.artist_filter -> viewModel.searchArtist(text)
            R.id.playlist_filter -> viewModel.searchPlaylist(text)
            R.id.album_filter -> viewModel.searchAlbum(text)
        }
    }

    private fun initialise(view: View?) {
        searchCard=view?.findViewById(R.id.search_card)!!
        searchSong=view?.findViewById(R.id.search_song)!!
        searchClick=view?.findViewById(R.id.search_click)!!
        searchedResultsRecycler=view?.findViewById(R.id.searched_results)!!
        searchFilters=view?.findViewById(R.id.search_filters)
    }


}