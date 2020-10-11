package com.branch.chat.network

import com.branch.chat.utils.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val preferenceManager: PreferenceManager) : Interceptor {

    companion object {
        private const val HEADER_AUTH_TOKEN = "x-branch-auth-token"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        preferenceManager.authToken?.let { authToken ->
            builder.addHeader(HEADER_AUTH_TOKEN, authToken)
        }
        return chain.proceed(builder.build())
    }
}