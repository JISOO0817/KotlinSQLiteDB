package com.example.kotlinsqlitedb.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlinsqlitedb.AdapterData
import com.example.kotlinsqlitedb.DB.Constants
import com.example.kotlinsqlitedb.DB.OpenHelper
import com.example.kotlinsqlitedb.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var dbHelper:OpenHelper
    private val NEW = " ${Constants.C_ADD_STAMP} DESC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = OpenHelper(this)
        loadData()
        val gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridLayoutManager
        addFBtn.setOnClickListener {
            startActivity(Intent(this, AddUpdateRecordActivity::class.java))
        }
    }

    private fun loadData() {
        val adapterData = AdapterData(this, dbHelper.getAllData(NEW))

        recyclerView.adapter = adapterData
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}