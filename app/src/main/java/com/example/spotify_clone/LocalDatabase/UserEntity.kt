package com.example.spotify_clone.LocalDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SqlUserEntity {
    @PrimaryKey
    var id:Int=1
    lateinit var username:String
    lateinit var userReference:String

    constructor(id: Int?, username: String, dataId:String) {
        if (id != null) {
            this.id = id
        }
        this.username = username
        this.userReference=dataId
    }
    constructor()

    override fun toString(): String {
        return "SqlUserEntity(id=$id, username='$username', dataId='$userReference')"
    }


}