package com.example.tokoxyz2024android.ui.login

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tokoxyz2024android.data.model.ApiResponse
import com.example.tokoxyz2024android.data.model.ApiResponseError
import com.example.tokoxyz2024android.data.model.ApiResponseSingle
import com.example.tokoxyz2024android.data.model.HttpApi
import com.example.tokoxyz2024android.data.storage.LoginSessionManager
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private var _loginResult = MutableLiveData<ApiResponseSingle>()
    val loginResult: LiveData<ApiResponseSingle> = _loginResult

    private var _loginError = MutableLiveData<ApiResponseError>()
    val loginError: LiveData<ApiResponseError> = _loginError
    var status = MutableLiveData<Boolean>(false)
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
                    val rsu = result.body()!!
                    Log.v("GW", rsu.toString())
                    _loginResult.value = rsu
                    userSessionManager.saveToken((result.body()!!.data!! as Map<String, String>)["token"].toString())
                    Toast.makeText(context,  rsu.message, Toast.LENGTH_LONG).show()
                    status.value = true
                }
                else{
                    val rsu = Gson().fromJson(result.errorBody()!!.string(), ApiResponseError::class.java)
                    Log.v("GW", rsu.toString())
                    _loginError.value = rsu
                    Toast.makeText(context, rsu.message, Toast.LENGTH_LONG).show()
                    status.value = false
                }
            } catch (ee: Exception){
                Log.e("GWS Error", ee.toString())
                Toast.makeText(context, "Internal Server Error", Toast.LENGTH_LONG).show()
                status.value = false
            }
        }
    }
}