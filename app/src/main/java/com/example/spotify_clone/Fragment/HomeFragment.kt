package com.example.spotify_clone.Fragment

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.SimplePlaylist
import com.example.spotify_clone.Adapters.HomeTopAdapter
import com.example.spotify_clone.Adapters.ParentClassAdapter
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.R
import com.example.spotify_clone.ViewModels.HomeFragmentViewModel

class HomeFragment : Fragment() {
    private lateinit var viewModel:HomeFragmentViewModel
//    private lateinit var artists:RecyclerView
    private lateinit var allListsRecyclerView:RecyclerView
    private lateinit var topScroller:ScrollView
//    private lateinit var allArtists:ArrayList<Artist>
    private lateinit var allLists:ArrayList<List<Thumbnail>>
//    private lateinit var allCategories:ArrayList<SimplePlaylist>
    private lateinit var allListAdapter: ParentClassAdapter
//    private lateinit var homeTopCategoryAdaptor:HomeTopAdapter
//    private lateinit var actionBarView:View
//    private lateinit var gridView:RecyclerView


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

        allLists= ArrayList(1)
//        gridView.layoutManager=GridLayoutManager(this.context,2)
        allListsRecyclerView.layoutManager=LinearLayoutManager(this.context)
        allListAdapter=ParentClassAdapter(allLists,this.requireActivity(),viewModel)
        allListsRecyclerView.adapter=allListAdapter

//        allCategories= ArrayList(5)



//        homeTopCategoryAdaptor= HomeTopAdapter(allCategories)
//        gridView.adapter=homeTopCategoryAdaptor

//        artists.adapter=artistsAdapter

        viewModel.getFetchedLists().observe(this.requireActivity(), Observer {
            if(it!=null){
                allLists.clear()
                Log.d("TAG", "onCreateView: parent notify data set changed and size is ${it}")
                allLists.addAll(it)
//                allArtists.clear()
//                allArtists.addAll(it)
//                artistsAdapter.notifyDataSetChanged()
                allListAdapter.notifyDataSetChanged()
            }
        })
        return view
    }

    private fun initialiseViews(view: View?) {
//        artists= view?.findViewById(R.id.all_lists)!!
//        actionBarView=view.findViewById(R.id.action_bar_gradient)
//        gridView=view.findViewById(R.id.layout_grid)
        allListsRecyclerView=view?.findViewById(R.id.all_lists)!!
//        artists.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
//        allArtists= ArrayList<Artist>()

    }
}