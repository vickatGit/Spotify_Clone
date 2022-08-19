package com.example.spotify_clone.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.Artist
import com.example.spotify_clone.Repository.DataRepository

class HomeFragmentViewModel: ViewModel() {
    private lateinit var repo:DataRepository
    init {
        repo=DataRepository()
        Log.d("TAG", "viewmodel: ")
    }
    fun fetchPopularArtists(): MutableLiveData<List<Artist>?> {
        return repo.fetchArtists()
    }

}