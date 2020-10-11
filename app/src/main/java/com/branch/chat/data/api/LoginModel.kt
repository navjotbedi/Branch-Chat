package com.branch.chat.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginModel(
    @Json(name = "username") val email: String?,
    @Json(name = "password") val password: String?
)