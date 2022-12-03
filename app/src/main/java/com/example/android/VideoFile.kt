package com.example.testing

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity (tableName = "videolist")
data class VideoFile(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val name:String,
    val duration:Long,
    val path:String,
    val newone:Int,
    val parent:String,
    val identifier:Long
)
