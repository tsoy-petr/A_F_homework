package com.android.hootor.academy.fundamentals.homework.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class MovieClient {

    companion object {

        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "e9d6b2db34845e6100bc75f8e904ebc5"

        @Volatile
        private var INSTANCE: Retrofit? = null

        private val interceptor = HttpLoggingInterceptor().also {
            it.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        private val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addNetworkInterceptor(interceptor)
            .build()

        private val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        private val contentType = "application/json".toMediaType()

        @ExperimentalSerializationApi
        fun getClient(): Retrofit {
            synchronized(this) {
                return INSTANCE ?: Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(json.asConverterFactory(contentType))
                    .client(client)
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }
}