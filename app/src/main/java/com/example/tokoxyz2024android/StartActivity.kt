package com.example.tokoxyz2024android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tokoxyz2024android.data.storage.LoginSessionManager

class StartActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userSessionManager = LoginSessionManager(this)
        val token = userSessionManager.getToken()
        if(token.isNullOrEmpty()){
            startActivity(Intent(this, AuthActivity::class.java))
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}