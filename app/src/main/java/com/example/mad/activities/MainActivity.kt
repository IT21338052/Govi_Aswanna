package com.example.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mad.R

class MainActivity : AppCompatActivity() {
    private lateinit var btnAddForm: Button
    private lateinit var btnViewForm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAddForm= findViewById(R.id.btnAddForm)
        btnViewForm=findViewById(R.id.btnViewForm)

        btnAddForm.setOnClickListener{
            val intent = Intent(this, InsertionActivity::class.java)
            startActivity(intent)
        }
        btnViewForm.setOnClickListener{
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }
    }
}