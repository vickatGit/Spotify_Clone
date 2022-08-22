package com.example.spotify_clone.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.PlaylistTrack
import com.example.spotify_clone.Repository.DataRepository

class PlaylistActivityViewModel: ViewModel() {
    private var id: String?=null
    private lateinit var repo:DataRepository
    init {
        repo=DataRepository.getInstance()
    }
    fun setId(id: String?) {
        this.id=id
    }

    fun fetchTracks() {
        repo.fetchTracks(id)
    }
    fun getFetchedTracks(): MutableLiveData<List<PlaylistTrack>> {
        return repo.getFetchedTracks()
    }

    fun getPlaylistInfo(): MutableLiveData<Playlist> {
        return repo.getPlaylistInfo(id)
    }

}