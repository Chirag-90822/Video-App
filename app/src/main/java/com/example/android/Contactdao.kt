package com.example.testing

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface Contactdao {
    @Insert
     fun insertvideo(file:VideoFile)

    @Update
    fun updatevideo(file:VideoFile)

    @Delete
     fun deletevieo(file:VideoFile)

    @Query("SELECT * FROM videolist")
     fun getContact() : LiveData<List<VideoFile>>

    @Query("SELECT * FROM videolist")
    fun getmelist() : List<VideoFile>

    @Query("SELECT EXISTS(SELECT * FROM videolist WHERE parent = :arg0)")
    fun isthere(arg0:String):Boolean

    @Query("SELECT EXISTS(SELECT * FROM videolist WHERE name = :name4)")
    fun isthere34(name4:String) : Boolean

    @Query("SELECT EXISTS(SELECT * FROM videolist WHERE name = :name60 AND parent= :popaye)")
    fun isthere35(name60:String,popaye:String) :Boolean

    @Query("SELECT * FROM videolist WHERE name = :name5")
    fun kop(name5:String): List<VideoFile>

    @Query("SELECT * FROM videolist WHERE name = :name5 AND parent= :name11")
    fun kop2(name5:String,name11:String): List<VideoFile>





    @Query("SELECT * FROM videolist")
    fun iscount() : List<VideoFile>

    @Query("UPDATE videolist SET duration = :durme , newone= :Inactive WHERE id = :giveid")
    fun updatedur(durme:Long,giveid:Long,Inactive:Int) : Int

    @Query("UPDATE videolist SET name = :name7 , path= :path4  WHERE id = :giveid")
    fun rename(name7:String,path4:String,giveid:Long) : Int

    @Query("DELETE FROM videolist WHERE name= :name10 AND parent= :necess")
    fun deleteme(name10:String,necess:String) : Int

    @Query("SELECT * FROM videolist WHERE identifier= :ident AND parent= :rightparent")
    fun showme(ident:Long,rightparent:String):List<VideoFile>



    @Query("SELECT EXISTS (SELECT * FROM videolist WHERE identifier= :ident AND parent= :rightparent)")
    fun showme2(ident:Long,rightparent:String): Boolean

    @Query("SELECT EXISTS (SELECT * FROM videolist WHERE identifier= :identifyme)")
    fun realident(identifyme:Long):Boolean

    @Query("SELECT * FROM videolist WHERE newone = :right AND parent= :parentname")
    fun alreadynew(right:Int,parentname:String):List<VideoFile>


    @Query("SELECT * FROM videolist WHERE newone = :right1 AND name= :name20")
    fun isnewonethere(right1:Int,name20:String):List<VideoFile>

}