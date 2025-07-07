package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.SigninActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity:AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        //firebase auth 회원가입
        auth=Firebase.auth
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_signup)
        val btn1=findViewById<Button>(R.id.signupButton) as Button
        btn1.setOnClickListener{

            DialogUtils.showCustomPopup(
                this,
                "회원가입이 완료되었어요!",
                "로그인하러 가기"
            ){
                val intent=Intent(this,SigninActivity::class.java)
                startActivity(intent)
            }
        }
        /*fun signUp(email:String,password:String){
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task->
                    if(task.isSuccessful){
                        goToMainActivity(task.result?.user)
                    }else if (task.exception?.message.isNullOrBlank()){
                        DialogUtils.showCustomPopup(
                            this,
                            "올바른 정보를 입력해주세요!",
                            "뒤로 가기"
                        ){
                            val intent=Intent(this,SignupActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    else{
                        signIn(email,password)
                    }

                }
        }*/
    }
}