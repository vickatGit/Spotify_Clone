package com.example.spotify_clone.Repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import at.favre.lib.crypto.bcrypt.BCrypt
import com.adamratzman.spotify.models.*
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.utils.Market
import com.example.spotify_clone.Fragment.PlaylistFragment
import com.example.spotify_clone.Models.ApiRelatedModels.GenresModel
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.Models.ApiRelatedModels.TrackModel
//import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
//import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.Models.UserModel
import com.example.spotify_clone.SpotifyActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DataRepository {
    val db = FirebaseFirestore.getInstance()
    var user: UserModel? = null
    var isExist: Boolean = false
    var some: DocumentSnapshot? = null
    //Login Related Task


    private var isEmailExist:MutableLiveData<Boolean> = MutableLiveData()
    private var isUserExist:MutableLiveData<UserModel> = MutableLiveData()











    //Login Related task is ended

    private var allLists: MutableLiveData<List<List<Thumbnail>>> = MutableLiveData()

    //    private var allArtists:MutableLiveData<List<Artist>?> = MutableLiveData()
    private var commonLists = ArrayList<List<Thumbnail>>(1)
    private var allArtists: MutableLiveData<List<Thumbnail>?> = MutableLiveData()
    private var favouriteSongs: MutableLiveData<List<String>?> = MutableLiveData()
    private var featuredPlaylists: MutableLiveData<List<Thumbnail>?> = MutableLiveData()
    private var chillPlaylist: MutableLiveData<List<Thumbnail>?> = MutableLiveData()
    private var genresPlaylist: MutableLiveData<List<Thumbnail>?> = MutableLiveData()
    private var sadPlaylist: MutableLiveData<List<Thumbnail>?> = MutableLiveData()
    private var recommendations: MutableLiveData<List<Thumbnail>?> = MutableLiveData()
    private var allCategories: MutableLiveData<List<SimplePlaylist>> = MutableLiveData()
    private var indianCategories: ArrayList<MutableLiveData<List<Thumbnail>?>> = ArrayList(7)

    private var allTracks: MutableLiveData<List<PlaylistTrack>> = MutableLiveData()
    private var playlistInfo: MutableLiveData<Playlist> = MutableLiveData()

    private var genres: MutableLiveData<List<GenresModel>> = MutableLiveData()
    private var searchedTracks: MutableLiveData<List<Track>?> = MutableLiveData()
    private var lastPlayback: MutableLiveData<TrackModel> = MutableLiveData()
    private var likedTracks: MutableLiveData<List<Track?>?> = MutableLiveData()
    private var searchedPlaylist: MutableLiveData<List<Thumbnail>?> = MutableLiveData()
    private var likedSongsIds: MutableLiveData<List<String>> = MutableLiveData()
    private var nextSong: MutableLiveData<Track> = MutableLiveData()
    private var albumTracks:MutableLiveData<List<SimpleTrack>?> = MutableLiveData()
    private var albumInfo:MutableLiveData<Album?> = MutableLiveData()
    private var getTrack:MutableLiveData<Track?> = MutableLiveData()

    companion object {

        private val CLIENT_ID = "a7ade92373684af7b78d8382b1031827"
        private val CLIENT_SECRET = "4b8c41c29cfd497298b910335d9599a1"
        private val TAG = "tag"

        private var dataRepository = DataRepository()
        fun getInstance(): DataRepository {
            return dataRepository
        }
    }

    fun createUser(user: UserModel): MutableLiveData<UserModel> {

        val docRef = db.collection("Users").document().id
        user.setuserReference(docRef.toString())
        db.collection("Users").document(docRef.toString()).set(user).addOnCompleteListener {
            if (it.isSuccessful) {
                isUserExist(user.email, user.password)
                Log.d(TAG, "createUser: user account is successfully created")
            }
        }.addOnFailureListener {
            Log.d(TAG, "createUser: cant create user")
        }
        return isUserExist(user.email, user.password)
    }

    fun isUserExist(email: String, password: String): MutableLiveData<UserModel> {
        db.collection("Users").whereEqualTo("email", email).limit(1).get().addOnCompleteListener {
            if (it.isSuccessful) {
                it.result.documents.forEach {
                    some = it
                    if (some != null) {
                        user = UserModel(some?.get("email").toString(),
                            some?.get("password").toString(),
                            user?.birth,
                            some?.get("gender").toString(),
                            some?.get("username").toString(),
                            some?.getString("userRef"))
                        val isUser=BCrypt.verifyer().verify(password.toCharArray(), user!!.password)
                        if(isUser.verified)
                            isUserExist.postValue(user)
                        else
                            isUserExist.postValue(null)
                    }
                }
            }
        }
        return isUserExist
    }

    fun GetUser(): UserModel? {
        if (some != null) {
            user = UserModel(
                some?.get("email").toString(),
                some?.get("password").toString(),
                null,
                some?.get("gender").toString(),
                some?.get("username").toString(),
                some?.getString("userRef")
            )
        }
        return user
    }

    fun isEmailAlreadyExist(email: String): MutableLiveData<Boolean> {


        db.collection("Users").whereEqualTo("email", email).get(Source.SERVER)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    if (it.result.size() == 0) {
                        isEmailExist.postValue(false)
                    }else{
                        isEmailExist.postValue(true)
                    }
                }
            }
        return isEmailExist

    }

    fun GetIsEmailExists(): MutableLiveData<Boolean> {
        return isEmailExist
    }

    fun fetchArtists() {
        CoroutineScope(Dispatchers.IO).launch {

            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            var allData: MutableLiveData<List<Thumbnail>> = MutableLiveData()
            val thumbnails = ArrayList<Thumbnail>(1)
            var thumb: Thumbnail
            api.artists.getArtists(
                "2CIMQHirSU0MQqyYHq0eOx",
                "57dN52uHvrHOxijzpIgu3E",
                "1vCWHaC5f2uS3yhpwWbIA6"
            ).forEach {
                val url = it?.images?.get(it.images.size - 2)?.url
                thumb = Thumbnail(
                    url.toString(),
                    it?.name.toString(),
                    it?.type.toString(),
                    it?.id.toString(),
                    allArtists,
                    "Popular Artists"
                )
                thumb.observer = allArtists
                thumbnails.add(thumb)

            }

//            commonLists.add(thumbnails)
            commonLists.add(thumbnails)

            allLists.postValue(commonLists)
            allArtists.postValue(thumbnails)
        }

    }

    fun fetchedLists(): MutableLiveData<List<List<Thumbnail>>> {
        return allLists
    }

    fun fetchTopList() {
        CoroutineScope(Dispatchers.IO).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            val categories = api.browse.getCategoryList(5, null, null, Market.IN)
            val gson = Gson()


            val playCategories: ArrayList<SpotifyCategory> = ArrayList()
            try {
                val obj = api.browse.getPlaylistsForCategory(categories.get(0).id, 6).items
                allCategories.postValue(obj)
            }catch (e:Exception){
                Log.d(TAG, "fetchTopList: ")
                
            }

        }
    }

    fun getAllCatgories(): MutableLiveData<List<SimplePlaylist>> {
        return allCategories
    }

    fun getFeaturedPlaylists() {
        CoroutineScope(Dispatchers.IO).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            var thumb: Thumbnail
            var thumbnails = ArrayList<Thumbnail>(1)
            try {


                api.browse.getFeaturedPlaylists(10, null, null, Market.US).playlists.items.forEach {
                    thumb = Thumbnail(
                        it.images.get(it.images.size - 1).url,
                        it.name,
                        it.type,
                        it.id,
                        featuredPlaylists,
                        "Featured Playlist"
                    )
                    thumbnails.add(thumb)

                }
                commonLists.add(thumbnails)

                allLists.postValue(commonLists)
                featuredPlaylists.postValue(thumbnails)
            }catch (e:Exception){
                Log.d(TAG, "getFeaturedPlaylists: exception")
            }
        }
    }

    fun getSadPlaylist() {
        CoroutineScope(Dispatchers.IO).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            var thumb: Thumbnail
            var thumbnails = ArrayList<Thumbnail>(1)
            try {

                api.browse.getPlaylistsForCategoryRestAction(
                    "sad",
                    10,
                    null,
                    Market.IN
                ).supplier.invoke().items.forEach {
                    thumb = Thumbnail(
                        it.images.get(it.images.size - 1).url,
                        it.name,
                        it.type,
                        it.id,
                        chillPlaylist,
                        "Pop Playlists"
                    )

                    thumbnails.add(thumb)

                }
                commonLists.add(thumbnails)
                allLists.postValue(commonLists)
                sadPlaylist.postValue(thumbnails)
            }catch (e:java.lang.Exception){
                Log.d(TAG, "getSadPlaylist: exception")
            }
        }
    }

    fun getChillPlaylist() {
        CoroutineScope(Dispatchers.IO).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            var thumb: Thumbnail
            var thumbnails = ArrayList<Thumbnail>(1)
            try {

                api.browse.getPlaylistsForCategoryRestAction(
                    "chill",
                    10,
                    null,
                    Market.IN
                ).supplier.invoke().items.forEach {
                    thumb = Thumbnail(
                        it.images.get(it.images.size - 1).url,
                        it.name,
                        it.type,
                        it.id,
                        chillPlaylist,
                        "Chill Playlists"
                    )

                    thumbnails.add(thumb)

                }
                commonLists.add(thumbnails)
                allLists.postValue(commonLists)
                chillPlaylist.postValue(thumbnails)
            }catch (e:Exception){
                Log.d(TAG, "getChillPlaylist: exception")
            }
        }
    }

    fun getGenresPlaylist( genres:String): MutableLiveData<List<Thumbnail>?> {
        var thumb: Thumbnail
        var thumbnails = ArrayList<Thumbnail>(1)
        CoroutineScope(Dispatchers.IO).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            try {

                api.browse.getPlaylistsForCategory(genres, 10, null, Market.IN).forEach {
                    api.browse.getPlaylistsForCategoryRestAction(
                        genres,
                        10,
                        null,
                        Market.IN
                    ).supplier.invoke().items.forEach {
                        thumb = Thumbnail(
                            it.images.get(it.images.size - 1).url,
                            it.name,
                            it.type,
                            it.id,
                            genresPlaylist,
                            genres + " Playlists"
                        )

                        thumbnails.add(thumb)

                    }
                    genresPlaylist.postValue(thumbnails)
                }
            }catch (e:java.lang.Exception){
                Log.d(TAG, "getGenresPlaylist: exception")
            }

        }
        return genresPlaylist
    }

    @SuppressLint("NewApi")
    fun getRecommendations() {
        CoroutineScope(Dispatchers.IO).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            var thumb: Thumbnail
            var thumbnails = ArrayList<Thumbnail>(1)
            api.browse.getNewReleases(10, null, Market.IN).items.forEach {
                var artists = ""
                it.artists.forEach {
                    artists = artists.plus(it.name + ",")
                    artists.plus(it.name)
                }
                thumb = Thumbnail(
                    it.images.get(it.images.size - 2).url,
                    artists,
                    it.type,
                    it.id,
                    recommendations,
                    "Recommanded for you"
                )
                thumbnails.add(thumb)

            }
            commonLists.add(thumbnails)
            allLists.postValue(commonLists)
            recommendations.postValue(thumbnails)

        }
    }



    @RequiresApi(Build.VERSION_CODES.N)
    fun getIndianCategoriesPlaylists() {
        CoroutineScope(Dispatchers.IO).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            try {

                api.browse.getCategoryList(5).items.forEach {
                    val name = it.name
                    var thumb: Thumbnail
                    var thumbnails = ArrayList<Thumbnail>(1)
                    var curpos = 0
                    CoroutineScope(Dispatchers.IO).launch {
                        api.browse.getPlaylistsForCategory(it.id, 7).items.forEach {
                            indianCategories.add(curpos, MutableLiveData())
                            thumb = Thumbnail(
                                it.images.get(0).url,
                                it.name,
                                it.type,
                                it.id,
                                indianCategories.get(curpos),
                                name
                            )
                            thumbnails.add(thumb)

                        }
                        commonLists.add(thumbnails)
                        allLists.postValue(commonLists)
                        indianCategories.get(curpos).postValue(thumbnails)
                        curpos++
                    }
                }
            }catch (e:Exception){
                Log.d(TAG, "getIndianCategoriesPlaylists: exception")
            }
        }
    }

    fun fetchTracks(id: String?,type:String) {

        CoroutineScope(Dispatchers.IO).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            try {


                val tracks = ArrayList<PlaylistTrack>(1)
                if (type == "genres") {
                    api.browse.getPlaylistsForCategoryRestAction(id!!, 10)
                        .supplier.invoke().items.forEach {
                            val play = it.toFullPlaylist()?.tracks?.forEach {
                                tracks.add(it!!)
                            }
                        }
                } else {
                    api.playlists.getPlaylistTracks(id!!).items.forEach {
                        tracks.add(it)
                    }
                }
                allTracks.postValue(tracks)
            }catch (e:java.lang.Exception){
                Log.d(TAG, "fetchTracks: exception")
            }
        }

    }

    fun getFetchedTracks(): MutableLiveData<List<PlaylistTrack>> {
        return allTracks
    }

    fun getPlaylistInfo(id: String?): MutableLiveData<Playlist> {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
                val playlist = api.playlists.getPlaylist(id!!)
                playlistInfo.postValue(playlist)
            }catch (e:Exception){
                Log.d(TAG, "getPlaylistInfo: exception")
            }
        }
        return playlistInfo
    }

    fun getGenres(): MutableLiveData<List<GenresModel>> {
        val allGenres = ArrayList<GenresModel>(1)
        CoroutineScope(Dispatchers.IO).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            try {

                api.browse.getCategoryList(20).items.forEach {
                    allGenres.add(GenresModel(it.id, it.name))
                }
                genres.postValue(allGenres)
            }catch (e:java.lang.Exception){
                Log.d(TAG, "getGenres: exception")
            }
        }
        return genres
    }

     fun searchSong(newText: String?): MutableLiveData<List<Track>?> {
        CoroutineScope(Dispatchers.Main).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            try {


                var thumb: Thumbnail
                var trackList = ArrayList<Track>(1)
                var thumbnails = ArrayList<Thumbnail>(1)
                api.search.searchTrack(newText!!, null, null, Market.IN).items.forEach {
//                thumb = Thumbnail(
//                    it.album.images.get(it.album.images.size - 1).url,
//                    it.name,
//                    it.type,
//                    it.id,
//                    searchedTracks,
//                    ""+it.artists.get(0).name+","+it.artists.get(it.artists.size-1).name
//                )
                    Log.d(TAG, "searchSong: in search result name is ${it.name} and id is ${it.id}")
//                thumbnails.add(thumb)
                    trackList.add(it)

                }

                searchedTracks.postValue(trackList)
            }catch (e:Exception){
                Log.d(TAG, "searchSong: exception")
            }
        }
        return searchedTracks
    }

