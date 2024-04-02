package com.example.tokoxyz2024android.data.model

import com.example.tokoxyz2024android.ui.home.CoItem
import com.squareup.moshi.Json
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
const val BASE_URL_NO_API = "http://192.168.43.236:8000/"
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
    suspend fun profile(@Header("Authorization") token: String): Response<ApiResponseSingle>

    @GET("/barang")
    suspend fun searchBarang(
        @Header("Authorization") token: String,
        @Query("q") q: String
    ): Response<ApiResponseItems>

    @POST("/checkout")
    suspend fun checkout(
        @Header("Authorization") token: String,
        @Body body: List<CoItem>
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
    val data: List<Map<String, Any>>
)

data class ApiResponseSingle(
    val status: Int,
    val message: String,
    val data: Map<String, String>
)

data class ApiResponseItems(
    val status: Int,
    val message: String,
    val data: List<BarangItem?>
)

data class BarangItem(
    val id: Int,
    @Json(name = "kode_barang") val kodeBarang: String,
    @Json(name = "nama_barang") val namaBarang: String,
    @Json(name = "expired_date") val expiredDate: String,
    val jumlah: Int,
    val harga: Int,
    val image: String,
    val rating: Float,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "updated_at") val updatedAt: String
)