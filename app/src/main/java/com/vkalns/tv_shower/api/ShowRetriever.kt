package com.vkalns.tv_shower.api

import com.vkalns.tv_shower.model.Show
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


class ShowRetriever(cacheDir: File) {
    private val api: TVMazeApi

    companion object {
        const val BASE_API_URL = "https://api.tvmaze.com/singlesearch/"
    }

    init {

        // Create a cache object
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val httpCacheDirectory = File(cacheDir, "http-cache")
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())

        // create a network cache interceptor, setting the max age to 3 minute
        val networkCacheInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())

            var cacheControl = CacheControl.Builder()
                .maxAge(3, TimeUnit.MINUTES)
                .build()

            response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
        }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(networkCacheInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .client(httpClient)
            .build()
        api = retrofit.create(TVMazeApi::class.java)
    }

    fun getSingleShow(name: String, callback: Callback<Show>) {
        val call = api.getSingleShow(name)
        call.enqueue(callback)
    }
}