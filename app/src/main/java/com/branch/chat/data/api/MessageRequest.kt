package com.branch.chat.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageRequest(
    @Json(name = "thread_id") val threadId: Long,
    @Json(name = "body") val message: String
)