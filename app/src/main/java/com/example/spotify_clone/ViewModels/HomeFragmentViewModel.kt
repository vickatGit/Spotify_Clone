package com.example.spotify_clone.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.SpotifyCategory
import com.example.spotify_clone.Repository.DataRepository

class HomeFragmentViewModel: ViewModel() {
    private lateinit var repo:DataRepository
    init {
        repo=DataRepository()
        repo.fetchArtists()
        repo.fetchTopList()
        Log.d("TAG", "viewmodel: ")
    }
    fun fetchedPopularArtist(): MutableLiveData<List<Artist>?> {
        return repo.fetchedArists()
    }
    fun getFetchedCategories(): MutableLiveData<List<SimplePlaylist>> {
        return repo.getAllCatgories()
    }

}