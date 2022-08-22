package com.example.spotify_clone.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.adamratzman.spotify.endpoints.pub.TrackAttribute
import com.adamratzman.spotify.endpoints.pub.TuneableTrackAttribute
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.SpotifyCategory
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.utils.Locale
import com.adamratzman.spotify.utils.Market
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.Models.UserModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.days


class DataRepository {
    val db=FirebaseFirestore.getInstance()
    var user:UserModel?=null
    var isExist:Boolean=false
    var some: DocumentSnapshot? =null
    private var allLists:MutableLiveData<List<List<Thumbnail>>> = MutableLiveData()
//    private var allArtists:MutableLiveData<List<Artist>?> = MutableLiveData()
    private var commonLists=ArrayList<List<Thumbnail>>(1)
    private var allArtists:MutableLiveData<List<Thumbnail>?> = MutableLiveData()
    private var featuredPlaylists:MutableLiveData<List<Thumbnail>?> = MutableLiveData()
    private var chillPlaylist:MutableLiveData<List<Thumbnail>?> = MutableLiveData()
    private var recommendations:MutableLiveData<List<Thumbnail>?> = MutableLiveData()

    private var allCategories:MutableLiveData<List<SimplePlaylist>> = MutableLiveData()
    private var indianCategories:ArrayList<MutableLiveData<List<Thumbnail>?>> = ArrayList(7)


