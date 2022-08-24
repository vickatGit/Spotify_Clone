package com.example.spotify_clone.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.Track
import com.example.spotify_clone.Repository.DataRepository

class SearchSongFragmentViewModel: ViewModel() {

    private lateinit var repo: DataRepository
    init {
        repo= DataRepository.getInstance()
    }
    fun search(newText: String?): MutableLiveData<List<Track>> {
        if(newText!=null) {
            return repo.search(newText)
        }
        else{
            return repo.getSearches()
        }
    }

}