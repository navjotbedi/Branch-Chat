package com.branch.chat.utils

import android.content.Context
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager to store and fetch shared preferences
 */
@Singleton
class PreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN"
    }

    private val sharedPreferences by lazy {
        getDefaultSharedPreferences(context)
    }

    var authToken: String?
        get() = sharedPreferences.getString(KEY_AUTH_TOKEN, null)
        set(value) = sharedPreferences.edit().putString(KEY_AUTH_TOKEN, value).apply()

    val isLoggedIn: Boolean
        get() = (authToken != null)
}