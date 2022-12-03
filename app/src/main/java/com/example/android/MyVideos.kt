package com.example.android

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import wseemann.media.FFmpegMediaMetadataRetriever
import java.io.File
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MyVideos(
    var folder: String,
    var myfolder: HashMap<String, String>,
    val context: Context,

)  :
    AsyncTask<Void,Void,Void>() {
    var lust:ArrayList<model>  = ArrayList<model>()
    val pu=myfolder.get(folder)+"/"
    val ful=File(pu)


    private fun converter1(duration: Int): String {
        val duration_formatted: String
        val sec = duration / 1000 % 60
        val min = duration / (1000 * 60) % 60
        val hrs = duration / (1000 * 60 * 60)

        if (hrs == 0) {
            duration_formatted =
                min.toString() + ":" + java.lang.String.format(Locale.UK, "%02d", sec)
        } else {
            duration_formatted =
                hrs.toString() + ":" + java.lang.String.format(Locale.UK, "%02d", min)+
                        ":" + java.lang.String.format(Locale.UK, "%02d", sec)
        }
        return duration_formatted
    }
    private fun converter(Size: Int): String{
        val kilo = 1024;
        val  mega = kilo * kilo;
        val giga = mega * kilo;
        val tera = giga * kilo;
        var s:String= ""
        var kb=Size/kilo
        var mb=kb/kilo
        var gb=mb/kilo
        var tb=gb/kilo
        if(Size < kilo) {
            s = Size.toString() + " B";
        } else if(Size >= kilo && Size < mega) {
            s =  String.format("%.2f", kb) + " KB";
        } else if(Size >= mega && Size < giga) {
            s = String.format("%.2f", mb) + " MB";
        } else if(Size >= giga && Size < tera) {
            s = String.format("%.2f", gb)+ " GB";
        } else if(Size >= tera) {
            s = String.format("%.2f", tb) + " TB";
        }
        var su=s.toDouble()
        var dec = DecimalFormat("#,###.00")
        var new=dec.format(su)
        s=new.toString()
        return s





    }


    override fun doInBackground(vararg params: Void?): Void? {
        var plo= folder
        var plop= myfolder?.get(plo)+"/"
        fetch(ful,myfolder,folder)




        return null
    }

    private fun fetch(ful: File, myfolder: HashMap<String, String>, folder: String){

        val fiu=ful.listFiles()
        fiu.forEach { f->
            if((f.name.endsWith(".mp4")||f.name.endsWith(".mkv"))){
                val mmr=FFmpegMediaMetadataRetriever()

                mmr.setDataSource(myfolder.get(folder)+"/"+f.name)
                var dur=mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION)
                var Size=mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_FILESIZE)
                var reso=mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
//                var si=converter(Size.toInt())
//                var duration=converter1(dur.toInt())

                var pak=model(f.name,myfolder.get(folder)+"/"+f.name,dur)

                lust.add(pak)

            }


        }
        Log.d("fuck","jdjscn "+lust.size)

    }


}




