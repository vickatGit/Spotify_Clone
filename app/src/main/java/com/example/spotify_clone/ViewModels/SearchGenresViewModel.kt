package com.example.spotify_clone.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.Repository.DataRepository

class SearchGenresViewModel: ViewModel() {
    private lateinit var repo: DataRepository
    init {
        repo= DataRepository.getInstance()
    }
    fun getGenres(): MutableLiveData<List<String>> {
        return repo.getGenres()
    }

}

//class SearchSongFragmentViewModel: ViewModel() {
//
//    private lateinit var repo: DataRepository
//    init {
//        repo= DataRepository.getInstance()
//    }
//    fun search(newText: String?): MutableLiveData<List<Thumbnail>?> {
//        if(newText!=null) {
//            return repo.search(newText)
//        }
//        else{
//            return repo.getSearches()
//        }
//    }
//
//}