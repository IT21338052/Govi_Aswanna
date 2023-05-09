package com.example.mad.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mad.R
import com.example.mad.activities.InventoryUpdateActivity
import com.example.mad.models.InventoryFormModel
import com.google.firebase.database.FirebaseDatabase

class InventoryFormAdapter(private val feedList: ArrayList<InventoryFormModel>, private val context: Context) :
    RecyclerView.Adapter<InventoryFormAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.inventoryform_listitem, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFeed = feedList[position]
        holder.pdctName.text = currentFeed.productname
        holder.pdctStock.text= currentFeed.productstock

//        FirebaseStorage.getInstance().getReference(currentFeed.productimage ?: "").downloadUrl
//            .addOnSuccessListener { downloadUrl ->
//                Glide.with(context).load(downloadUrl).into(holder.viewIn_pImg)
//            }

        /**
         * delete item from database
         */
        holder.btnDelete.setOnClickListener {
            val dbRef = FirebaseDatabase.getInstance().getReference("Inventory")
                .child(currentFeed.productid.toString())
            val mTask = dbRef.removeValue()
            feedList.drop(position)
            Toast.makeText(context, "Item has been deleted!!", Toast.LENGTH_LONG).show()
        }

        /**
         *  Intent to edit page
         */
        holder.btnEdit.setOnClickListener {
            context.startActivity(
                Intent(context, InventoryUpdateActivity::class.java).apply {
                    putExtra("id", currentFeed.productid)
                    putExtra("name", currentFeed.productname)
                    putExtra("category", currentFeed.productcategory)
                    putExtra("stock", currentFeed.productstock)
//                    putExtra("image", currentFeed.productimage)
                    putExtra("description", currentFeed.productdescription)
                })
        }
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val pdctName: TextView = itemView.findViewById(R.id.pdctName)
        val pdctStock: TextView = itemView.findViewById(R.id.pdctStock)
        val viewIn_pImg: ImageView = itemView.findViewById(R.id.pdctImg)
        val btnEdit: Button = itemView.findViewById(R.id.btnUpdate)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }
}
