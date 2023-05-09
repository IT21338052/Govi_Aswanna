package com.example.mad.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mad.R
import com.example.mad.models.InventoryFormModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class InventoryInsertionActivity : AppCompatActivity() {
    private lateinit var txtName: EditText
    private lateinit var txtDescription: EditText
    private lateinit var txtStock: EditText
    private lateinit var spinner: Spinner
    private var sImage: String? = ""

    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var btnBack: ImageView

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_insertion)

        txtName = findViewById(R.id.txtName)
        spinner = findViewById(R.id.spinner)
        txtStock = findViewById(R.id.txtStock)
        txtDescription = findViewById(R.id.txtDescription)
        btnSave = findViewById(R.id.btnSubmit)
        btnCancel = findViewById(R.id.btnCancle)
        btnBack = findViewById(R.id.btnBack)

        ArrayAdapter.createFromResource(
            this,
            R.array.complains_category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        dbRef = FirebaseDatabase.getInstance().getReference("Inventory")

        btnSave.setOnClickListener{
            saveFormData()
        }
        btnCancel.setOnClickListener{
            clearForm()
        }
        btnBack.setOnClickListener {
            startActivity(Intent(this, InventoryMainActivity::class.java))
        }
    }

    private fun saveFormData() {
        val name = txtName.text.toString()
        val category = spinner.selectedItem.toString()
        val stock = txtStock.text.toString()
        val description = txtDescription.text.toString()



        if (name.isEmpty() || stock.isEmpty() || category.equals("Select Category") || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all the required fields.", Toast.LENGTH_SHORT).show()
            return
        }
        if(title.isEmpty()){
            txtName.error="Please enter Name"
        }
        if(stock.isEmpty()){
            txtStock.error="Please enter Contact details"
        }
        if(description.isEmpty()){
            txtDescription.error="Please enter Description"
        }

        val id = dbRef.push().key!!

        val formData = InventoryFormModel(id, name, category, stock,sImage, description)

        dbRef.child(id).setValue(formData)
            .addOnCompleteListener{
                Toast.makeText(this,"Data inserted successfully", Toast.LENGTH_LONG).show()
                clearForm()
                val intent = Intent(this, InventoryMainActivity::class.java)
                finish()
                startActivity(intent)
            }
            .addOnFailureListener { err ->
                Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun clearForm() {
        txtName.text.clear()
        txtStock.text.clear()
        txtDescription.text.clear()
    }

    fun insert_Img(view: View) {
        val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
        myfileintent.setType("image/*")
        ActivityResultLauncher.launch(myfileintent)
    }

    private val ActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data
            try {
                val inputStream = contentResolver.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStream)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes = stream.toByteArray()
                sImage = Base64.encodeToString(bytes, Base64.DEFAULT)
                inputStream?.close()
                Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show()
            } catch (ex: Exception) {
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }
}