//    fun searchAlbums(newText: String?): MutableLiveData<List<Thumbnail>?>{
//        CoroutineScope(Dispatchers.Main).launch {
//            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
//            var thumb: Thumbnail
//            var thumbnails = ArrayList<Thumbnail>(1)
//            api.search.searchAlbum(newText.toString()).items.forEach {
//                thumb= Thumbnail(it.images.get(0).url,it.name,it.type,it.id,searchedTracks,
//                    ""+it.artists.get(0).name+""+it.artists.get(it.artists.size-1).name)
//                thumbnails.add(thumb)
//            }
//            searchedTracks.postValue(thumbnails)
//        }
//        return searchedTracks
//    }
//    fun getSearches(): MutableLiveData<List<Thumbnail>?> {
//        return searchedTracks
//    }
//    fun searchArtists(newText: String?): MutableLiveData<List<Thumbnail>?>{
//        CoroutineScope(Dispatchers.Main).launch {
//            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
//            var thumb: Thumbnail
//            var thumbnails = ArrayList<Thumbnail>(1)
//            api.search.searchArtist(newText.toString()).items.forEach {
//                if(it.images.size>0) {
//
//                    thumb = Thumbnail(
//                        it.images.get(it.images.size - 1).url,
//                        it.name,
//                        it.type,
//                        it.id,
//                        searchedTracks,
//                        ""
//                    )
//                    thumbnails.add(thumb)
//                }
//            }
//            searchedTracks.postValue(thumbnails)
//        }
//        return searchedTracks
//    }
    fun searchPlaylist(newText: String?): MutableLiveData<List<Thumbnail>?>{
        CoroutineScope(Dispatchers.Main).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            var thumb: Thumbnail
            var thumbnails = ArrayList<Thumbnail>(1)
            try {


                api.search.searchPlaylist(newText.toString()).items.forEach {
                    thumb = Thumbnail(
                        it.images.get(it.images.size - 1).url,
                        it.name,
                        it.type,
                        it.id,
                        searchedPlaylist,
                        ""
                    )
                    thumbnails.add(thumb)
                }
                searchedPlaylist.postValue(thumbnails)
            }catch (e:Exception){
                Log.d(TAG, "searchPlaylist: exception")
            }
        }
        return searchedPlaylist
    }

    fun fetchNextSong(linkedTrackId: String?): MutableLiveData<Track> {
        CoroutineScope(Dispatchers.IO).launch {
            try {


                val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
                if (linkedTrackId != null) {
                    nextSong.postValue(api.tracks.getTrack(linkedTrackId)?.asTrack)
                }
            }catch (e:Exception){
                Log.d(TAG, "fetchNextSong: exception")
            }
        }
        return nextSong
    }

    fun fetchAlbumTracks(albumId: String): MutableLiveData<List<SimpleTrack>?> {
        CoroutineScope(Dispatchers.IO).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            val allTracks=ArrayList<SimpleTrack>(1)
            try {


                api.albums.getAlbum(albumId)?.tracks?.items?.forEach {
                    allTracks.add(it)
                }
                albumTracks.postValue(allTracks)
            }catch (e:Exception){
                Log.d(TAG, "fetchAlbumTracks: exception")
            }
        }
        return albumTracks
    }

    fun fetchAlbumInfo(albumId: String): MutableLiveData<Album?> {
        CoroutineScope(Dispatchers.IO).launch {
            try {


                val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
                albumInfo.postValue(api.albums.getAlbum(albumId))
            }catch (e:java.lang.Exception)
            {
                Log.d(TAG, "fetchAlbumInfo: exception")
            }
        }
        return albumInfo
    }

    fun addToFavourites(
        id: String?,
        userId: String?,
        currentPlaylistId: String?,
        applicationContext: Context
    ) {
        val favouriteSong=hashMapOf(
            "song" to id!!
        )
        SpotifyActivity.userFavouriteSongs.add(id)
        db.collection("Users").document(userId!!).collection("favourite_songs")
            .document(id!!).set(favouriteSong, SetOptions.merge()).addOnSuccessListener {
                Log.d(TAG, "addedToFavourites: ")
                val curpos=PlaylistFragment.allTracksInfos.indexOfFirst { it.id==id }
                if(curpos!=-1) {
                    SpotifyActivity.playlistTracks.get(curpos).isFavourite = false
                    PlaylistFragment.allTracksInfos.get(curpos).isFavourite = true
                }
                var intent=Intent()
                intent.setAction("updatePlaylist")
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
    }

    fun removeFromFavourites(
        id: String?,
        userId: String?,
        currentPlaylistId: String?,
        applicationContext: Context
    ) {
        db.collection("Users").document(userId!!).collection("favourite_songs").document(id!!).delete().addOnSuccessListener {
            Log.d(TAG, "removedFromFavourites: ")
            var curpos=SpotifyActivity.userFavouriteSongs.indexOf(id)
            if(curpos!=-1) {
                SpotifyActivity.userFavouriteSongs.removeAt(curpos)
            }
            val pos=PlaylistFragment.allTracksInfos.indexOfFirst { it.id==id }
            if(pos!=-1) {
                SpotifyActivity.playlistTracks.get(pos).isFavourite = false
                PlaylistFragment.allTracksInfos.get(pos).isFavourite = false
            }
            var intent=Intent()
            intent.setAction("updatePlaylist")
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
        }
    }
    fun getSongFavourites(userId: String?): MutableLiveData<List<String>?> {
        val songList=ArrayList<String>(0)
        db.collection("Users").document(userId!!).collection("favourite_songs").get().addOnSuccessListener {
            it.documents.forEach {
                songList.add(it.get("song").toString())
            }
            favouriteSongs.postValue(songList)
        }
        return favouriteSongs
    }

    fun getSong(songId: String): MutableLiveData<Track?> {
        Log.d(TAG, "getSong: insearchsong in repo $songId")
        CoroutineScope(Dispatchers.IO).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            val track=api.tracks.getTrack(songId)
            Log.d(TAG, "data repo getSong: ${track?.name} and id is ${track?.id}")
            getTrack.postValue(track)
        }
        return getTrack
    }

    fun getLikedSongs(userId: String, likedSongList: List<String>): MutableLiveData<List<Track?>?> {
        val tracks=ArrayList<Track?>(1)
        CoroutineScope(Dispatchers.IO).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            likedSongList.forEach {
                val track=api.tracks.getTrack(it)
                tracks.add(track)
            }
            likedTracks.postValue(tracks)
        }
        return likedTracks
    }
    fun getLikedSongsIds(userId: String): MutableLiveData<List<String>> {
        var tracksIds=ArrayList<String>(1)
        db.collection("Users").document(userId.toString()).collection("favourite_songs").get().addOnSuccessListener {
            it.documents.forEach {
                val songId = it.get("song").toString()
                tracksIds.add(songId)
            }
            likedSongsIds.postValue(tracksIds)
        }
        return likedSongsIds

    }



    fun saveLastPlayback(currentSong: TrackModel?, userId: String?) {

        db.collection("Users").document(userId.toString()).collection("last_playback").document("lastPlayback").set(
            currentSong!!,
            SetOptions.merge())
    }
    fun getLastPlayback(userId: String?): MutableLiveData<TrackModel> {
        var track:TrackModel?=null
        db.collection("Users").document(userId.toString()).collection("last_playback").document("lastPlayback")
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    track= TrackModel(it.result.get("id").toString()
                        ,it.result.get("image").toString()
                        ,it.result.get("name").toString()
                        ,it.result.get("artist").toString()
                        ,null
                        ,it.result.get("linkedTrackId").toString()
                        ,it.result.get("previewUrl").toString()
                        ,it.result.getBoolean("isFavourite"))
                    lastPlayback.postValue(track!!)
                }
            }
        return lastPlayback
    }

}


