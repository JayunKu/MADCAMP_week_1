package com.example.myapplication.ui
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.DialogUtils
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SigninActivity:AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth=Firebase.auth
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_signin)
        val btn1=findViewById<Button>(R.id.signinButton) as Button
        btn1.setOnClickListener{
            val email=getInputEmail()
            val password=getInputPassword()
            if(email.isEmpty()||password.isEmpty()){
                DialogUtils.showCustomPopup(
                    this,
                    "이메일과 비밀번호를 입력해주세요!",
                    "돌아가기"
                ){
                   finish()
                }
            }
            else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            DialogUtils.showCustomPopup(
                                this,
                                "로그인에 성공했어요!",
                                "메인화면으로"
                            ) {
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            DialogUtils.showCustomPopup(
                                this,
                                "로그인에 실패했어요!",
                                "돌아가기"
                            ) {
                                val intent = Intent(this, SigninActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
            }
        }
    }
    private fun getInputEmail():String{
        val emailEditText=findViewById<android.widget.EditText>(R.id.inputUserEmail)
        return emailEditText.text.toString().trim()
    }
    private fun getInputPassword():String{
        val passwordEditText= findViewById<android.widget.EditText>(R.id.inputUserPassword)
        return passwordEditText.text.toString().trim()
    }

}