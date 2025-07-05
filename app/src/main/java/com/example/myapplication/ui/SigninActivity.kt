package com.example.myapplication.ui
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.DialogUtils
import com.example.myapplication.R

class SigninActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_signin)
    }

}