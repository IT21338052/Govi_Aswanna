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
import android.widget.Toast
import com.example.mad.models.FormModel
import com.example.mad.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {
    private lateinit var txtName: EditText
    private lateinit var txtDescription: EditText
    private lateinit var txtContact: EditText
    private lateinit var spinner: Spinner

    private lateinit var btnSave: Button
    private lateinit var btnCancle: Button
    private lateinit var btnBack: ImageView

    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        txtName= findViewById(R.id.txtName)
        txtContact= findViewById(R.id.txtContact)
        txtDescription= findViewById(R.id.txtDescription)
        spinner = findViewById(R.id.spinner)
        btnSave= findViewById(R.id.btnSubmit)
        btnCancle= findViewById(R.id.btnCancle)
        btnBack= findViewById(R.id.btnBack)


        ArrayAdapter.createFromResource(
            this,
            R.array.complains_category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        dbRef = FirebaseDatabase.getInstance().getReference("Form")

        btnSave.setOnClickListener{
            saveTaskData()
        }
        btnCancle.setOnClickListener{
            txtName.text.clear()
            txtContact.text.clear()
            txtDescription.text.clear()
        }
        btnBack.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    private fun saveTaskData() {
        //geting values
        val name = txtName.text.toString()
        val description = txtDescription.text.toString()
        val contact = txtContact.text.toString()
        val category = spinner.selectedItem.toString()


        if(title.isEmpty()){
            txtName.error="Please enter Name"
        }
        if(contact.isEmpty()){
            txtContact.error="Please enter Contact details"
        }
        if(description.isEmpty()){
            txtDescription.error="Please enter Description"
        }


        val id = dbRef.push().key!!

        val task= FormModel(id,name,contact,category,description)


        dbRef.child(id).setValue(task)
            .addOnCompleteListener{
                Toast.makeText(this,"Data insert successfully", Toast.LENGTH_LONG).show()
                txtName.text.clear()
                txtContact.text.clear()
                txtDescription.text.clear()

            }.addOnFailureListener { err->
                Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }

}