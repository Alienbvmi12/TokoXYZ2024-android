package com.example.tokoxyz2024android.ui.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokoxyz2024android.data.model.ApiResponse
import com.example.tokoxyz2024android.data.model.ApiResponseItems
import com.example.tokoxyz2024android.data.model.ApiResponseSingle
import com.example.tokoxyz2024android.data.model.BarangItem
import com.example.tokoxyz2024android.data.model.HttpApi
import com.example.tokoxyz2024android.data.storage.LoginSessionManager
import com.google.gson.Gson
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private var _listBarang = MutableLiveData<List<BarangItem?>>()
    val listBarang: LiveData<List<BarangItem?>> = _listBarang
    private var _coResponse = MutableLiveData<ApiResponse>()
    val coResponse: LiveData<ApiResponse> = _coResponse
    private var _checkoutItem = MutableLiveData<
            MutableMap<
                    String, CoItem
                    >
            >()
    val checkoutItem: LiveData<
            MutableMap<
                    String, CoItem
                    >
            > = _checkoutItem

    val context = application.applicationContext
    val userSessionManager = LoginSessionManager(context)

    fun init() {
        _checkoutItem.value = mutableMapOf()
    }

    fun searchBarang(q: String = "") {
        viewModelScope.launch {
            try {
                val res = HttpApi.retrofitService.searchBarang(
                    userSessionManager.getToken().toString(),
                    q
                )
                if (res.isSuccessful) {
                    _listBarang.value = res.body()?.data
                } else {
                    val errRes =
                        Gson().fromJson(res.errorBody().toString(), ApiResponseItems::class.java)
                    Toast.makeText(context, errRes.message, Toast.LENGTH_LONG)
                }
            } catch (ee: Exception) {
                Toast.makeText(context, "Oops, something went wrong", Toast.LENGTH_LONG)
            }
        }
    }

    fun addItem(item: BarangItem, add: Int): Int{
        val cekotItem = _checkoutItem
        if(!cekotItem.value!!.containsKey(item.kodeBarang)){
            if(add > 0){
                cekotItem.value!![item.kodeBarang] = CoItem(
                    item.id, item.harga, 1
                )
            }
            return add
        }
        else{
            val cil = cekotItem.value!![item.kodeBarang]!!.qty + add
            cekotItem.value!![item.kodeBarang]!!.qty = cil
            if(cil <= 0){
                cekotItem.value!!.remove(item.kodeBarang)
            }
            return cil
        }
    }

    fun checkout(){
        viewModelScope.launch {
            val list = checkoutItem.value!!.values.toList()
            if(list.isNotEmpty()){
                try{
                    val res = HttpApi.retrofitService.checkout(
                        userSessionManager.getToken().toString(), list
                    )

                    if(res.isSuccessful){
                        _coResponse.value = res.body()
                    } else {
                        val errRes =
                            Gson().fromJson(res.errorBody().toString(), ApiResponse::class.java)
                        _coResponse.value = errRes
                        Toast.makeText(context, errRes.message, Toast.LENGTH_LONG)
                    }
                } catch (ee: Exception){
                    Toast.makeText(context, "Oops, something went wrong", Toast.LENGTH_LONG)
                }
            }
            else{
                Toast.makeText(context, "Mohon pilih barang terlebih dahulu!!", Toast.LENGTH_LONG)
            }
        }
    }
}

data class CoItem(
    var id_barang: Int,
    var harga: Int,
    var qty: Int
)