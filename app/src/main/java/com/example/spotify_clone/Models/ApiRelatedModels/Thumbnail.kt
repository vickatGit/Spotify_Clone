package com.example.spotify_clone.Models.ApiRelatedModels

import androidx.lifecycle.MutableLiveData

data class Thumbnail(
    val imageUrl:String, val name:String, val type:String, val next:String,
    var observer: MutableLiveData<List<Thumbnail>?>
)