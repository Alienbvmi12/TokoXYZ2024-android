package com.example.tokoxyz2024android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tokoxyz2024android.R
import com.example.tokoxyz2024android.adapter.InvoiceAdapter
import com.example.tokoxyz2024android.data.model.ApiResponse
import com.example.tokoxyz2024android.databinding.ActivityInvoiceBinding
import com.google.gson.Gson

class InvoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInvoiceBinding.inflate(layoutInflater)
        val transaksi = Gson().fromJson(intent.getStringExtra("transaksi"), ApiResponse::class.java)

        binding.itemList.adapter = InvoiceAdapter(transaksi.data)

        binding.selesai.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
        setContentView(binding.root)
    }
}