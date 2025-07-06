package com.example.myapplication.ui
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.DialogUtils
import com.example.myapplication.MainActivity
import com.example.myapplication.R

class SigninActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_signin)
        val btn1=findViewById<Button>(R.id.signinButton) as Button
        btn1.setOnClickListener{
            DialogUtils.showCustomPopup(
                this,
                "로그인에 성공했어요!",
                "메인화면으로"
            ){
                val intent=Intent(this,MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

}