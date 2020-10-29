package com.vkalns.tv_shower.api

import com.vkalns.tv_shower.model.Show
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TVMazeApi {
    @GET("shows")
    fun getSingleShow(@Query("q") name: String): Call<Show>
}