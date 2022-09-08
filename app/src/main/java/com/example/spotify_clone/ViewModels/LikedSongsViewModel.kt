package com.example.spotify_clone.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.Track
import com.example.spotify_clone.Repository.DataRepository

class LikedSongsViewModel: ViewModel() {
    private lateinit var repo: DataRepository
    private lateinit var userId: String
    init {
        repo= DataRepository.getInstance()
    }
    fun getLikedSongs(likedSongList: List<String>): MutableLiveData<List<Track?>?> {
        return repo.getLikedSongs(this.userId,likedSongList)
    }

    fun setUserId(userId: String) {
        this.userId=userId
    }
    fun getUserId(): String {
        return this.userId
    }

    fun getLikedSongsIds(): MutableLiveData<List<String>> {
        return repo.getLikedSongsIds(this.userId)
    }
}