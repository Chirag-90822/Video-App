package com.example.android


import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList
import java.io.File
import kotlin.collections.HashMap
import android.media.*
import android.os.*
import android.view.Menu
import android.view.MenuItem
import android.view.View


import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import java.util.*
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import android.media.MediaScannerConnection
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.testing.VideoDatabase
import com.example.testing.VideoFile
import se.sawano.java.text.AlphanumericComparator
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.*
import java.lang.Runnable


class FilesActivity : AppCompatActivity(),SearchView.OnQueryTextListener, View.OnLongClickListener {


    var renamed2:Boolean=false
    var isdestoryed=true
    var enablenew=false
    var disablenew=false
    var anoth:MutableList<model> = mutableListOf()
    var anoth2= mutableListOf<model>()
    var anoth3= mutableListOf<model>()

    var newlist=ArrayList<String>()

    private val mHandler: Handler = Handler()
    var done:Int=0
    var is_sellected = false
    var selectionArrayList: ArrayList<model> = ArrayList<model>()
    var count = 0
    var mediafiles = ArrayList<model>()
    var searched = false
     var boisd:ArrayList<model>?=null
    var uris = ArrayList<Uri>()
    var mapofuri:ConcurrentHashMap<String,Uri>?=null
//    lateinit var loppp:Task
    lateinit var videoDataBase:VideoDatabase

    var actioncsll: androidx.appcompat.view.ActionMode? = null
    var infl = true
    lateinit var poi:Giveuri


    lateinit var searchView : SearchView


    lateinit var mtoast: Runnable
    lateinit var foldername: String
    lateinit var givemyarray: MutableList<model>
    lateinit var foldertitle: TextView
    lateinit var back2: ImageView
    lateinit var myfolder: java.util.HashMap<String, String>

    lateinit var toolbar: Toolbar
    var allarray = ArrayList<String>()

