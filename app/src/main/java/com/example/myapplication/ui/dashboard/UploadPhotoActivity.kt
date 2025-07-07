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
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

//처음에만 사진접근권한 허용 묻도록
//접근권한 안 물어보고 갤러리에서 사진 가져오는게 안된댕
class UploadPhotoActivity:AppCompatActivity() {
    //필요한 변수들 lateinit:초기화하지 않은 변수들
    private lateinit var imageView:ImageView
    private var isPhotoUploaded=false
    private lateinit var btn1:Button
    private lateinit var btn2:Button
    private lateinit var uploadPhoto:ImageView
    private lateinit var inputDest:EditText

    private var selectedImageUri:Uri?=null

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
        inputDest=findViewById(R.id.inputdest)

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
    //firebase db에 사진과 여행지 정보 저장
    data class PhotoInfo(
        val userId:String="",
        val imageUrl:String="",
        val description:String=""
    )
    fun uploadImageToFirebase() {
        val user = FirebaseAuth.getInstance().currentUser ?: return //현재 로그인한 사용자 정보
        val userId = user.uid //user 정보 중 id를 사용자db에 uid로 저장
        val uri=selectedImageUri?:return //업로드한 사진 uri
        val description = inputDest.text.toString() //입력한 여행지.
        val storageRef = FirebaseStorage.getInstance().reference
        val fileRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        fileRef.putFile(uri)
            .continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it //잘못되면 진행중인 것들 종료되도록
                }
            }
                fileRef.downloadUrl
            }
            .addOnSuccessListener{downloadUri->
                    val photoInfo=PhotoInfo(
                        userId=userId,
                        imageUrl=downloadUri.toString(),
                        description=description
                    )
                //photos db에 photoInfo(세부항목 3개) 저장
                    val dbRef= FirebaseDatabase.getInstance().getReference("photos")
                        .push().setValue(photoInfo)
                        .addOnSuccessListener{
                            inputDest.text.clear()
                            uploadPhoto.setImageResource(R.color.grey)
                            btn2.visibility=View.GONE

                }

    }


    }
}