package com.android.hootor.academy.fundamentals.homework.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class MovieClient {

    companion object {

        private const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL_342 = "https://image.tmdb.org/t/p/w342"
        const val IMAGE_BASE_URL_ORIGINAL = "https://image.tmdb.org/t/p/original"
        private const val API_KEY = "e9d6b2db34845e6100bc75f8e904ebc5"
        const val QUERY_PAGE_SIZE = 20

        @Volatile
        private var INSTANCE: Retrofit? = null

        private val apiKeyInterceptor = Interceptor {chain: Interceptor.Chain ->
            var original = chain.request()
            val url = original.url.newBuilder().addQueryParameter("api_key", API_KEY).build()
            original = original.newBuilder().url(url).build()
            chain.proceed(original)
        }

        private val interceptor = HttpLoggingInterceptor().also {
            it.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        private val client = OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .addInterceptor(apiKeyInterceptor)
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