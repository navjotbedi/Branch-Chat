package com.branch.chat.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(messages: List<Message>)

    @Query("SELECT * FROM message WHERE threadId = :threadId")
    abstract fun getConversation(threadId: Long): Flow<List<Message>>

    @Query("SELECT * FROM message WHERE id IN (SELECT MAX(id) FROM message GROUP BY threadId) ORDER BY id DESC")
    abstract fun getMessages(): Flow<List<Message>>

    @ExperimentalCoroutinesApi
    fun getMessagesDistinctUntilChanged() = getMessages().distinctUntilChanged()

    @ExperimentalCoroutinesApi
    fun getConversationDistinctUntilChanged(threadId: Long) =
        getConversation(threadId).distinctUntilChanged()

}