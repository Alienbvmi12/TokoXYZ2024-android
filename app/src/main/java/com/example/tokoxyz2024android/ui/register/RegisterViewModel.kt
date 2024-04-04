package com.example.tokoxyz2024android.ui.register

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tokoxyz2024android.data.model.ApiResponseSingle
import com.example.tokoxyz2024android.data.model.HttpApi
import com.google.gson.Gson
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private var _registerResult = MutableLiveData<ApiResponseSingle>()
    val registerResult: LiveData<ApiResponseSingle> = _registerResult
    val context: Context = application

    fun register(
        nama: String,
        username: String,
        alamat: String,
        password: String,
        passwordConfirmation: String
    ){
        viewModelScope.launch {
            try{
                val result = HttpApi.retrofitService.register(
                    mapOf(
                        "nama" to nama,
                        "username" to username,
                        "alamat" to alamat,
                        "password" to password,
                        "password_confirmation" to passwordConfirmation
                    )
                )

                if(result.isSuccessful){
                    _registerResult.value = result.body()
                    Toast.makeText(context, "Register Berhasil", Toast.LENGTH_LONG)
                }
                else{
                    val rsu = Gson().fromJson(result.errorBody()!!.string(), ApiResponseSingle::class.java)
                    _registerResult.value = rsu
                    Toast.makeText(context, rsu.message, Toast.LENGTH_LONG).show()
                }
            } catch (ee: Exception){
                Toast.makeText(context, "Internal Server Error", Toast.LENGTH_LONG).show()
            }
        }
    }
}