package com.example.spotify_clone.LocalDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserLoginDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertUser(user: SqlUserEntity)

  @Query("SELECT * FROM SqlUserEntity")
  fun getUser():SqlUserEntity
}