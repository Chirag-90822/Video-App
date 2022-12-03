package com.example.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaybackiconAdapter(var iconmodellist:ArrayList<IconModel>,var context:Context) : RecyclerView.Adapter<PlaybackiconAdapter.Viewholder>()
    {
lateinit var mlistener:OnItemClickListener

  interface OnItemClickListener{
      fun onItemClick(position: Int) {

      }

  }
    fun setOnItemClickListener( listener:OnItemClickListener ){
        mlistener=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view= LayoutInflater.from(context).inflate(R.layout.icon_layout,parent,false)
        return Viewholder(view,mlistener)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
          holder.iconame.setText(iconmodellist.get(position).icontitle)
        holder.icon.setImageResource(iconmodellist.get(position).imageView)

    }

    override fun getItemCount(): Int {
        return iconmodellist.size
    }
    class Viewholder(itemView: View, mlistener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {



        var item=itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if(mlistener!=null){
                    var position=adapterPosition
                    if(position!=RecyclerView.NO_POSITION){
                        mlistener.onItemClick(position)
                    }
                }
            }

        })
        var iconame=itemView.findViewById<TextView>(R.id.icon_title)
        var icon=itemView.findViewById<ImageView>(R.id.playback_icon)



    }

}