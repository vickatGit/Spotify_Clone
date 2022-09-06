package com.example.spotify_clone.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spotify_clone.Models.ApiRelatedModels.GenresModel
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.Repository.DataRepository

class SearchGenresViewModel: ViewModel() {
    private lateinit var repo: DataRepository
    init {
        repo= DataRepository.getInstance()
    }
    fun getGenres(): MutableLiveData<List<GenresModel>> {
        return repo.getGenres()
    }

    fun getGenresPlaylist(genres: String): MutableLiveData<List<Thumbnail>?> {
        return repo.getGenresPlaylist(genres)
    }

}
