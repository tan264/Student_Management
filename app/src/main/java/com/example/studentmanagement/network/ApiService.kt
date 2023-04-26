package com.example.studentmanagement.network

import com.example.studentmanagement.model.Student
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

enum class ApiStatus { LOADING, ERROR, DONE }

const val BASE_URL = "http://10.0.2.2/mamnonbanmai/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val client = OkHttpClient.Builder().readTimeout(5000, TimeUnit.MILLISECONDS)
    .writeTimeout(5000, TimeUnit.MILLISECONDS).connectTimeout(10000, TimeUnit.MILLISECONDS)
    .retryOnConnectionFailure(true).build()
private val retrofit =
    Retrofit.Builder().client(client).addConverterFactory(
        MoshiConverterFactory.create(moshi).asLenient()
    ).baseUrl(
        BASE_URL
    ).build()

interface ApiService {
    @Multipart
    @POST("uploadImage.php")
    fun uploadImage(@Part image: MultipartBody.Part): Call<String>

    @FormUrlEncoded
    @POST("insert.php")
    fun insertData(
        @Field("id_class") idClass: Int,
        @Field("full_name") fullName: String,
        @Field("nick_name") nickName: String,
        @Field("birthday") birthday: String,
        @Field("gender") gender: String,
        @Field("avatar") avatar: String
    ): Call<String>

    @FormUrlEncoded
    @POST("edit.php")
    fun editData(
        @Field("id_student") idStudent: Int,
        @Field("id_class") idClass: Int,
        @Field("full_name") fullName: String,
        @Field("nick_name") nickName: String,
        @Field("birthday") birthday: String,
        @Field("gender") gender: String,
        @Field("avatar") avatar: String,
        @Field("address") address: String
    ): Call<String>

    @GET("getListStudents.php")
    suspend fun getListStudents(): List<Student>

    @GET("getStudent.php")
    suspend fun getStudent(): Student

    @FormUrlEncoded
    @POST("deleteStudent.php")
    fun deleteStudent(@Field("id_student") idStudent: Int): Call<String>
}

object Api {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}