package com.example.spotify_clone.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.Track
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.Repository.DataRepository

class SearchSongFragmentViewModel: ViewModel() {

    private lateinit var repo: DataRepository
    init {
        repo= DataRepository.getInstance()
    }
    fun searchSong(newText: String?): MutableLiveData<List<Track>?> {
//        if(newText!=null) {
            return repo.searchSong(newText)
//        }
//        else{
//            return repo.getSearches()
//        }
    }
//    fun searchArtist(text:String){
//        repo.searchArtists(text)
//    }
//    fun searchAlbum(text: String) {
//        repo.searchAlbums(text)
//    }
//    fun searchPlaylist(text:String){
//        repo.searchPlaylist(text)
//    }

    fun getSong(songId: String): MutableLiveData<Track?> {
        Log.d("TAG", "getSong: insearchsong viewmodel"+songId)
        return repo.getSong(songId)
    }


}