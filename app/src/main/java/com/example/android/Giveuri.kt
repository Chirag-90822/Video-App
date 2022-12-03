package com.example.android

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import android.os.Handler
import java.io.File
import java.util.concurrent.ConcurrentHashMap


class Giveuri(var givemyarray:MutableList<model>, val context: Context?, var count:Int):AsyncTask<Void,Void,Void>(){
    constructor():this(emptyList<model>() as MutableList<model>,null,0){

    }
    private val m2Handler: Handler = Handler()
    var mapofuri= ConcurrentHashMap<String,Uri>()
    companion object{
        var mapi= ConcurrentHashMap<String,Uri>()
    }


    override fun doInBackground(vararg params: Void?): Void? {
        if(givemyarray.size>=count) {
            for (jk in givemyarray) {


                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(jk.path),
                    null,
                    object : MediaScannerConnection.OnScanCompletedListener {
                        override fun onScanCompleted(path: String?, uri: Uri?) {

                            var fool = File(path)
                            if (mapofuri.isEmpty() || mapofuri.containsKey(fool.name) == false) {

                                mapofuri.put(fool.name, uri!!)
                            }
                        }

                    }
                )
            }
          mapi=mapofuri
        }
        return null
    }


}