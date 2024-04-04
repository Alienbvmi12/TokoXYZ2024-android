package com.example.tokoxyz2024android.ui.home

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tokoxyz2024android.data.model.ApiResponse
import com.example.tokoxyz2024android.data.model.ApiResponseItems
import com.example.tokoxyz2024android.data.model.ApiResponseSingle
import com.example.tokoxyz2024android.data.model.BarangItem
import com.example.tokoxyz2024android.data.model.CoSubmit
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
        _listBarang.value = listOf()
    }

    fun searchBarang(q: String = "") {
        viewModelScope.launch {
            try {
                val res = HttpApi.retrofitService.searchBarang(
                    userSessionManager.getToken().toString(),
                    q
                )
                if (res.isSuccessful) {
                    _listBarang.value = res.body()!!.data
                    Log.v("GW", res.body().toString())
                } else {
                    val errRes =
                        Gson().fromJson(res.errorBody()!!.string(), ApiResponseItems::class.java)
                    Toast.makeText(context, errRes.message, Toast.LENGTH_LONG).show()
                    Log.v("GW", errRes.message)
                }
            } catch (ee: Exception) {
                Log.e("GWS Error", ee.toString())
                Toast.makeText(context, "Oops, something went wrong", Toast.LENGTH_LONG).show()
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
                return add
            }
            else{
                return 0
            }
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

    fun getSelectedItemQty(kodeBarang: String): Int {
        return _checkoutItem.value?.get(kodeBarang)?.qty ?: 0
    }

    fun checkout(){
        viewModelScope.launch {
            val list2 = checkoutItem.value!!.values.toList()
            val list = CoSubmit(list2)
            if(list2.size > 0){
                try{
                    val res = HttpApi.retrofitService.checkout(
                        userSessionManager.getToken().toString(), list
                    )

                    if(res.isSuccessful){
                        _coResponse.value = res.body()
                        Log.v("MAG", "Success")
                    } else {
                        Log.v("MAGs", "Alert")
                        val errRes =
                            Gson().fromJson(res.errorBody()!!.string(), ApiResponse::class.java)
                        _coResponse.value = errRes
                        Toast.makeText(context, errRes.message, Toast.LENGTH_LONG).show()
                    }
                } catch (ee: Exception){
                    Log.e("MAG Error", ee.toString())
                    Toast.makeText(context, "Oops, something went wrong", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(context, "Mohon pilih barang terlebih dahulu!!", Toast.LENGTH_LONG).show()
            }
        }
    }
}

data class CoItem(
    var id_barang: Int,
    var harga: Int,
    var qty: Int
)