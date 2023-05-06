package com.example.mad.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mad.R
import com.example.mad.activities.UpdateActivity
import com.example.mad.models.FormModel
import com.google.firebase.database.FirebaseDatabase

class FormAdapter(private  val feedList:ArrayList<FormModel>, private val context: Context) :
    RecyclerView.Adapter<FormAdapter.ViewHolder>(){

    private  lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener =clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val itemView =LayoutInflater.from(parent.context).inflate(R.layout.form_list_item,parent,false)
        return  ViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stringBuilder :StringBuilder;
        stringBuilder =  java.lang.StringBuilder();
        val currentfeed= feedList[position]
        holder.lblId.text= stringBuilder.append("Form ID : ").append(currentfeed.id)
        holder.lblAbout.text=stringBuilder.append("About : ").append(currentfeed.description)

        /**
         * delete item from database
         */
        holder.btnDelete.setOnClickListener{
            val dbRef = FirebaseDatabase.getInstance().getReference("Form").child(currentfeed.id.toString())
            val mTask = dbRef.removeValue()
            feedList.drop(position);
            Toast.makeText(context, "Item has been deleted!!",Toast.LENGTH_LONG).show();
        }

        /**
         *  Intent to edit page
         */
        holder.btnEdit.setOnClickListener{
            context.startActivity(Intent(context, UpdateActivity::class.java).apply {
                putExtra("id", currentfeed.id)
                putExtra("name", currentfeed.name)
                putExtra("contact", currentfeed.contact)
                putExtra("category", currentfeed.category)
                putExtra("description", currentfeed.description)
            })
        }

    }


    override fun getItemCount(): Int {
       return feedList.size
    }
    class ViewHolder(itemView: View, clickListener: onItemClickListener):RecyclerView.ViewHolder(itemView) {
        val lblId : TextView =itemView.findViewById(R.id.lblId)
        val lblAbout : TextView = itemView.findViewById(R.id.lblAbout)
        val btnEdit : Button =itemView.findViewById(R.id.btnEdit)
        val btnDelete : Button =itemView.findViewById(R.id.btnDelete)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

}