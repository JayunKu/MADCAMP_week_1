package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.SigninActivity

class SignupActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
    }
}