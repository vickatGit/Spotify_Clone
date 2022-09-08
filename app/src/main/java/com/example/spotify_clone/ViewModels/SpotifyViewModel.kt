package com.example.spotify_clone.ViewModels

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.Track
import com.example.spotify_clone.Models.ApiRelatedModels.TrackModel
import com.example.spotify_clone.Repository.DataRepository
import com.example.spotify_clone.SpotifyActivity

class SpotifyViewModel: ViewModel() {

    private lateinit var repo: DataRepository
    private var userFragmentNavigationHistory=ArrayList<Fragment>(1)
    private var playlistTracks=ArrayList<TrackModel>(1)
    private var currentSongPosition:Int?=null
    private var userId:String? = null
    init {
        repo= DataRepository.getInstance()
    }
    fun fetcheNextSong(linkedTrackId: String?): MutableLiveData<Track> {
        return repo.fetchNextSong(linkedTrackId)
    }

    fun setCurrentFragment(pushFragment: Fragment) {
        userFragmentNavigationHistory.add(pushFragment)
        Log.d("backstack", "onCreate: viewmodel"+userFragmentNavigationHistory.size)
    }

    fun popFragmentHistoty() {
        userFragmentNavigationHistory.removeAt(userFragmentNavigationHistory.size-1)
    }

    fun setPlaylistTracks(playlistTracks: ArrayList<TrackModel>) {
        this.playlistTracks.addAll(playlistTracks)
    }
    fun getPlaylistTracks(): ArrayList<TrackModel> {
        return playlistTracks
    }

    fun setCurrentSong(songInfo: Int?) {
        if (songInfo != null) {
            this.currentSongPosition=songInfo
        }
    }
    fun getCurrentSongPosition(): Int? {
        return currentSongPosition
    }

    fun addToFavourites(id: String?, currentPlaylistId: String?, spotifyActivity: SpotifyActivity) {
        repo.addToFavourites(id,this.userId,currentPlaylistId,spotifyActivity.applicationContext)
    }

    fun setUserId(userId: String?) {
        this.userId=userId
    }
    fun getUserId(): String? {
        return userId
    }

    fun removeFromFavourites(id: String?, currentPlaylistId: String?,spotifyActivity: SpotifyActivity) {
        repo.removeFromFavourites(id,this.userId,currentPlaylistId,spotifyActivity.applicationContext)
    }

    fun getFavouriteSongs(): MutableLiveData<List<String>?> {
        return repo.getSongFavourites(userId)
    }

    fun saveLastPlayback(currentSong: TrackModel?) {
        repo.saveLastPlayback(currentSong,userId)
    }
    fun getLastPlayback(): MutableLiveData<TrackModel> {
        return repo.getLastPlayback(userId)
    }


}