package com.example.studentmanagement.model

import com.squareup.moshi.Json

data class Parent(
    @Json(name = "name") val name: String,
    @Json(name = "relationship") val relationship: String,
    @Json(name = "tel") val tel: String
)