package com.example.android

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import java.io.File
import java.net.URLDecoder


class MainActivity2 : AppCompatActivity() {
    lateinit var simplexo:ExoPlayer
    lateinit var comingfile:File
    lateinit var playerview:PlayerView
     var position=0
     var ok:ArrayList<String> = ArrayList()
    lateinit var namearray:ArrayList<String>
    lateinit var concatenate:ConcatenatingMediaSource
     var popy:Uri?=null
    lateinit var goku:File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        when{
            intent?.action==Intent.ACTION_VIEW->{


            }
        }



          var comei=intent.data.toString()
        var seck=comei.lastIndexOf("/")
        var iop=comei.substring(seck+1)


        Log.d("dhahi","sppw "+URLDecoder.decode(iop))

        playerview=findViewById(R.id.exoplayer2)
        simplexo=ExoPlayer.Builder(this).build()
        playerview.player=simplexo
        var mediaone=MediaItem.fromUri(intent.data!!)
        simplexo.addMediaItem(mediaone)
        simplexo.prepare()
        simplexo.play()









    }






}