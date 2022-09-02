package com.example.spotify_clone.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.Album
import com.adamratzman.spotify.models.SimpleTrack
import com.example.spotify_clone.Repository.DataRepository

class AlbumViewModel: ViewModel() {

    private var id: String?=null
    private lateinit var repo: DataRepository
    init {
        repo= DataRepository.getInstance()
    }
    fun fetchAlbumTracks(albumId: String): MutableLiveData<List<SimpleTrack>?> {
        return repo.fetchAlbumTracks(albumId)
    }
    fun fetchAlbumInfo(albumId: String): MutableLiveData<Album?> {
        return repo.fetchAlbumInfo(albumId)
    }

}