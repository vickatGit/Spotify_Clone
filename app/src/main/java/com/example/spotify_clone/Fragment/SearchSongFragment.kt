package com.example.spotify_clone.Fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Track
import com.example.spotify_clone.Adapters.SearchSongAdapter
import com.example.spotify_clone.R
import com.example.spotify_clone.ViewModels.SearchSongFragmentViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class SearchSongFragment : Fragment() {

    private lateinit var searchCard:CardView
    private lateinit var searchClick:TextView
    private lateinit var searchSong:androidx.appcompat.widget.SearchView
    private lateinit var viewModel:SearchSongFragmentViewModel
    private lateinit var searchedResultsRecycler:RecyclerView
    private lateinit var searchedResultesAdapter: SearchSongAdapter
    private  var searchedResults=ArrayList<Track>(1)
    private lateinit var searchFilters:ChipGroup
    private lateinit var layoutView:View
    private lateinit var songFilter:Chip
    private lateinit var progress:ProgressBar
    private lateinit var playlistFilter:Chip
    private lateinit var frameLayout:FrameLayout
    var chipList=ArrayList<Chip>(4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance=true
        viewModel=ViewModelProvider(this).get(SearchSongFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_search_song, container, false)
        initialise(view)
//        searchedResultsRecycler.layoutManager=LinearLayoutManager(this.requireContext())
//        searchedResultesAdapter=SearchSongAdapter(searchedResults,viewModel,this)
//        searchedResultsRecycler.adapter=searchedResultesAdapter


//        viewModel.searchSong(null).observe(this.viewLifecycleOwner, Observer {
//            if(it!=null && it.size>0) {
//            Log.d("TAG", "onCreateView: observing searches"+it)
//                if (it?.get(0)?.type == "playlist" || it?.get(0)?.type == "album") {
//                    searchedResultsRecycler.layoutManager = GridLayoutManager(this.requireContext(),2)
//                    searchedResults.clear()
//                    searchedResults.addAll(it)
//                    progress.visibility=View.INVISIBLE
//                    searchedResultsRecycler.visibility=View.VISIBLE
//                    searchedResultesAdapter.notifyDataSetChanged()
//                } else {
//                    searchedResultsRecycler.layoutManager=GridLayoutManager(this.requireContext(),1)
//                    searchedResults.clear()
//                    searchedResults.addAll(it!!)
//                    progress.visibility=View.INVISIBLE
//                    searchedResultsRecycler.visibility=View.VISIBLE
//                    searchedResultesAdapter.notifyDataSetChanged()
//                }
//            }
//        })
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
                    Log.d("TAG", "onQueryTextChange: searchQuery in search $newText")
//                    progress.visibility=View.VISIBLE
//                    searchedResultsRecycler.visibility=View.INVISIBLE
                }
                return true
            }
        })
        layoutView=view
        return view
    }

    private fun fetchResults( text:String){
        when(searchFilters.checkedChipId){
            R.id.song_filter -> {
//                viewModel.searchSong(text)
                val frag=SearchSongResultsFragment()
                val bundle=Bundle()
                Log.d("TAG", "onQueryTextChange: searchQuery in search $text")
                bundle.putString("searched_query",text)
                frag.arguments=bundle
                childFragmentManager.beginTransaction().replace(R.id.search_fragment,frag).commit()
                Log.d("TAG", "fetchResults: adding ")


                songFilter.chipStrokeColor= ColorStateList.valueOf(resources.getColor(R.color.secondaryTextColor))
                playlistFilter.chipStrokeColor= ColorStateList.valueOf(resources.getColor(R.color.secondaryLightColor))
            }
            R.id.playlist_filter -> {

                Log.d("TAG", "onQueryTextChange: searchQuery in search $text")
                val frag=SearchPlaylistResultsFragment()
                val bundle=Bundle()
                bundle.putString("searched_query",text)
                frag.arguments=bundle
                childFragmentManager.beginTransaction().replace(R.id.search_fragment,frag).commit()

                playlistFilter.chipStrokeColor= ColorStateList.valueOf(resources.getColor(R.color.secondaryTextColor))
                songFilter.chipStrokeColor= ColorStateList.valueOf(resources.getColor(R.color.secondaryLightColor))
            }
        }
    }

    fun deselectChips( selectedChip:Int){
        for (i in 0..chipList.size-1){
            if(i!=selectedChip)
                chipList.get(i).setBackgroundDrawable(ContextCompat.getDrawable(this.requireContext(),R.drawable.next_button_bg))
        }
    }

    private fun initialise(view: View?) {

        searchCard=view?.findViewById(R.id.search_card)!!
        searchSong=view?.findViewById(R.id.search_song)!!
        searchClick=view?.findViewById(R.id.search_click)!!
        searchFilters=view?.findViewById(R.id.search_filters)
        songFilter=view?.findViewById(R.id.song_filter)
        playlistFilter=view?.findViewById(R.id.playlist_filter)
        chipList.add(songFilter)
        progress=view?.findViewById(R.id.progress)
        chipList.add(playlistFilter)
        songFilter.chipStrokeColor= ColorStateList.valueOf(resources.getColor(R.color.secondaryTextColor))
        playlistFilter.chipStrokeColor= ColorStateList.valueOf(resources.getColor(R.color.secondaryLightColor))
        searchFilters.check(R.id.song_filter)

        frameLayout=view.findViewById(R.id.search_fragment)
    }


}