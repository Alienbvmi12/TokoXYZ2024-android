package com.example.tokoxyz2024android.ui.profile

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokoxyz2024android.data.model.ApiResponseSingle
import com.example.tokoxyz2024android.data.model.HttpApi
import com.example.tokoxyz2024android.data.storage.LoginSessionManager
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private var _profileResult = MutableLiveData<ApiResponseSingle>()
    val profileResult: LiveData<ApiResponseSingle> = _profileResult

    val context: Context = app.applicationContext
    private val userSessionManager: LoginSessionManager = LoginSessionManager(context)

    fun getProfile(){
        viewModelScope.launch {
            try{
                val res = HttpApi.retrofitService.profile(userSessionManager.getToken().toString())

                if(res.isSuccessful){
                    _profileResult.value = res.body()
                } else{
                    val fres = Gson().fromJson(res.errorBody()!!.string(), ApiResponseSingle::class.java)
                    _profileResult.value = fres
                    Toast.makeText(context, fres.message, Toast.LENGTH_LONG).show()
                }
            } catch (ee: Exception){
                Toast.makeText(context, "Oops, something went wrong", Toast.LENGTH_LONG).show()
            }
        }
    }

}