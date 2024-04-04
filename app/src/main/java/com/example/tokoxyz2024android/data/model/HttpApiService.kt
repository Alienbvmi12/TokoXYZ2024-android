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

const val BASE_URL = "https://guided-cow-diverse.ngrok-free.app/api/"
const val BASE_URL_NO_API = "https://guided-cow-diverse.ngrok-free.app/"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(
    BASE_URL).build()

interface HttpApiService {
    @POST("login")
    suspend fun login(@Body body: Map<String, String>): Response<ApiResponseSingle>

    @POST("register")
    suspend fun register(@Body body: Map<String, String>): Response<ApiResponseSingle>

    @POST("logout")
    suspend fun logout(@Header("Authorization") token: String): Response<ApiResponse>

    @GET("profile")
    suspend fun profile(@Header("Authorization") token: String): Response<ApiResponseSingle>

    @GET("barang")
    suspend fun searchBarang(
        @Header("Authorization") token: String,
        @Query("q") q: String
    ): Response<ApiResponseItems>

    @POST("checkout")
    suspend fun checkout(
        @Header("Authorization") token: String,
        @Body body: CoSubmit
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
    val data: Map<String, String>?
)

data class ApiResponseError(
    val status: Int,
    val message: String,
    val data: List<String>?
)

data class ApiResponseItems(
    val status: Int,
    val message: String,
    val data: List<BarangItem?>
)

data class CoSubmit(
    val items: List<CoItem>
)

data class BarangItem(
    var id: Int,
    @Json(name = "kode_barang") var kodeBarang: String,
    @Json(name = "nama_barang") var namaBarang: String,
    @Json(name = "expired_date") var expiredDate: String,
    var jumlah: Int,
    var harga: Int,
    var image: String,
    var rating: Double,
    @Json(name = "created_at") var createdAt: String,
    @Json(name = "updated_at") var updatedAt: String
)