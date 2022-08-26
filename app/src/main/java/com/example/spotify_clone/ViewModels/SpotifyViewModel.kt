package com.example.spotify_clone.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.Track
import com.example.spotify_clone.Repository.DataRepository

class SpotifyViewModel: ViewModel() {

    private lateinit var repo: DataRepository
    init {
        repo= DataRepository.getInstance()
    }
    fun fetcheNextSong(linkedTrackId: String?): MutableLiveData<Track> {
        return repo.fetchNextSong(linkedTrackId)
    }
}