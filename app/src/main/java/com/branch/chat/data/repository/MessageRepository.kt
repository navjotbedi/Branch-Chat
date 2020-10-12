package com.branch.chat.data.repository

import android.util.Log
import com.branch.chat.data.db.Message
import com.branch.chat.data.api.MessageRequest
import com.branch.chat.data.db.MessageDao
import com.branch.chat.network.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val messageDao: MessageDao,
    private val apiService: ApiService
) {

    @ExperimentalCoroutinesApi
    fun getMessages() = messageDao.getMessagesDistinctUntilChanged()

    @ExperimentalCoroutinesApi
    fun getConversation(threadId: Long) = messageDao.getConversationDistinctUntilChanged(threadId)

    fun storeAllMessages(messages: List<Message>) = messageDao.insertAll(messages)

    suspend fun fetchMessages() = flowOf(apiService.getAllMessages()).map { storeAllMessages(it) }

    suspend fun sendMessage(request: MessageRequest) = apiService.sendMessage(request)

}