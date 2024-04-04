package com.example.tokoxyz2024android.adapter

import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tokoxyz2024android.R
import com.example.tokoxyz2024android.data.model.BASE_URL_NO_API
import com.example.tokoxyz2024android.data.model.BarangItem

@BindingAdapter("dataList")
fun bindBarangList(recyclerView: RecyclerView, data: List<BarangItem>){
    val adapter = recyclerView.adapter as ItemAdapter
    adapter.submitList(data)
}

@BindingAdapter("loadRating")
fun bindRating(txt: TextView, data: BarangItem){
    txt.text = data.rating.toString()
}

@BindingAdapter("loadNama")
fun bindNama(txt: TextView, data: BarangItem){
    txt.text = data.namaBarang.toString()
}

@BindingAdapter("loadHarga")
fun bindHarga(txt: TextView, data: BarangItem){
    txt.text = data.harga.toString()
}


@BindingAdapter("loadImage")
fun loadImage(imgView: ImageView, data: BarangItem){
    val url = BASE_URL_NO_API + data.image
    imgView.load(url)
}