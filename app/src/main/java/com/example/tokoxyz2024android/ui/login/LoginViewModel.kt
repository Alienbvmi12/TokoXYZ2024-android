package com.example.tokoxyz2024android.ui.login

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tokoxyz2024android.data.model.ApiResponse
import com.example.tokoxyz2024android.data.model.ApiResponseSingle
import com.example.tokoxyz2024android.data.model.HttpApi
import com.example.tokoxyz2024android.data.storage.LoginSessionManager
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private var _loginResult = MutableLiveData<ApiResponseSingle>()
    val loginResult: LiveData<ApiResponseSingle> = _loginResult
    val context: Context = application

    val userSessionManager = LoginSessionManager(context)

    fun login(username: String, password: String){
        viewModelScope.launch {
            try{
                val result = HttpApi.retrofitService.login(
                    mapOf(
                        "username" to username,
                        "password" to password
                    )
                )
                if(result.isSuccessful){
                    _loginResult.value = result.body()
                    userSessionManager.saveToken(result.body()!!.data["token"].toString())
                }
                else{
                    _loginResult.value = Gson().fromJson(result.errorBody()!!.toString(), ApiResponseSingle::class.java)
                    Toast.makeText(context,  loginResult.value!!.message, Toast.LENGTH_LONG)
                }
            } catch (ee: Exception){
                Toast.makeText(context, "Internal Server Error", Toast.LENGTH_LONG)
            }
        }
    }
}