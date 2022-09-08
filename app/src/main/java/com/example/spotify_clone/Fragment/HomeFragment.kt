package com.example.spotify_clone.Fragment

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify_clone.Adapters.ParentClassAdapter
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.R
import com.example.spotify_clone.SpotifyActivity
import com.example.spotify_clone.ViewModels.HomeFragmentViewModel

class HomeFragment : Fragment() {
    private lateinit var viewModel:HomeFragmentViewModel
    private lateinit var allListsRecyclerView:RecyclerView
    private lateinit var topScroller:ScrollView
    @Volatile
    private lateinit var allLists:ArrayList<List<Thumbnail>>
    private lateinit var allListAdapter: ParentClassAdapter

    companion object{
        private val TAG="home_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: lifecycle")
        retainInstance=true
        viewModel=ViewModelProvider(this.requireActivity()).get(HomeFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(this.resources.configuration.orientation== Configuration.ORIENTATION_LANDSCAPE){
            SpotifyActivity.landHomeFragment.setBackgroundColor(resources.getColor(R.color.secondaryLightColor))
            SpotifyActivity.landSearchFragment.setBackgroundColor(resources.getColor(R.color.spotify_background))
            SpotifyActivity.landLibraryFragment.setBackgroundColor(resources.getColor(R.color.spotify_background))
        }else {
            SpotifyActivity.bottomNavigationView.menu.findItem(R.id.home).setChecked(true)
        }
        Log.d(TAG, "onCreateView: lifecycle")
        val view=inflater.inflate(R.layout.fragment_home, container, false)
        initialiseViews(view)

        allLists= ArrayList(1)
        allListsRecyclerView.layoutManager=LinearLayoutManager(this.context)
        allListAdapter=ParentClassAdapter(allLists,this.requireActivity(),viewModel)
        allListsRecyclerView.adapter=allListAdapter


        viewModel.getFetchedLists().observe(this.requireActivity(), Observer {
            if(it!=null){
                allLists.clear()
                Log.d("TAG", "onCreateView: parent notify data set changed and size is ${it}")
                allLists.addAll(it)
                allListAdapter.notifyDataSetChanged()
            }
        })
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: ")
    }

    override fun onAttach(activity: Activity) {
        Log.d(TAG, "onAttach: lifecycle")
        super.onAttach(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: lifecycle")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG, "onStart: lifecycle")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume: lifecycle")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause: lifecycle")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop: lifecycle")
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: lifecycle")
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach: lifecycle")
        super.onDetach()
    }

    private fun initialiseViews(view: View?) {
        allListsRecyclerView=view?.findViewById(R.id.all_lists)!!

    }
}