package com.example.myapplication.ui.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Button
import android.widget.ImageView
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R
//처음에만 사진접근권한 허용 묻도록
//접근권한 안 물어보고 갤러리에서 사진 가져오는게 안된댕
class UploadPhotoActivity:AppCompatActivity() {
    private lateinit var imageView:ImageView
    private var isPhotoUploaded=false

    private lateinit var btn1:Button
    private lateinit var btn2:Button

    private val pickImageLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if(result.resultCode==RESULT_OK){
            val uri:Uri?=result.data?.data
            uri?.let{
                imageView.setImageURI(it)
            }
        }
    }
    //갤러리 접근권한 요청
    private val requestPermissionLauncher=
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted->
            if(isGranted){
                openGallery()
            }
            else{ //dialog 사용 X. Toast 써봄
                Toast.makeText(this,"사진 접근 권한이 필요합니다.",Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_photo)

        imageView = findViewById<ImageView>(R.id.uploadPhoto) // 적절한 ID 사용
        btn1=findViewById(R.id.galleryBtn)
        btn2=findViewById(R.id.uploadBtn)

        updateButtonVisibility()
        btn1.setOnClickListener { //갤러리 버튼을 클릭하면 권한 확인 후 사진을 가져오는 함수 실행
            checkPermissionAndOpenGallery()
            isPhotoUploaded=true
            updateButtonVisibility()
        }
    }

    private fun checkPermissionAndOpenGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }

            shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(this, "사진 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                requestPermissionLauncher.launch(permission)
            }

            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }
    //갤러리 여는 함수
    private fun openGallery(){
        val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type="image/*"
        pickImageLauncher.launch(intent)
    }

    //갤러리 접근 버튼 사라지고 업로드 버튼이 보이도록
    private fun updateButtonVisibility(){
        if(isPhotoUploaded){
            btn1.visibility= View.GONE
            btn2.visibility=View.VISIBLE
        }
        else{
            btn1.visibility=View.VISIBLE
            btn2.visibility=View.GONE
        }
    }
}