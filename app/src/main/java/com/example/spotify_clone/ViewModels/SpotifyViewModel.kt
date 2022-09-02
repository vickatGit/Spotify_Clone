package com.example.spotify_clone.ViewModels

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.Track
import com.example.spotify_clone.Fragment.PlaylistFragment
import com.example.spotify_clone.Models.ApiRelatedModels.TrackModel
import com.example.spotify_clone.Repository.DataRepository

class SpotifyViewModel: ViewModel() {

    private lateinit var repo: DataRepository
    private var userFragmentNavigationHistory=ArrayList<Fragment>(1)
    private var playlistTracks=ArrayList<TrackModel>(1)
    private var currentSongPosition:Int?=null
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
}