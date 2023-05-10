package com.example.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.mad.R
import com.example.mad.models.InventoryFormModel
import com.google.firebase.database.*

class InventoryMainActivity : AppCompatActivity() {
    private lateinit var btnAddForm: Button
    private lateinit var btnViewForm: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var inventoryRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_main)

        database = FirebaseDatabase.getInstance()
        inventoryRef = database.getReference("Inventory")

        inventoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var sum = 0
                for (data in snapshot.children) {
                    val inventory = data.getValue(InventoryFormModel::class.java)
                    val stock = inventory?.productstock?.toIntOrNull() ?: 0
                    sum += stock
                }
                Toast.makeText(this@InventoryMainActivity, "Sum of all products in inventory: $sum", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InventoryMainActivity, "Error retrieving inventory data: $error", Toast.LENGTH_SHORT).show()
            }
        })


        btnAddForm= findViewById(R.id.btnAddForm)
        btnViewForm=findViewById(R.id.btnViewForm)

        btnAddForm.setOnClickListener{
            val intent = Intent(this, InventoryInsertionActivity::class.java)
            startActivity(intent)
        }
        btnViewForm.setOnClickListener{
            val intent = Intent(this, InventoryFetchingActivity::class.java)
            startActivity(intent)
        }
    }
}