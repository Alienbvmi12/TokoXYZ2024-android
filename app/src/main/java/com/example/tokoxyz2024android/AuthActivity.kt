package com.example.tokoxyz2024android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tokoxyz2024android.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAuthBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}