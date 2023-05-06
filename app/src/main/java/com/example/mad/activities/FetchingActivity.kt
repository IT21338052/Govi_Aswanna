package com.example.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad.R
import com.example.mad.adapters.FormAdapter
import com.example.mad.models.FormModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FetchingActivity : AppCompatActivity() {
    private lateinit var topicRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var feedList: ArrayList<FormModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        topicRecyclerView = findViewById(R.id.rvTask)
        topicRecyclerView.layoutManager = LinearLayoutManager(this)
        topicRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        feedList = arrayListOf<FormModel>()

        getFeedBackData()

    }

    private fun getFeedBackData( ) {
        topicRecyclerView.visibility= View.GONE
        tvLoadingData.visibility= View.VISIBLE
        dbRef= FirebaseDatabase.getInstance().getReference("Form")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                feedList.clear()
                if(snapshot.exists()) {
                    for (taskSnap in snapshot.children) {
                        val taskData = taskSnap.getValue(FormModel::class.java)
                        feedList.add(taskData!!)
                    }
                    val mAdapter = FormAdapter(feedList, this@FetchingActivity)
                    topicRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : FormAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent= Intent(this@FetchingActivity, UpdateActivity::class.java)

                            //put extra
                            intent.putExtra("id", feedList[position].id)
                            intent.putExtra("name", feedList[position].name)
                            intent.putExtra("contact", feedList[position].contact)
                            intent.putExtra("category", feedList[position].category)
                            intent.putExtra("description", feedList[position].description)

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