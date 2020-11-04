package com.example.kotlinsqlitedb.Activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kotlinsqlitedb.DB.OpenHelper
import com.example.kotlinsqlitedb.R
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_update_record.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.foodDescriptionEt
import kotlinx.android.synthetic.main.activity_detail.foodNameEt
import kotlinx.android.synthetic.main.activity_detail.foodPriceEt
import kotlinx.android.synthetic.main.activity_detail.photoIv

class DetailActivity : AppCompatActivity() {


    private val CAMERA_REQUEST_CODE = 100
    private val STORAGE_REQUEST_CODE = 101

    private val IMAGE_PICK_CAMERA_CODE = 200
    private val IMAGE_PICK_GALLERY_CODE = 201

    //lateinit var 늦은 초기화
    private lateinit var cameraPermission: Array<String>
    private lateinit var storagePermission: Array<String>



    private  var id:String? =""
    private var name:String? =""
    private var price:String? =""
    private var description:String? =""
    private var imageUri:Uri? = null
    private var image:String? = ""
    private var addTimestamp:String? = ""
    private var updateTimestamp:String? = ""

    lateinit var dbHelper: OpenHelper


    private var actionBar: ActionBar? = null
    /**
     *  intent.putExtra("id",id)
    intent.putExtra("name",name)
    intent.putExtra("price",price)
    intent.putExtra("description",description)
    intent.putExtra("image",image)
    intent.putExtra("addTimestamp",addTimestamp)
    intent.putExtra("updateTimestamp",updateTimestamp)
     *
     */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        actionBar = supportActionBar
        //!!은 널을 허용하지 않는 것
        actionBar!!.title = "음식 정보"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        dbHelper = OpenHelper(this)

        cameraPermission = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        storagePermission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


        //데이터 가져오기
        val intent = intent



        id = intent.getStringExtra("id")
        name = intent.getStringExtra("name")
        price = intent.getStringExtra("price")
        description = intent.getStringExtra("description")
        imageUri = Uri.parse(intent.getStringExtra("image"))
        addTimestamp = intent.getStringExtra("addTimestamp")
        updateTimestamp = intent.getStringExtra("updateTimestamp")


        foodNameEt.setText(name)
        foodPriceEt.setText(price)
        foodDescriptionEt.setText(description)

        try {
            Picasso.get().load(imageUri).placeholder(R.drawable.ic_launcher_background).into(photoIv)
        } catch (e: Exception) {
            photoIv.setImageResource(R.drawable.ic_launcher_background)
        }


        photoIv.setOnClickListener {
            //다이얼로그
            imagePickDialog()

        }

        updateBtn.setOnClickListener {
            updateData()
        }

        deleteBtn.setOnClickListener {
            deleteData()
        }

    }

    private fun deleteData() {

       /* var id = intent.getStringExtra("id")
        if (id != null) {
            dbHelper.delete(id)
        }*/
        var id = intent.getStringExtra("id")
        if (id != null) {
            dbHelper.deleteData(id)
        }
        finish()
    }

    private fun updateData() {

        var name = foodNameEt.text.toString().trim()
        var price = foodPriceEt.text.toString().trim()
        var description = foodDescriptionEt.text.toString().trim()



        // ?. => null이 아닐 때만 실행하고 널이면 null 반환
        val timestamp = System.currentTimeMillis();
        dbHelper?.updateData(
            "$id",
            "$name",
            "$imageUri",
            "$price",
            "$description",
            "$addTimestamp",
            "$timestamp"
        )

        Toast.makeText(this,"수정하였습니다.",Toast.LENGTH_SHORT).show()
        finish()

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
            }else if(which == 1){
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
                        Toast.makeText(this,"카메라와 갤러리 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            STORAGE_REQUEST_CODE->{
                if(grantResults.isNotEmpty()){
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if(storageAccepted){
                        pickFromGallery()
                    }else{
                        Toast.makeText(this,"갤러리 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                CropImage.activity(data!!.data)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this)
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                CropImage.activity(data!!.data)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this)
            }
            else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                val result = CropImage.getActivityResult(data)
                if(resultCode == Activity.RESULT_OK){
                    val resultUri = result.uri
                    imageUri = resultUri
                    photoIv.setImageURI(resultUri)
                }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    val error = result.error
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }



}