package com.example.kotlinsqlitedb.Activity

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kotlinsqlitedb.DB.OpenHelper
import com.example.kotlinsqlitedb.R
import kotlinx.android.synthetic.main.activity_add_update_record.*

class AddUpdateRecordActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 100
    private val STORAGE_REQUEST_CODE = 101

    private val IMAGE_PICK_CAMERA_CODE = 200
    private val IMAGE_PICK_GALLERY_CODE = 201

    //lateinit var 늦은 초기화
    private lateinit var cameraPermission: Array<String>
    private lateinit var storagePermission: Array<String>

    private var imageUri: Uri? = null

    lateinit var dbHelper:OpenHelper

    private var name:String? = ""
    private var price:String? = ""
    private var description:String? = ""

    //?는 널일 수도 있다는 것것
    private var actionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_record)

        //액션바 초기화
        actionBar = supportActionBar
        //!!은 널을 허용하지 않는 것
        actionBar!!.title = "음식 추가"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        //헬퍼클래스 초기화
        dbHelper = OpenHelper(this)


        //퍼미션 배열 초기화 (여기서 초기화 -> lateinit var 사용)

        cameraPermission = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        storagePermission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        photoIv.setOnClickListener {
            //다이얼로그
            imagePickDialog()
        }

        addBtn.setOnClickListener {

            inputData()
        }

    }


    private fun inputData() {

        name = ""+foodNameEt.text.toString().trim()
        price = ""+foodPriceEt.text.toString().trim()
        description = ""+foodDescriptionEt.text.toString().trim()

        val timestamp = System.currentTimeMillis()
        val id = dbHelper.insertData(
            ""+name,
            ""+price,
            ""+description,
            ""+imageUri,
            ""+timestamp,
            ""+timestamp)


    }

    private fun imagePickDialog() {
        val option = arrayOf("카메라","갤러리")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("이미지 가져오기 ")
        builder.setItems(option){dialog, which ->
            if(which==0){
                //카메라 클릭
                if(checkCameraPermission()){
                    pickFromCamera()
                }else{
                    requestCameraPermission()
                }
            }else{
                //갤러리 클릭
                if(checkStoragePermission()){
                    pickFromGallery()
                }else{
                    requestStoragePermission()
                }
            }
        }

        builder.show()
    }

    private fun pickFromGallery(){
        //인텐트 이용해서 갤러리에서 이미지 가져옴

         val galleryIntent = Intent(Intent.ACTION_PICK)
         galleryIntent.type="image/*"
         startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE)
    }

    private fun requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE)
    }

    private fun checkStoragePermission(): Boolean {

        return ContextCompat.checkSelfPermission(
            this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun pickFromCamera() {
        //인텐트 이용해서 카메라에서 이미지 가져옴

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"Image Title")
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image Description")

        // 이미지 uri 에 넣음
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE)
    }

    private fun requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE)
    }

    private fun checkCameraPermission(): Boolean {
        val result1 = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val result2 = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        return result1 && result2
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() //이전 액티비티로 이동
        return super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE->{
                if(grantResults.isNotEmpty()){
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if( cameraAccepted && storageAccepted){
                        pickFromCamera()
                    }else{
                        Toast.makeText(this,"카메라와 갤러리 권한이 필요합니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            STORAGE_REQUEST_CODE->{
                if(grantResults.isNotEmpty()){
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if(storageAccepted){
                        pickFromGallery()
                    }else{
                        Toast.makeText(this,"갤러리 권한이 필요합니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}