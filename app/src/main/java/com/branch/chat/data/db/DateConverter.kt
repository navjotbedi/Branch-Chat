package com.branch.chat.data.db

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    companion object {
        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
    }

    @TypeConverter
    @FromJson
    fun fromJson(value: String?): Date? {
        return try {
            value?.let { simpleDateFormat.parse(it) }
        } catch (ex: Exception) {
            null
        }
    }

    @TypeConverter
    @ToJson
    fun toJson(date: Date?): String? {
        return try {
            date?.let {
                simpleDateFormat.format(date)
            }
        } catch (ex: Exception) {
            null
        }
    }

}