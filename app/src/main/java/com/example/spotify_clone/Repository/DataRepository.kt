package com.example.spotify_clone.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.SpotifyCategory
import com.adamratzman.spotify.spotifyAppApi
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

    private var allCategories:MutableLiveData<List<SimplePlaylist>> = MutableLiveData()


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
                thumb=Thumbnail(url.toString(),it?.name.toString(),it?.type.toString(),it?.id.toString(),allArtists)
                thumb.observer = allArtists
                thumbnails.add(thumb)
            }

            commonLists.add(thumbnails)
            commonLists.add(thumbnails)
            Log.d(TAG, "getFeaturedPlaylists: length "+ commonLists.size)
            allLists.postValue(commonLists)
            allArtists.postValue(thumbnails)
        }

    }
//    fun fetchedArists(): MutableLiveData<List<Thumbnail>?> {
//        return allArtists
//    }
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
            val obj=api.browse.getNewReleasesRestAction(1,null,Market.IN)
            api.browse.getFeaturedPlaylists(10).playlists.items.forEach {
                thumb=Thumbnail(it.images.get(it.images.size-1).url,it.name,it.type,it.id,featuredPlaylists)
                thumbnails.add(thumb)
            }
            commonLists.add(thumbnails)
            Log.d(TAG, "getFeaturedPlaylists: length "+ commonLists.size)
            allLists.postValue(commonLists)
            featuredPlaylists.postValue(thumbnails)
        }
    }
}