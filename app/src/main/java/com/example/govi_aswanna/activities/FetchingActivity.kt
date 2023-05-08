package com.example.govi_aswanna.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.govi_aswanna.models.FoodModel
import com.example.govi_aswanna.adapters.foodAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.govi_aswanna.R
import com.example.govi_aswanna.models.User

class FetchingActivity : AppCompatActivity() {

    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var foodList: ArrayList<FoodModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private var accountType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching_mp)

        firebaseAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference.child("Users")

        foodRecyclerView = findViewById(R.id.rvfood)
        foodRecyclerView.layoutManager = LinearLayoutManager(this)
        foodRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        foodList = arrayListOf<FoodModel>()

        getFoodData()

    }

    private fun getFoodData() {

        foodRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Foods")

        val currentUser = FirebaseAuth.getInstance().currentUser

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodList.clear()
                Log.i("Works", "Fine")
                if (snapshot.exists()){
                    Log.i("Works", "Fine2")
                    //Get Id data
                    for (fdSnap in snapshot.children){
                        Log.i("Works", "Fine3")
                        val fdData = fdSnap.getValue(FoodModel::class.java)
                        foodList.add(fdData!!)

                    }
                    Log.i("Works", foodList.toString())
                    val mAdapter = foodAdapter(foodList)
                    foodRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : foodAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val userQuery = dbRef.orderByChild("email").equalTo(currentUser?.email)
                            Log.i("Works", userQuery.toString())

                            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        Log.i("Works", "Snap exists")
                                        for (data in snapshot.children) {
                                            val user = data.getValue(User::class.java)
                                            if (user?.accountType == "Farmer") {
                                                val intent = Intent(this@FetchingActivity, FoodDetailsActivity::class.java)

                                                intent.putExtra("type", foodList[position].type)
                                                intent.putExtra("foodId", foodList[position].foodId)
                                                intent.putExtra("foodName", foodList[position].foodName)
                                                intent.putExtra("qty", foodList[position].qty)
                                                intent.putExtra("price", foodList[position].price)
                                                intent.putExtra("seller", foodList[position].seller)

                                                startActivity(intent)
                                            } else if (user?.accountType == "Buyer") {

                                                val intent = Intent(this@FetchingActivity, FoodDetailsBuyerActivity::class.java)

                                                intent.putExtra("type", foodList[position].type)
                                                intent.putExtra("foodId", foodList[position].foodId)
                                                intent.putExtra("foodName", foodList[position].foodName)
                                                intent.putExtra("qty", foodList[position].qty)
                                                intent.putExtra("price", foodList[position].price)
                                                intent.putExtra("seller", foodList[position].seller)

                                                startActivity(intent)
                                            }
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // handle onCancelled event
                                }
                            })


                        }

                    })

                    foodRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}








