package com.example.kotlinsqlitedb.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class OpenHelper(context: Context?):SQLiteOpenHelper(
    context,
    Constants.DB_NAME,
    null,
    Constants.DB_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(Constants.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS "+ Constants.TABLE_NAME)
        onCreate(db)
    }

    //insert data

    fun insertData(
        name:String?,
        price:String?,
        description:String?,
        image:String?,
        addTimeStamp:String?,
        updateTimeStamp:String?
    ):Long{
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(Constants.C_NAME, name)
        values.put(Constants.C_PRICE, price)
        values.put(Constants.C_DESCRIPTION, description)
        values.put(Constants.C_IMAGE, image)
        values.put(Constants.C_ADD_STAMP,addTimeStamp)
        values.put(Constants.C_UPDATE_STAMP,updateTimeStamp)

        val id = db.insert(Constants.TABLE_NAME,null,values)
        db.close()
        return id

    }


}