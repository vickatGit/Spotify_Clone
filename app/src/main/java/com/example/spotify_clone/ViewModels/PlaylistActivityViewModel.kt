package com.example.spotify_clone.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.PlaylistTrack
import com.example.spotify_clone.Repository.DataRepository
import com.example.spotify_clone.SpotifyActivity

class PlaylistActivityViewModel: ViewModel() {
    private var id: String?=null
    private lateinit var repo:DataRepository
    init {
        repo=DataRepository.getInstance()
    }
    fun setId(id: String?) {
        this.id=id
        SpotifyActivity.currentPlaylistId=id
    }

    fun fetchTracks(type:String) {
        repo.fetchTracks(id,type)
    }
    fun getFetchedTracks(): MutableLiveData<List<PlaylistTrack>> {
        return repo.getFetchedTracks()
    }

    fun getPlaylistInfo(): MutableLiveData<Playlist> {
        return repo.getPlaylistInfo(id)
    }

}