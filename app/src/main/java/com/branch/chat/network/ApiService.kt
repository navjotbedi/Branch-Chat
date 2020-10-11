package com.branch.chat.network

import com.branch.chat.data.api.LoginModel
import com.branch.chat.data.db.Message
import com.branch.chat.data.api.LoginResponse
import com.branch.chat.data.api.MessageRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("api/login")
    suspend fun login(
        @Body loginModel: LoginModel
    ): LoginResponse

    @POST("api/messages")
    suspend fun sendMessage(
        @Body messageRequest: MessageRequest
    ): Message

    @GET("api/messages")
    suspend fun getAllMessages(): List<Message>
}