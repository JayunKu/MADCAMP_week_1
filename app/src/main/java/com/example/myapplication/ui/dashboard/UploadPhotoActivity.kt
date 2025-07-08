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
import android.database.Cursor
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import java.io.File
import com.example.myapplication.model.Photo

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
            val url:Uri?=result.data?.data
            url?.let{
                imageView.setImageURI(it)
                selectedImageUri=it
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
        btn2.setOnClickListener{
            uploadImageToFirebase()
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

    private fun getPathFromUri(uri: Uri): String? {
        var path: String? = null

        try {
            // MediaStore를 통해 선택된 이미지의 경우
            if (DocumentsContract.isDocumentUri(this, uri)) {
                val docId = DocumentsContract.getDocumentId(uri)

                if (uri.authority == "com.android.providers.media.documents") {
                    val id = docId.split(":")[1]
                    val selection = MediaStore.Images.Media._ID + "=?"
                    val selectionArgs = arrayOf(id)

                    val cursor: Cursor? = contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        arrayOf(MediaStore.Images.Media.DATA),
                        selection,
                        selectionArgs,
                        null
                    )

                    cursor?.use {
                        if (it.moveToFirst()) {
                            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                            path = it.getString(columnIndex)
                        }
                    }
                }
            } else if (uri.scheme == "content") {
                val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        path = it.getString(columnIndex)
                    }
                }
            } else if (uri.scheme == "file") {
                path = uri.path
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return path
    }


    private fun uploadImageToFirebase() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val userEmail = user.email
        val userId = user.uid
        val description = inputDest.text.toString().trim()

        if (description.isEmpty()) {
            Toast.makeText(this, "여행지를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        selectedImageUri?.let { uri ->
            val imagePath = getPathFromUri(uri)
            if (imagePath != null) {
                val photoInfo = Photo(
                    userEmail = userEmail.toString().trim(),
                    userId = userId,
                    imageUrl = imagePath,
                    description = description
                )

                // Firebase Database에 데이터 저장
                val database = FirebaseDatabase.getInstance()
                val photosRef = database.getReference("photos")

                // 고유 키 생성하여 데이터 저장
                val photoKey = photosRef.push().key
                if (photoKey != null) {
                    photosRef.child(photoKey).setValue(photoInfo)
                        .addOnSuccessListener {
                            Toast.makeText(this, "사진이 성공적으로 업로드되었습니다.", Toast.LENGTH_SHORT).show()
                            finish() // 액티비티 종료
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "업로드 실패: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "데이터베이스 키 생성 실패", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "이미지 경로를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}