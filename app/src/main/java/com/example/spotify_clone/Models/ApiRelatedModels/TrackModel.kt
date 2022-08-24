package com.example.spotify_clone.Models.ApiRelatedModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackModel(
    val id: String?,
    val image: String?,
    val name: String?,
    val artist: String?,
    val duration: Int?,
    val linkedTrackId: String?,
    val previewUrl: String?
) : Parcelable
