package com.example.tokoxyz2024android.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoxyz2024android.data.model.BarangItem

@BindingAdapter("dataList")
fun bindBarangList(recyclerView: RecyclerView, data: List<BarangItem>){
    val adapter = recyclerView.adapter as ItemAdapter
    adapter.submitList(data)
}