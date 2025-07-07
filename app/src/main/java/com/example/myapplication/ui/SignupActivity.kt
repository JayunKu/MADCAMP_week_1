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
            val name=getInputName()
            val email=getInputEmail()
            val password=getInputPassword()

            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    task->
                    if(task.isSuccessful){
                        DialogUtils.showCustomPopup(
                            this,
                            "회원가입이 완료되었어요!",
                            "로그인하러 가기"
                        ){
                            val intent=Intent(this,SigninActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    else{
                        DialogUtils.showCustomPopup(
                            this,
                            "회원가입에 실패했어요",
                            "돌아가기"
                        ){
                            val intent=Intent(this,SignupActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }


        }
    }
    private fun getInputEmail():String{
        val emailEditText=findViewById<android.widget.EditText>(R.id.inputEmail)
        return emailEditText.text.toString().trim()
    }
    private fun getInputPassword():String{
        val passwordEditText=findViewById<android.widget.EditText>(R.id.inputPw)
        return passwordEditText.text.toString().trim()
    }
    private fun getInputName():String{
        val nameEditText=findViewById<android.widget.EditText>(R.id.inputName)
        return nameEditText.text.toString().trim()
    }
}