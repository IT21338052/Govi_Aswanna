package com.example.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad.R
import com.example.mad.adapters.InventoryFormAdapter
import com.example.mad.models.InventoryFormModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InventoryFetchingActivity : AppCompatActivity() {
    private lateinit var topicRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var feedList: ArrayList<InventoryFormModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_fetching)

        topicRecyclerView = findViewById(R.id.rvTask)
        topicRecyclerView.layoutManager = LinearLayoutManager(this)
        topicRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        feedList = arrayListOf<InventoryFormModel>()

        getFeedBackData()

    }

    private fun getFeedBackData( ) {
        topicRecyclerView.visibility= View.GONE
        tvLoadingData.visibility= View.VISIBLE
        dbRef= FirebaseDatabase.getInstance().getReference("Inventory")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                feedList.clear()
                if(snapshot.exists()) {
                    for (taskSnap in snapshot.children) {
                        val taskData = taskSnap.getValue(InventoryFormModel::class.java)
                        feedList.add(taskData!!)
                    }
                    val mAdapter = InventoryFormAdapter(feedList, this@InventoryFetchingActivity)
                    topicRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : InventoryFormAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent= Intent(this@InventoryFetchingActivity, InventoryUpdateActivity::class.java)

                            //put extra
                            intent.putExtra("id", feedList[position].productid)
                            intent.putExtra("name", feedList[position].productname)
                            intent.putExtra("category", feedList[position].productcategory)
                            intent.putExtra("stock", feedList[position].productstock)
                            intent.putExtra("description", feedList[position].productdescription)

                            startActivity(intent)
                        }

                    })

                    topicRecyclerView.visibility= View.VISIBLE
                    tvLoadingData.visibility= View.GONE


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}