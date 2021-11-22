package com.hrishi.tempmail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class inbox_rvAdapter(var data : List<Mail>) : RecyclerView.Adapter<inbox_rvAdapter.viewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_layout, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.senderID.text = data[position].from
        holder.time.text = data[position].date
        holder.subject.text = data[position].subject
    }

    override fun getItemCount(): Int = data.size

    inner class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val subject : TextView
        val time : TextView
        val senderID : TextView

        init {
            subject = itemView.findViewById(R.id.tv_mailSubject)
            time = itemView.findViewById(R.id.tv_mailTime)
            senderID = itemView.findViewById(R.id.tv_senderID)
        }
    }
}