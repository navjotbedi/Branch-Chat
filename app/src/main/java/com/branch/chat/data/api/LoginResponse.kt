package com.branch.chat.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(@Json(name = "auth_token") val authToken: String)