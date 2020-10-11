package com.branch.chat.di

import com.branch.chat.data.db.DateConverter
import com.branch.chat.network.ApiService
import com.branch.chat.network.AuthInterceptor
import com.branch.chat.utils.PreferenceManager
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class NetworkModule {

    companion object {
        private const val BASE_URL = "https://android-messaging-app-in-2020.herokuapp.com/"
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(prefereneManager: PreferenceManager): Retrofit {
        val logger =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addNetworkInterceptor(AuthInterceptor(prefereneManager))
            .build()

        val moshi = Moshi.Builder()
            .add(DateConverter())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

}