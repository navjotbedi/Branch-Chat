package com.branch.chat.data.db

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    private val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)

    @TypeConverter
    @FromJson
    fun fromJson(value: String?): Date? {
        return value?.let {
            simpleDateFormat.parse(it)
        }
    }

    @TypeConverter
    @ToJson
    fun toJson(date: Date?): String? {
        return date?.let {
            simpleDateFormat.format(date)
        }
    }

}