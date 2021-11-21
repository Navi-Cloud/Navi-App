package com.navi.file.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.navi.file.R
import com.navi.file.model.FileData

class MyFileFragmentAdapter(private var list : MutableList<FileData>) : RecyclerView.Adapter<MyFileFragmentAdapter.ViewHolder>() {

    inner class ViewHolder(private val view : View): RecyclerView.ViewHolder(view) {

        fun bind(item : FileData){
            val filename : TextView = view.findViewById(R.id.filename)
            val filedata : TextView = view.findViewById(R.id.filedate)
            val isfile : ImageView = view.findViewById(R.id.filestyle)

            filename.text = item.filename
            filedata.text = item.date
            if(item.isFolder){
                isfile.setImageResource(R.drawable.icon_black_folder)
            }else{
                isfile.setImageResource(R.drawable.icon_black_file)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyFileFragmentAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_my_file,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}