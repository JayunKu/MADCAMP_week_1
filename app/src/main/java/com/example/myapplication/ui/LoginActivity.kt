package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.myapplication.ui.SigninActivity

class LoginActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        val btn1=findViewById<Button>(R.id.signin_button) as Button
        val btn2=findViewById<Button>(R.id.signup_button) as Button
        btn1.setOnClickListener{
            val intent=Intent(this,SigninActivity::class.java)
            startActivity(intent)
        }
        btn2.setOnClickListener {
            val intent=Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }
    }
}
