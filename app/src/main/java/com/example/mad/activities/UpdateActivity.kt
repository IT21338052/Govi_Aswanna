package com.example.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.mad.R
import com.example.mad.models.FormModel
import com.google.firebase.database.FirebaseDatabase

class UpdateActivity : AppCompatActivity() {
    private lateinit var txtName: EditText
    private lateinit var txtDescription: EditText
    private lateinit var txtContact: EditText
    private lateinit var spinner: Spinner

    private lateinit var btnSave: Button
    private lateinit var btnCancle: Button
    private lateinit var btnBack: ImageView
    private lateinit var txtCategory : String

    private lateinit var btnUpdate: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        initView()
        setValuesToViews()


        ArrayAdapter.createFromResource(
            this,
            R.array.complains_category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        btnCancle.setOnClickListener{
            setValuesToViews()
        }

        btnSave.setOnClickListener{
            updateTaskData(
                intent.getStringExtra("id").toString(),
                txtName.text.toString(),
                txtContact.text.toString(),
                spinner.selectedItem.toString(),
                txtDescription.text.toString(),
            )
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this,FetchingActivity::class.java))
        }

    }

    private fun initView() {
        txtName= findViewById(R.id.txtName)
        txtContact= findViewById(R.id.txtContact)
        txtDescription= findViewById(R.id.txtDescription)
        spinner = findViewById(R.id.spinner)
        btnSave= findViewById(R.id.btnSubmit)
        btnCancle= findViewById(R.id.btnCancle)
        btnBack= findViewById(R.id.btnBack)

    }
    private fun setValuesToViews() {
        txtName.setText(intent.getStringExtra("name"))
        txtContact.setText(intent.getStringExtra("contact"))
        txtCategory = intent.getStringExtra("category").toString()
        txtDescription.setText(intent.getStringExtra("description"))
    }

    private fun updateTaskData(id: String,name: String,contact: String,category: String,description: String) {

        if(!txtCategory.equals(category)){
            txtCategory = category
        }
        val dbRef = FirebaseDatabase.getInstance().getReference("Form").child(id)
        val taskInfo = FormModel(id,name,contact,txtCategory,description)
        val task = dbRef.setValue(taskInfo)

        task.addOnSuccessListener {
            Toast.makeText(this, "Data updated", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Update Error ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}