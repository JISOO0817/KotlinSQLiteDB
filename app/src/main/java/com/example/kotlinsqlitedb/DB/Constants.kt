package com.example.kotlinsqlitedb.DB

object Constants {


    // const val 은 자바에서 static final 과 같음
    //DB 이름
    const val DB_NAME = "FOOD_DB"

    //DB 버전
    const val DB_VERSION = 1

    //Table 이름
    const val TABLE_NAME = "FOOD_TABLE"

    //Columns
    const val C_ID = "ID"
    const val C_NAME = "NAME"
    const val C_PRICE = "PRICE"
    const val C_DESCRIPTION = "DESCRIPTION"
    const val C_IMAGE = "IMAGE"
    const val C_ADD_STAMP = "ADD_STAMP"
    const val C_UPDATE_STAMP = "UPDATE_STAMP"

    const val CREATE_TABLE = (
            "CREATE TABLE" + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_NAME + " TEXT,"
            + C_PRICE + " TEXT,"
            + C_DESCRIPTION + " TEXT,"
            + C_IMAGE + " TEXT,"
            + C_ADD_STAMP + " TEXT,"
            + C_UPDATE_STAMP + " TEXT"
            + ")"
            )

}