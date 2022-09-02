package com.example.spotify_clone.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify_clone.Adapters.SearchGenresAdapter
import com.example.spotify_clone.R
import com.example.spotify_clone.SpotifyActivity
import com.example.spotify_clone.ViewModels.SearchGenresViewModel


class SearchFragment(val spotifyActivity: SpotifyActivity) : Fragment() {

    private lateinit var search:CardView
    private lateinit var genresRecycler:RecyclerView
    private lateinit var genresAdapter:SearchGenresAdapter
    private lateinit var viewmodel:SearchGenresViewModel
    private var allGenres=ArrayList<String>(1)
    private lateinit var searchGenresAdapter: SearchGenresAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance=true

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view=inflater.inflate(R.layout.fragment_search, container, false)
        initialise(view)
        searchGenresAdapter= SearchGenresAdapter(allGenres)
        genresRecycler.layoutManager=GridLayoutManager(this.requireContext(),2)
        genresRecycler.adapter=searchGenresAdapter
        viewmodel=ViewModelProvider(this).get(SearchGenresViewModel::class.java)
        viewmodel.getGenres().observe(this.viewLifecycleOwner, Observer {
            allGenres.clear()
            allGenres.addAll(it)
            searchGenresAdapter.notifyDataSetChanged()

        })
        search.setOnClickListener {
            var intent:Intent= Intent()
            intent.setAction(SpotifyActivity.FRAG_RECIEVER)
            intent.putExtra("type","search_fragment")
            LocalBroadcastManager.getInstance(this.requireContext()).sendBroadcast(intent)

        }


        return view
    }
    fun initialise(view: View?){
        search=view?.findViewById(R.id.search)!!
        genresRecycler=view?.findViewById(R.id.genres)!!
    }

}