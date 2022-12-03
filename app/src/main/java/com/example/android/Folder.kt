package com.example.android

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Folder(
    val myfolder: HashMap<String, String>,
    val mycount: HashMap<String, Int>,
    val folderarray: ArrayList<String>,
    val context: Context?) :
    RecyclerView.Adapter<Folder.myview>() {
    constructor():this(
        HashMap(),
       HashMap(), ArrayList(),null){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myview {
        val view=LayoutInflater.from(context).inflate(R.layout.folder,parent,false)
        return myview(view)

    }


    override fun onBindViewHolder(holder: myview, position: Int) {
        var foldername=folderarray.get(position)
        holder.name.setText(foldername)
        var folderco=mycount.get(foldername)
        holder.count.setText(folderco.toString())
        holder.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent:Intent=Intent(context,FilesActivity::class.java)
                intent.putExtra("foldername",foldername)
                intent.putExtra("folderpath",myfolder)
                intent.putExtra("co",folderco.toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context!!.startActivity(intent)

            }

        })

    }

    override fun getItemCount(): Int {
        return folderarray.size
    }
    class myview(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name:TextView=itemView.findViewById(R.id.name)
        var count:TextView=itemView.findViewById(R.id.count)



    }


}