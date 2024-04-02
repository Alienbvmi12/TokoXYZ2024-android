package com.example.tokoxyz2024android.data.storage

import android.content.Context
import android.content.SharedPreferences

class LoginSessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    fun saveToken(token: String){
        sharedPreferences.edit().putString("token", "Bearer $token").apply()
    }

    fun getToken(): String?{
        return sharedPreferences.getString("token", null)
    }

    fun clearToken(){
        sharedPreferences.edit().clear().apply()
    }
}