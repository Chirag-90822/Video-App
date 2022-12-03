package com.example.android

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog

import android.content.DialogInterface
import android.media.MediaScannerConnection
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import java.io.File
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import wseemann.media.FFmpegMediaMetadataRetriever
import androidx.fragment.app.FragmentActivity
import android.widget.Toast
import android.util.SparseBooleanArray
import androidx.core.view.isVisible
import com.example.testing.VideoDatabase
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import se.sawano.java.text.AlphanumericComparator
import java.util.*
import kotlin.collections.ArrayList


class fileview(val context: Context?, var lop: MutableList<model>) : RecyclerView.Adapter<fileview.MYv>(),Filterable{
 var videoactivity: FilesActivity? = context as? FilesActivity
     lateinit var mainViewModel: MainViewModel
     var selectlist=ArrayList<model>()
    var isenable=false
        var showshimmer=true


    var toenable=false
    var candelete=false
    var renamedone=false
    var renamearray:ArrayList<model> = ArrayList()
    var deletearray:ArrayList<model> =ArrayList()
    var deletedone=false
    lateinit var renamesuper:String
    lateinit var renamepath:String
    lateinit var onerenamed:String


    var listdelete=ArrayList<Int>()
    private val itemStateArray = SparseBooleanArray()

    var backup=ArrayList<model>()
    var c=1
    lateinit var hlloris:model

   constructor():this(null, ArrayList()){

   }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MYv {

        val view= LayoutInflater.from(parent.context).inflate(R.layout.myfiles,parent,false)
        mainViewModel =  ViewModelProvider((context as FragmentActivity?)!!).get(MainViewModel::class.java)
        return MYv(view, videoactivity!!)
    }




