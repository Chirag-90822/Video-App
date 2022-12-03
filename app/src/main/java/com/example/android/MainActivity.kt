package com.example.android

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.util.HashMap

class MainActivity : AppCompatActivity() {
    var myfolder= hashMapOf<String,String>()
    var mycount : HashMap<String,Int> = HashMap<String,Int>()
    var folderarray:ArrayList<String> = fetch(Environment.getExternalStorageDirectory())
    companion object{
        lateinit var giving:Folder
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dexter.withContext(this.applicationContext)
            .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object:PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                   folderarray= fetch(Environment.getExternalStorageDirectory())


                    giving=Folder(myfolder, mycount,
                        folderarray , applicationContext)
                    var recyclerview=findViewById<RecyclerView>(R.id.recycle)
                    recyclerview.adapter=giving
                    recyclerview.layoutManager= LinearLayoutManager(applicationContext,RecyclerView.VERTICAL,false)


                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {


                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    if (p1 != null) {
                        p1.continuePermissionRequest()

                    }
                }

            }).check()





    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }


    fun fetch(file:File):ArrayList<String> {
        val all = arrayListOf<String>()

        val video = file.listFiles()


        val s = file.parentFile.name
        if (video != null){

            video.forEach { f ->
                if (!f.isHidden && f.isDirectory && s != "Android") {
                    all.addAll(fetch(f))
                } else {


                    if ((f.name.endsWith(".mp4") || f.name.endsWith(".mkv"))) {
                        val pname = f.parent
                        val pname1 = pname.lastIndexOf("/")
                        val pname2 = pname.substring(pname1 + 1)



                        if (all.size > 0 && !all.contains(pname2)) {
                            myfolder.put(pname2, f.parentFile.toString())
                            mycount.put(pname2, 1)
                            all.add(pname2)
                        } else if (all.size == 0) {
                            all.add(pname2)
                            myfolder.put(pname2, f.parentFile.toString())
                            mycount.put(pname2, 1)
                        } else if (all.size > 0 && all.contains(pname2)) {
                            mycount.get(pname2)?.plus(1)?.let { mycount.put(pname2, it) }
                        }

                    }
                }
            }
    }else{

        }

        return all

    }

    override fun onStart() {
        super.onStart()
        folderarray=fetch(Environment.getExternalStorageDirectory())
    }

    override fun onRestart() {
        super.onRestart()
        folderarray=fetch(Environment.getExternalStorageDirectory())
    }
    fun updatecount(nameof:String,countmein:Int){
        mycount.set(nameof,mycount.get(nameof)!!.minus(countmein))
        giving.notifyDataSetChanged()
        Log.d("updated ","dpdp "+mycount)

    }


}