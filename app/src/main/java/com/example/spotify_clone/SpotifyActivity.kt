package com.example.spotify_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.adamratzman.spotify.*
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.Token
import com.example.spotify_clone.Fragment.HomeFragment
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SpotifyActivity : AppCompatActivity() {

    companion object{
        private val CLIENT_ID="a7ade92373684af7b78d8382b1031827"
        private val ACCESS_TOKEN="BQBkraliWNUThl6HuwRbePsvbZWrAWvPyq18Nhd7UoM2CUY_FJaNLl0j2cJ55IVg_sXtfssbbDUKkcTBv95_g2K12sHk7pLce7pw3SkeA7PC42_wdszj1y7VkntJ835TW44U-sia6zDUU_VZHkSFMtC5q7J0kRejK4CbQl1nABy_y6dqufibKZHhnLbYJ8XGzN_aACcUjzaKzGAcGQP7waH2FGhDtX8tNVWaKmVgP_BUBSyOcQDt8w"
        private val CLIENT_SECRET="4b8c41c29cfd497298b910335d9599a1"
        private val REFRESH_TOKEN="AQCV3rYxddoBzMDE2oTSKO7NxQkijok39BXC1V70T6r9wmNE1dluASfflJaouK6DYOS4dSybt8oV9Hot24ewGhYT2R0tBKI5Y1lYdcpT_chJUl8RpUXEbxvnjQJjfroFjio"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)

        val homeFragment=HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.spotify_frame,homeFragment).commit()




    }
}