    companion object{

        private val CLIENT_ID="a7ade92373684af7b78d8382b1031827"
        private val CLIENT_SECRET="4b8c41c29cfd497298b910335d9599a1"
        private val TAG="tag"

        private var dataRepository= DataRepository()
        fun getInstance(): DataRepository {
            return dataRepository
        }
    }
    fun createUser(user: UserModel) {


        val docRef=db.collection("Users").document()
        user.setuserReference(docRef.toString())
        db.collection("Users").document().set(user).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("TAG", "createUser: Successful")
            }
        }.addOnFailureListener {
            Log.d("TAG", "createUser: failed")
        }
        isUserExist(user.email,user.password)
    }
    fun isUserExist(email: String, password: String) {
        db.collection("Users").whereEqualTo("email",email).limit(1).get().addOnCompleteListener {
            if(it.isSuccessful){
                it.result.documents.forEach {
                    some=it
                }
            }
        }
    }
    fun GetUser():UserModel?{
        if(some!=null) {
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
    fun isEmailAlreadyExist(email: String) {


        db.collection("Users").whereEqualTo("email",email).get(Source.SERVER).addOnCompleteListener {

            if(it.isSuccessful) {
                if (it.result.size() == 0) {
                    isExist = false
                }
            }
        }
        Log.d("TAG", "isEmailAlreadyExist:after $isExist")

    }
    fun GetIsEmailExists(): Boolean {
        return isExist
    }

    fun fetchArtists(){
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "fetchArtists: repo")
            val api=spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            var allData:MutableLiveData<List<Thumbnail>> = MutableLiveData()
            val thumbnails=ArrayList<Thumbnail>(1)
            var thumb:Thumbnail
            api.artists.getArtists("2CIMQHirSU0MQqyYHq0eOx","57dN52uHvrHOxijzpIgu3E","1vCWHaC5f2uS3yhpwWbIA6").forEach {
                val url=it?.images?.get(it.images.size-2)?.url
                thumb=Thumbnail(url.toString(),it?.name.toString(),it?.type.toString(),it?.id.toString(),allArtists,"Popular Artists")
                thumb.observer = allArtists
                thumbnails.add(thumb)
                Log.d(TAG, "fetchArtists: types " + it.type)
            }

//            commonLists.add(thumbnails)
            commonLists.add(thumbnails)
            Log.d(TAG, "fetch: length "+ commonLists.size)
            allLists.postValue(commonLists)
            allArtists.postValue(thumbnails)
        }

    }

    fun fetchedLists(): MutableLiveData<List<List<Thumbnail>>> {
        return allLists
    }
    fun fetchTopList(){
        CoroutineScope(Dispatchers.IO).launch {
            val api=spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            val categories=api.browse.getCategoryList(5,null,null,Market.IN)
            val gson= Gson()
            Log.d(TAG, "fetchTopList: "+api.token)

            val playCategories:ArrayList<SpotifyCategory> = ArrayList()
            val obj=api.browse.getPlaylistsForCategory(categories.get(0).id,6).items

            allCategories.postValue(obj)
        }
    }

    fun getAllCatgories(): MutableLiveData<List<SimplePlaylist>> {
        return allCategories
    }
    fun getFeaturedPlaylists(){
        CoroutineScope(Dispatchers.IO).launch {
            val api=spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            var thumb:Thumbnail
            var thumbnails=ArrayList<Thumbnail>(1)

            api.browse.getFeaturedPlaylists(10,null,null,Market.US).playlists.items.forEach {
                thumb=Thumbnail(it.images.get(it.images.size-1).url, it.name, it.type, it.id, featuredPlaylists, "Featured Playlist")
                thumbnails.add(thumb)
                Log.d(TAG, "getFeaturedPlaylists: types"+it.type)
            }
            commonLists.add(thumbnails)
            Log.d(TAG, "getFeaturedPlaylists: length "+ commonLists.size)
            allLists.postValue(commonLists)
            featuredPlaylists.postValue(thumbnails)
        }
    }
    fun getChillPlaylist(){
        CoroutineScope(Dispatchers.IO).launch {
            val api=spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            var thumb:Thumbnail
            var thumbnails=ArrayList<Thumbnail>(1)
            api.browse.getPlaylistsForCategoryRestAction("chill",10,null,Market.IN).supplier.invoke().items.forEach {
                thumb= Thumbnail(it.images.get(it.images.size-1).url,it.name,it.type,it.id,chillPlaylist,"Chill Playlists")
                Log.d(TAG, "getChillPlaylist: $thumb")
                thumbnails.add(thumb)
                Log.d(TAG, "getChillPlaylist: types "+it.type)
            }
            commonLists.add(thumbnails)
            allLists.postValue(commonLists)
            chillPlaylist.postValue(thumbnails)
        }
    }

    fun getRecommendations(){
        CoroutineScope(Dispatchers.IO).launch {
            val api=spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            var thumb:Thumbnail
            var thumbnails=ArrayList<Thumbnail>(1)
            api.browse.getNewReleases(10,null,Market.IN).items.forEach {
                thumb=Thumbnail(it.images.get(it.images.size-2).url,it.name.toString(),it.type,it.id,recommendations,"Recommanded for you")
                thumbnails.add(thumb)
                Log.d(TAG, "getRecommendations: types "+it.type)
            }
            commonLists.add(thumbnails)
            allLists.postValue(commonLists)
            recommendations.postValue(thumbnails)

        }
    }
    fun getIndianCategoriesPlaylists(){
        CoroutineScope(Dispatchers.IO).launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()
            api.browse.getCategoryList(5).items.forEach {
                val name=it.name
                var thumb: Thumbnail
                var thumbnails = ArrayList<Thumbnail>(1)
                var curpos=0
                api.browse.getPlaylistsForCategory(it.id,7).items.forEach {
                    indianCategories.add(curpos,MutableLiveData())
                    thumb= Thumbnail(it.images.get(0).url,it.name,it.type,it.id,indianCategories.get(curpos),name)
                    thumbnails.add(thumb)
                    Log.d(TAG, "getIndianCategoriesPlaylists: types "+it.type)
                }
                commonLists.add(thumbnails)
                allLists.postValue(commonLists)
                indianCategories.get(curpos).postValue(thumbnails)
                curpos++
            }
        }
    }
//    fun initialiseIndianCategoriesLiveData(){
//        indianCategories[0]
//    }
}
//0JQ5DAqbMKFHCxg5H5PtqW
//BQCIPfMoNAoxM9ahxRWQ38WBA6zsbfGctJF7BcybCgVNIczPdSofweht9D_2vivHddDV2_SgeVDcug3HbW4dbLeCbUJKrRPf3SgpzO6x0ndS70Rawr0