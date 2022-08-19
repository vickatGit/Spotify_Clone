package com.example.spotify_clone.LocalDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SqlUserEntity::class], views = [], version = 2,exportSchema = false)
abstract class UserLoginSignUpDatabase : RoomDatabase() {
    abstract fun getUserLoginDao():UserLoginDao

    companion object{
        @Volatile
        var userLoginSignUpDatabase: UserLoginSignUpDatabase? =null
        @Synchronized
        fun getInstance(context: Context):UserLoginSignUpDatabase?{
            if(userLoginSignUpDatabase==null) {

                userLoginSignUpDatabase = Room.databaseBuilder(
                    context,
                    UserLoginSignUpDatabase::class.java,
                    "UserLoginSignUpDatabase.db"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
            }

            return userLoginSignUpDatabase
        }
    }


}