    var alreadynewlist=ArrayList<String>()
    companion object{
     var watching:ArrayList<File> = ArrayList()
            lateinit var recyclerView:RecyclerView
            lateinit var give: fileview
        var disablenwlist=ArrayList<String>()



}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.files)




        foldername = intent.getStringExtra("foldername").toString()
        myfolder =
            intent.getSerializableExtra("folderpath") as HashMap<String, String>
        var count = intent.getStringExtra("co")?.toInt()
        toolbar = findViewById(R.id.file_title)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)



        recyclerView = findViewById<RecyclerView>(R.id.recycle2)
        back2 = findViewById(R.id.back2)
        foldertitle = findViewById(R.id.folder_title)
        foldertitle.setText(foldername)
        videoDataBase= VideoDatabase.getDatabase(applicationContext)
        GlobalScope.launch {
            if(videoDataBase.fileDao().iscount().size>0){
                Log.d("checkingboi","dpdov ")
                var mpl=videoDataBase.fileDao().alreadynew(1,foldername)
                for(cv in mpl){
                    alreadynewlist.add(cv.name)
                }
                Log.d("checkingboi","dpdov "+ alreadynewlist.size)
            }
        }

        val allfile = File(myfolder.get(foldername) + "/")
        val allvideo = allfile.listFiles()
        allarray = ArrayList<String>()
        allvideo.forEach { f ->
            if ((f.name.endsWith(".mp4") || f.name.endsWith(".mkv"))) {
                allarray.add(f.name)
            }
        }
        back2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onBackPressed()
            }

        })
        for(apk in myfolder){
            watching.add(File(apk.value))
        }







        CoroutineScope(Dispatchers.IO).launch {
            printfiles()
        }


        give = fileview(this@FilesActivity, anoth)

        recyclerView.adapter = give

        recyclerView.layoutManager =
            LinearLayoutManager(
                this@FilesActivity,
                RecyclerView.VERTICAL,
                false)
        runOnUiThread(object  :Runnable{
            override fun run() {
                Handler().postDelayed(object :Runnable{
                    override fun run() {
                        give.showshimmer=false
                        give.notifyDataSetChanged()

                    }

                },1000)
            }

        })




    }

    private suspend fun printfiles() {
        val job= CoroutineScope(Dispatchers.IO).async {
            listing(allarray)
        }.await()


        videoDataBase= VideoDatabase.getDatabase(applicationContext)


        givemyarray = job
        filldatabase(job)

        Log.d("popaye","posps "+givemyarray.size)
        searched = true


        Collections.sort(anoth,AlphanumericComparator())









    }

    private suspend fun listing(allarray: ArrayList<String>):MutableList<model> {

        allarray.forEach { f ->
            val lpo = MediaMetadataRetriever()
            try {
                lpo.setDataSource(myfolder.get(foldername) + "/" + f)
                val op = lpo.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

                var durat = converter1(op!!.toInt())
                val mud = model(f, myfolder.get(foldername) + "/" + f, durat)
                val mud1 = model(f, myfolder.get(foldername) + "/" + f, op)
                anoth.add(mud)
                anoth2.add(mud1)
            }
            catch (e:Exception){
                val mud = model(f, myfolder.get(foldername) + "/" + f, "0:00")
                val mud1 = model(f, myfolder.get(foldername) + "/" + f, "000")
                anoth.add(mud)
                anoth2.add(mud1)
            }


        }

        return anoth


    }

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
    private suspend fun filldatabase(givemearray: MutableList<model>) {
        Log.d("lalal","oppdd "+videoDataBase.fileDao().iscount().size+" "+videoDataBase.fileDao().isthere(foldername)+" "+givemearray.size)
            if ((videoDataBase.fileDao().iscount().size<=0 || videoDataBase.fileDao().isthere(foldername)==false)) {

                Log.d("checkingboi", "2 ")

                for (i in anoth) {
                    var identify = File(i.path).lastModified()
                    Log.d("checkingboi", "3 ")
                    videoDataBase.fileDao().insertvideo(
                        VideoFile(
                            (0.toLong()),
                            i.title!!, (0.toLong()), i.path!!, 0, foldername, identify
                        )
                    )
                }

            } else {
                Log.d("checkingboi", "4 ")

                for (i in anoth) {
                    var identify = File(i.path).lastModified()
                    Log.d("checkingboi", "5 ")
                    if(videoDataBase.fileDao().isthere35(i.title!!,foldername)==false){
                    if ((videoDataBase.fileDao().realident(identify) == true) || videoDataBase.fileDao().showme2(identify, foldername)) {

                        Log.d("checkingboi", "6")
                        var checking2 = videoDataBase.fileDao().showme(identify, foldername)
                        if (checking2.size > 0) {
                            Log.d("checkingboi", "8")

                            for (c in checking2) {
                                videoDataBase.fileDao().rename(i.title, i.path!!, c.id)

                            }
                        } else {

                            var datchecking = videoDataBase.fileDao().isnewonethere(1, i.title)
                            if (datchecking.size > 0) {
                                Log.d("checkingboi", "9")

                                for (g in datchecking) {

                                    videoDataBase.fileDao().insertvideo(
                                        VideoFile(
                                            (0.toLong()),
                                            i.title,
                                            (0.toLong()),
                                            i.path!!,
                                            1,
                                            foldername,
                                            identify
                                        )
                                    )
                                    newlist.add(i.title)
                                    runOnUiThread(object : Runnable {
                                        override fun run() {
                                            recyclerView.post {
                                                Log.d("chiragboi", "recyvl")
                                                enablenew = true
                                                give.notifyDataSetChanged()
                                            }
                                        }

                                    })
                                }
                            } else {
                                Log.d("checkingboi", "10")
                                videoDataBase.fileDao().insertvideo(
                                    VideoFile(
                                        (0.toLong()),
                                        i.title,
                                        (0.toLong()),
                                        i.path!!,
                                        0,
                                        foldername,
                                        identify
                                    )
                                )
                            }

                        }

                    } else if (videoDataBase.fileDao().isthere35(i.title, foldername) == false) {
                        Log.d("checkingboi", "7 ")
                        newlist.add(i.title)
                        var identify = File(i.path).lastModified()
                        videoDataBase.fileDao().insertvideo(
                            VideoFile(
                                (0.toLong()),
                                i.title!!,
                                (0.toLong()),
                                i.path!!,
                                1,
                                foldername,
                                identify
                            )
                        )
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                recyclerView.post {
                                    Log.d("chiragboi", "recyvl")
                                    enablenew = true
                                    give.notifyDataSetChanged()
                                }
                            }

                        })
                    }


                }
            }

                var dataop = videoDataBase.fileDao().getmelist()
                for (ity in dataop) {
                    if (allarray.contains(ity.name) == false && ity.parent == foldername) {
                        videoDataBase.fileDao().deleteme(ity.name, foldername)
                    }
                }


            }




    }