    override fun onBindViewHolder(holder: MYv, @SuppressLint("RecyclerView") position: Int) {

        if (showshimmer) {
            holder.shimmerframe.startShimmer()
        } else {
            holder.shimmerframe.stopShimmer()
            holder.shimmerframe.setShimmer(null)
            Log.d("shimmer","dodkod ")

            Glide.with(context!!).load(lop[position].path).into(holder.image)
            holder.title.setText(lop[position].title)
            holder.title.background = null
            holder.more.background=null
            holder.duration.setText(lop[position].duration)
            holder.duration.visibility=View.VISIBLE




        if (videoactivity!!.enablenew == true) {
            if (videoactivity!!.newlist.contains(holder.title.text)) {
                holder.newly.visibility = View.VISIBLE
                Log.d("llalit", "enabled")
            } else {
                holder.newly.visibility = View.GONE
            }
        }

        if (videoactivity!!.alreadynewlist.size > 0) {
            if (videoactivity!!.alreadynewlist.contains(holder.title.text)) {
                if (holder.newly.isVisible == false) {
                    holder.newly.visibility = View.VISIBLE
                }


                Log.d("llalit", "enabled7" + videoactivity!!.alreadynewlist)
            } else {
                holder.newly.visibility = View.GONE
            }
        }
        if (videoactivity!!.disablenew == false) {
            if (FilesActivity.disablenwlist.contains(holder.title.text)) {
                holder.newly.visibility = View.GONE
                Log.d("llalit", "rec ")
            }
        }


        holder.more.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var bottomsheeetdialog = BottomSheetDialog(context!!, R.style.bottomsheetdialog)
                var bottomsheetview = LayoutInflater.from(context).inflate(R.layout.file_menu, null)

                bottomsheetview.findViewById<ImageView>(R.id.down_arrow)
                    .setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            bottomsheeetdialog.dismiss()
                        }


                    })
                bottomsheetview.findViewById<LinearLayout>(R.id.menu_share)
                    .setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {

                            MediaScannerConnection.scanFile(
                                context,
                                arrayOf(lop.get(position).path.toString()),
                                null,
                                object : MediaScannerConnection.OnScanCompletedListener {
                                    override fun onScanCompleted(path: String?, uri: Uri?) {

                                        val intent = Intent(android.content.Intent.ACTION_SEND)
                                        intent.type = "video/*"
                                        var ilop = intent.putExtra(Intent.EXTRA_STREAM, uri)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)



                                        context!!.startActivity(intent)
                                    }

                                })

                            Toast.makeText(context, "loading..", Toast.LENGTH_SHORT).show()

                        }

                    })
                bottomsheetview.findViewById<LinearLayout>(R.id.menu_delete)
                    .setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            deleteFiles(position, v)
                        }


                    })
                bottomsheetview.findViewById<LinearLayout>(R.id.menu_rename)
                    .setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            renameFiles(position, v)
                        }


                    })
                bottomsheetview.findViewById<LinearLayout>(R.id.menu_properties)
                    .setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            showProperties(position)
                        }


                    })

                bottomsheeetdialog.setContentView(bottomsheetview)
                bottomsheeetdialog.show()

            }

        })

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (holder.checkbox.visibility == View.GONE) {
                    val intent: Intent = Intent(context, VideoPlay::class.java)
                    intent.putExtra("foldername", lop.get(position).title)
                    intent.putExtra("positio", position)
                    var bundle = Bundle()
                    bundle.putParcelableArrayList("videolist", ArrayList(lop))
                    intent.putExtras(bundle)

                    startActivity(context!!, intent, null)
                }
                if (holder.checkbox.isVisible) {
                    if (holder.checkbox.isChecked) {
                        holder.checkbox.isChecked = false
                        selectlist.remove(videoactivity!!.givemyarray.get(position))

                        listdelete.remove(position)
                    } else {
                        holder.checkbox.isChecked = true
                        selectlist.add(videoactivity!!.givemyarray.get(position))

                        listdelete.add(position)
                    }
                    mainViewModel.setText(FilesActivity.give.selectlist.size.toString() + " items Selected")
                }
            }
        })






        if (videoactivity!!.is_sellected) {
            holder.checkbox.visibility = View.VISIBLE
            holder.more.visibility = View.GONE
            if (selectlist.size > 0) {
                if (selectlist.contains(lop.get(position))) {
                    holder.checkbox.isChecked = true
                } else {
                    holder.checkbox.isChecked = false
                }
            } else {
                holder.checkbox.isChecked = false
            }

        } else {
            holder.checkbox.isChecked = false
            holder.checkbox.visibility = View.GONE
            holder.more.visibility = View.VISIBLE
        }


    }


    }



    fun notifyme(listdelete: ArrayList<model>) {

        for(i in listdelete){
            lop.remove(i)
        }
        notifyDataSetChanged()
    }







    private fun showProperties(position: Int) {


        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.file_properties)
        val lpo = FFmpegMediaMetadataRetriever()
        lpo.setDataSource(lop.get(position).path)
        val op = lpo.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_FILESIZE)
        var op2=lpo.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)

        val name: String? = lop.get(position).title
        val path: String? = lop.get(position).path
        val size: String?  = op.toString()
        val duration: String? = lop.get(position).duration
        val resolution: String? = op2.toString()

        val tit = dialog.findViewById<TextView>(R.id.pro_title)
        val st = dialog.findViewById<TextView>(R.id.pro_storage)
        val siz = dialog.findViewById<TextView>(R.id.pro_size)
        val dur = dialog.findViewById<TextView>(R.id.pro_duration)
        val res = dialog.findViewById<TextView>(R.id.pro_resolution)

        tit.text = name
        st.text = path
        siz.text = size+" Bytes"
        dur.text = duration
        res.text = resolution + "p"

        dialog.show()
    }

     fun renameFiles(position: Int, v: View?) {

        val dialog =  Dialog(context!!);
        dialog.setContentView(R.layout.rename_layout)
        val edittext=dialog.findViewById<EditText>(R.id.rename_edit_text)
        val cancel=dialog.findViewById<Button>(R.id.cancel_rename_button)
        val rename_but=dialog.findViewById<Button>(R.id.rename_button)
        var file2:File=File(lop.get(position).path.toString())
        var nametext=file2.name
         var fordatabase=nametext
         var renamecon=File(file2.parentFile.absolutePath).listFiles()
         var renameoflist=ArrayList<String>()
         for( i in renamecon){
             if(i.name.endsWith(".mp4") || i.name.endsWith(".mkv")){
                 var rathod=i.name.lastIndexOf(".")
                 renameoflist.add(i.name.substring(0,rathod).toLowerCase())
             }
         }
        nametext=nametext.substring(0,nametext.lastIndexOf("."))
        edittext.setText(nametext)
        edittext.requestFocus()

        cancel.setOnClickListener(object  :View.OnClickListener{
            override fun onClick(v: View?) {
                dialog.dismiss()
            }

        })
        rename_but.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if(TextUtils.isEmpty(edittext.text.toString())){
                    Toast.makeText(context,"File name cannot be Empty",Toast.LENGTH_SHORT).show()



                }
                else if(renameoflist.contains(edittext.text.toString().toLowerCase())&& edittext.text.toString().toLowerCase()!=file2.name.toLowerCase()){
                    Toast.makeText(context,"This File name is already used",Toast.LENGTH_SHORT).show()
                }
                else {
                    val onlyPath: String = file2.getParentFile().getAbsolutePath()
                    var ext: String = file2.getAbsolutePath()
                    ext = ext.substring(ext.lastIndexOf("."))
                    val newPath = onlyPath + "/" + edittext.getText() + ext
                    var renamefile: File = File(newPath)


                    var re = file2.renameTo(renamefile)
                    if (re) {
                        renamedone = true

                        renamearray.add(lop.get(position))
                        var newname = edittext.getText()
                        var newname2 = "" + newname + ext
                        onerenamed = nametext

                        renamesuper = newname2
                        renamepath = newPath

                        hlloris = model((newname2), newPath, lop.get(position).duration)
                        videoactivity!!.renamed2 = true

                        GlobalScope.launch {
                            val datalist=videoactivity!!.videoDataBase.fileDao().kop2(fordatabase,file2.parentFile.name)
                            var rid:Long=0.toLong()
                            for(uo in datalist){
                                rid=uo.id

                            }
                            val anotherquery=videoactivity!!.videoDataBase.fileDao().rename(newname2,newPath,rid)
                            Log.d("success","rename"+anotherquery)

                        }

                        lop.set(position, hlloris)
                        Collections.sort(lop,AlphanumericComparator())

                        notifyDataSetChanged()


                        Toast.makeText(context, "Rename Success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Rename Failed", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }

            }

        })
        dialog.show()
    }

    private fun deleteFiles(position: Int, v: View?) {
        val builder: AlertDialog.Builder =AlertDialog.Builder(context!!)
        builder.setTitle("delete")
            .setMessage(lop.get(position).title)
            .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which ->
            }).setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which ->

                val file = File(lop.get(position).path.toString())
                var filert=file.parentFile.name
                var fordatabase=lop.get(position).title!!
                val deleted: Boolean = file.delete()
                if (deleted) {
                    deletedone=true
                    deletearray.add(lop.get(position))
                    var substance=MainActivity()
                    substance.updatecount(filert,1)

                    GlobalScope.launch {

                        val anotherquery=videoactivity!!.videoDataBase.fileDao().deleteme(fordatabase,filert)
                        Log.d("success","delete"+anotherquery)

                    }
                    lop.removeAt(position)
                   Collections.sort(lop,AlphanumericComparator())
                    notifyDataSetChanged()

                    Toast.makeText(context,"File Deleted Success",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,"Deletion Failed",Toast.LENGTH_SHORT).show()
                }
            }).show()

    }

    override fun getItemCount(): Int {

        if(showshimmer){

            Log.d("shimmer","ioppp  ")
            return 10
        }
        else{

            Log.d("shimmer","gipolp ")
            return lop.size
        }

    }


    class MYv(
        itemView: View,
        var videoactivity: FilesActivity
    ) : RecyclerView.ViewHolder(itemView){


        var newselect=ArrayList<model>()



        var title=itemView.findViewById<TextView>(R.id.video_title)
         var more=itemView.findViewById<ImageView>(R.id.sheetitems)
        var checkbox=itemView.findViewById<CheckBox>(R.id.checkitems)
        var newly=itemView.findViewById<TextView>(R.id.addednew)
        var duration=itemView.findViewById<TextView>(R.id.video_dur)

        var image=itemView.findViewById<ImageView>(R.id.video_image)
        var shimmerframe=itemView.findViewById<ShimmerFrameLayout>(R.id.shimmerframe)


        var oi=this.setIsRecyclable(false)

       var uitems=itemView.setOnLongClickListener(videoactivity)
        var kw=fileview()
        var checkb=checkbox.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                videoactivity.prepareselection(v,absoluteAdapterPosition)



            }

        })
        var checked=checkbox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

            }

        })



    }



    override fun getFilter(): Filter {
        return filmy
    }
    var filmy= object : Filter(){
        override fun performFiltering(keyword: CharSequence?): FilterResults {
            var filteredata=ArrayList<model>()

            if(c==1){
                backup.addAll(videoactivity?.givemyarray!!)
                Collections.sort(backup,AlphanumericComparator())

                c++
            }
           if(renamedone){

            removebackup(renamedone,backup) }
            if(deletedone){

                deletebackup(deletedone,backup)
            }
            if(keyword!!.toString().isEmpty()){

                filteredata.addAll(backup)
           }
            else{

                for(obj in backup){
                    if(obj.title!!.toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                        filteredata.add(obj)
                }
            }
            var filteresults=FilterResults()
            filteresults.values=filteredata
            return filteresults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            lop.clear()

                lop.addAll(results?.values as Collection<model>)

            notifyDataSetChanged()

        }

    }

    private fun deletebackup(deleteop: Boolean, backup: ArrayList<model>) {

            deletedone=false
            var itr=backup.iterator()
            while (itr.hasNext()){

                var phone=itr.next()
                if(deletearray.contains(phone)){

                    itr.remove()

                }
            }

    }

    private fun removebackup(renameop: Boolean, backup: ArrayList<model>) {
        for(kol in 0..backup.size-1){
            var ansme=backup.get(kol)
            if(renamearray.contains(ansme)){

                var superboi=model(renamesuper,renamepath,ansme.duration)
                backup.set(kol,superboi)
            }
        }

        renamedone=false

    }






}

