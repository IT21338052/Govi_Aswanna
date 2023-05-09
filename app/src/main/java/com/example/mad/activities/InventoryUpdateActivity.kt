package com.example.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.mad.R
import com.example.mad.models.InventoryFormModel
import com.google.firebase.database.FirebaseDatabase

class InventoryUpdateActivity : AppCompatActivity() {
    private lateinit var txtName: EditText
    private lateinit var spinner: Spinner
    private lateinit var txtStock: EditText
    private lateinit var txtDescription: EditText


    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var btnBack: ImageView
    private lateinit var txtCategory: String

    private lateinit var btnUpdate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_update)

        initView()
        setValuesToViews()

        val categoryList = resources.getStringArray(R.array.complains_category_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setSelection(categoryList.indexOf(txtCategory))

        btnCancel.setOnClickListener {
            setValuesToViews()
        }

        btnSave.setOnClickListener {
            updateTaskData(
                intent.getStringExtra("id").toString(),
                txtName.text.toString(),
                spinner.selectedItem.toString(),
                txtStock.text.toString(),
                txtDescription.text.toString(),
            )
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, InventoryFetchingActivity::class.java))
        }
    }

    private fun initView() {
        txtName = findViewById(R.id.txtName)
        spinner = findViewById(R.id.spinner)
        txtStock = findViewById(R.id.txtStock)
        txtDescription = findViewById(R.id.txtDescription)
        btnSave = findViewById(R.id.btnSubmit)
        btnCancel = findViewById(R.id.btnCancle)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun setValuesToViews() {
        txtName.setText(intent.getStringExtra("name"))
        txtCategory = intent.getStringExtra("category").toString()
        txtStock.setText(intent.getStringExtra("stock"))
        txtDescription.setText(intent.getStringExtra("description"))
    }

    private fun updateTaskData(
        id: String,
        name: String,
        category: String,
        stock: String,
        description: String
    ) {
        if (!txtCategory.equals(category)) {
            txtCategory = category
        }

        val dbRef = FirebaseDatabase.getInstance().getReference("Inventory").child(id)
        val taskInfo = InventoryFormModel(id, name,txtCategory, stock,  description)
        val task = dbRef.setValue(taskInfo)

        task.addOnSuccessListener {
            Toast.makeText(this, "Data updated", Toast.LENGTH_LONG).show()

            val intent = Intent(this, InventoryFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Update Error ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}
