package com.branch.chat.data.db

import androidx.room.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat
import java.util.*

@JsonClass(generateAdapter = true)
@Entity(tableName = "message")
@TypeConverters(DateConverter::class)
data class Message(
    @PrimaryKey @ColumnInfo(name = "id") @Json(name = "id") val messageId: Long,
    @Json(name = "thread_id") val threadId: Long,
    @Json(name = "user_id") val userId: String?,
    @Json(name = "body") val body: String,
    @Json(name = "agent_id") val agentId: String?,
    @Json(name = "timestamp") val timestamp: Date?
) {
    companion object {
        private val simpleDateFormat = SimpleDateFormat("dd MMM, yyyy HH:mm", Locale.US)
    }

    @Ignore
    @Transient
    val dateString: String? = timestamp?.let { simpleDateFormat.format(it) }
    @Ignore
    @Transient
    val senderId: String? = agentId ?: userId
}