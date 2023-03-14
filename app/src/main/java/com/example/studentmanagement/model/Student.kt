package com.example.studentmanagement.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    @Json(name = "id") val id: Int,
    @Json(name = "fullName") val fullName: String,
    @Json(name = "nickName") val nickName: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "age") val age: Int,
    @Json(name = "avatar") val avatar: String,
    @Json(name = "birthday") val birthday: String,
    @Json(name = "class") val classStudent: String,
    @Json(name = "address") val address: String?
) : Parcelable