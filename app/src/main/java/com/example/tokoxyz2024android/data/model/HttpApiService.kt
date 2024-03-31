package com.example.tokoxyz2024android.data.model

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

const val BASE_URL = "http://192.168.43.236:8000/api"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(
    BASE_URL).build()

interface HttpApiService {
    @POST("/login")
    suspend fun login(@Body body: Map<String, String>): Response<ApiResponseSingle>

    @POST("/register")
    suspend fun register(@Body body: Map<String, String>): Response<ApiResponseSingle>

    @POST("/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<ApiResponse>

    @GET("/profile")
    suspend fun profile(@Header("Authorization") token: String): Response<ApiResponse>

    @GET("/barang")
    suspend fun searchBarang(
        @Header("Authorization") token: String,
        @Query("q") q: String
    ): Response<ApiResponse>

    @POST("/checkout")
    suspend fun checkout(
        @Header("Authorization") token: String,
        @Body body: List<Map<String, String>>
    ): Response<ApiResponse>

}

object HttpApi{
    val retrofitService: HttpApiService by lazy{
        retrofit.create(HttpApiService::class.java)
    }
}

data class ApiResponse(
    val status: Int,
    val message: String,
    val data: List<Map<String, Any>?>
)

data class ApiResponseSingle(
    val status: Int,
    val message: String,
    val data: Map<String, String>
)