//    class Task(
//    val filesActivity: FilesActivity,
//   val allarray: ArrayList<String>,
//    val myfolder: java.util.HashMap<String, String>,
//    val foldername: String
//) : AsyncTask<Void, Void, Void>() {
//
//
//    var anoth:MutableList<model> = mutableListOf()
//        var anoth2= mutableListOf<model>()
//
//
//
//    override fun doInBackground(vararg params: Void?): Void? {
//
//            allarray.forEach { f ->
//                val lpo = MediaMetadataRetriever()
//                lpo.setDataSource(myfolder.get(foldername) + "/" + f)
//                val op = lpo.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
//
//                var durat = converter1(op!!.toInt())
//                val mud = model(f, myfolder.get(foldername) + "/" + f, durat)
//                val mud1 = model(f, myfolder.get(foldername) + "/" + f, op)
//                anoth.add((mud))
//                anoth2.add(mud1)
//
//            }
//
//
//
//
//        filesActivity.videoDataBase= VideoDatabase.getDatabase(filesActivity.applicationContext)
//        filesActivity.filldatabase(anoth2)
//
//
//        return null
//    }
//
//        override fun onPostExecute(result: Void?) {
//            super.onPostExecute(result)
//            Collections.sort(anoth,AlphanumericComparator())
//
//        }
//
//    private fun converter1(duration: Int): String {
//        val duration_formatted: String
//        val sec = duration / 1000 % 60
//        val min = duration / (1000 * 60) % 60
//        val hrs = duration / (1000 * 60 * 60)
//
//        if (hrs == 0) {
//            duration_formatted =
//                min.toString() + ":" + java.lang.String.format(Locale.UK, "%02d", sec)
//        } else {
//            duration_formatted =
//                hrs.toString() + ":" + java.lang.String.format(Locale.UK, "%02d", min)+
//                        ":" + java.lang.String.format(Locale.UK, "%02d", sec)
//        }
//        return duration_formatted
//    }
//}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_file,menu)

        var menuitem=menu!!.findItem(R.id.search_title)
        var searchView:SearchView= menuitem.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        menuitem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {


                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

       return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

          give.filter.filter(newText)
        searched=true

        return false

    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId==R.id.search_title){
            var menuitem=toolbar.menu!!.findItem(R.id.search_title)
            var searchView:SearchView= menuitem.actionView as SearchView
            searchView.setOnQueryTextListener(this)

        }
        return super.onOptionsItemSelected(item)
    }


  var actionboi= object : ActionMode.Callback{
      override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {

          give.mainViewModel.setText("0 items Selected")
         mode!!.menuInflater.inflate(R.menu.items_selected,menu)


          return true
      }

      override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {


          give.mainViewModel.text
              .observe(this@FilesActivity,
                  Observer<String?> { s ->

                      mode!!.setTitle(s)
                  })


          return true
      }

      override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
         when(item!!.itemId){
             R.id.delete_selected->{
                 give.candelete=true
                 var pressok=false

                 var dialog=AlertDialog.Builder(this@FilesActivity)
                     .setTitle("Delete")
                     .setMessage("Selected items will be deleted")
                     .setPositiveButton("OK",object : DialogInterface.OnClickListener{
                         override fun onClick(dialog: DialogInterface?, which: Int) {
                             selectedFile(give.selectlist,give.candelete)
                             give.notifyme(give.selectlist)
                             pressok=false

                               done=give.selectlist.size
                             callingcount(done)
                             is_sellected=false
                             give.notifyDataSetChanged()
                             mode!!.finish()
                         }

                     })
                     .setNegativeButton("Cancel",object : DialogInterface.OnClickListener{
                         override fun onClick(dialog: DialogInterface?, which: Int) {
                             dialog!!.dismiss()
                         }

                     }).show()






             }
             R.id.share_selected->{
                 give.candelete=false
                 selectedFile(give.selectlist,give.candelete)
                 give.selectlist.clear()
                 give.listdelete.clear()
                 is_sellected=false
                 give.notifyDataSetChanged()

                 mode!!.finish()

             }
         }
          return true
      }

      override fun onDestroyActionMode(mode: ActionMode?) {
          give.toenable=false
          give.selectlist.clear()
          give.listdelete.clear()
          is_sellected=false
          give.notifyDataSetChanged()

         actioncsll=null
      }

  }

    private fun callingcount(done: Int) {
        var boi=MainActivity()
        boi.updatecount(foldername,done)


    }



    override fun onLongClick(v: View?): Boolean {
        if(actioncsll==null){

            actioncsll=startSupportActionMode(actionboi)
         is_sellected=true

        }
        give.notifyDataSetChanged()
        poi= Giveuri(givemyarray,this,count!!.toInt())
        poi.execute()


       return true
    }
    fun selectedFile(selectlist: ArrayList<model>, candelete: Boolean) {



        for(jp in selectlist){
            var pat=jp.path


            if(candelete){
                var filedele=File(pat)
                var isdelete=filedele.delete()

                if(isdelete){

                    Toast.makeText(this,"Deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
        if(candelete){
            var itr=give.backup.iterator()

            while(itr.hasNext()){
                var phone=itr.next()

                if( selectlist.contains(phone)){
                    itr.remove()


                }
            }
            var datpo=ArrayList<String>()
            for( i in selectlist){
                datpo.add(i.title!!)
            }

            GlobalScope.launch {


                for( k in datpo) {
                    var fordatabase34 = k
                    val anotherquery = videoDataBase.fileDao().deleteme(fordatabase34,foldername)

                }



            }
        }
        if(candelete==false){
            uris.clear()
            mapofuri=poi.mapofuri
            if(renamed2){
                renamed2=false
                renamed3(give.renamesuper,give.renamepath,give.hlloris,give.onerenamed)
            }


            for(jo in selectlist){
           if(mapofuri!!.containsKey(jo.title)){
               uris.add(mapofuri!!.get(jo.title)!!)
           }

            }
             MediaScannerConnection.scanFile(this, arrayOf(uris.toString()),null,object : MediaScannerConnection.OnScanCompletedListener{
                 override fun onScanCompleted(path: String?, uri: Uri?) {
                     val intent = Intent(android.content.Intent.ACTION_SEND_MULTIPLE)
                     intent.type = "video/*"
                     var ilop=intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris)
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);



                     startActivity(intent)
                 }

             })



        }


    }

    fun renamed3(isrenamed: String, isrenamaedpath: String, newone: model, onerenamed: String){
        MediaScannerConnection.scanFile(this, arrayOf(isrenamaedpath),null, object : MediaScannerConnection.OnScanCompletedListener{
            override fun onScanCompleted(path: String?, uri: Uri?) {
                mapofuri!!.set(onerenamed,uri!!)
            }

        })



    }

    fun prepareselection(v: View?, position: Int) {
        if((v as CheckBox).isChecked() ){
         give.selectlist.add(givemyarray.get(position))
            give.listdelete.add(position)
            Log.d("kiran","polo "+givemyarray.size+" "+givemyarray.get(position).title)

        }else{
            give.selectlist.remove(givemyarray.get(position))
            Log.d("kiran","polo "+givemyarray.size+" "+givemyarray.get(position).title)
            give.listdelete.remove(position)
        }
        give.mainViewModel.setText(give.selectlist.size.toString()+" items Selected")
    }

    fun changenew(newchanging:String){

        disablenwlist.add(newchanging)
        Log.d("llalit","comingin ")
        recyclerView.post {
            Log.d("llalit","goingtorec ")
            give.notifyDataSetChanged()
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        alreadynewlist.clear()
        disablenwlist.clear()
        Log.d("checkingboi","spdpdpp "+ alreadynewlist.size)
    }





}







