package com.vkalns.tv_shower.api

import com.vkalns.tv_shower.model.Show
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


class ShowRetriever {
    private val api: TVMazeApi

    companion object {
        const val BASE_API_URL = "https://api.tvmaze.com/singlesearch/"
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
        api = retrofit.create(TVMazeApi::class.java)
    }

    fun getSingleShow(name: String, callback: Callback<Show>){
        val call = api.getSingleShow(name)
        call.enqueue(callback)
    }
}