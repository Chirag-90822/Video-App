package com.example.testing

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [VideoFile::class], version = 2)
abstract class VideoDatabase : RoomDatabase() {


    abstract fun fileDao(): Contactdao

    companion object{
        @Volatile
        private var INSTANCE:VideoDatabase?=null

        fun getDatabase(context: Context):VideoDatabase{
            if(INSTANCE==null){
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        VideoDatabase::class.java, "videoDB"
                    ).fallbackToDestructiveMigration().build()

                }
            }
            return INSTANCE!!
        }
    }

}