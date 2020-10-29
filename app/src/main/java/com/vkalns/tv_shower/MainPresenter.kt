package com.vkalns.tv_shower

import android.util.Log
import com.vkalns.tv_shower.api.ShowRetriever
import com.vkalns.tv_shower.model.Show
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainPresenter(val cacheDir: File) : MainContract.Presenter<MainContract.View> {

    private val showRetriever = ShowRetriever(cacheDir)
    private var view: MainContract.View? = null
    private lateinit var callback: Callback<Show>


    override fun onAttach(view: MainContract.View) {
        this.view = view
        callback = object : Callback<Show> {
            override fun onResponse(call: Call<Show>?, response: Response<Show>?) {
                response?.isSuccessful?.let {
                    if (response.raw().cacheResponse() != null) {
                        Log.d("Network", "response came from cache")
                    }

                    if (response.raw().networkResponse() != null) {
                        Log.d("Network", "response came from server")
                    }
                    val show = response.body()
                    if (show != null) {
                        displayShowInfo(show)
                    } else {
                        view.displayErrorToast("No show matches your search criteria")
                    }

                }
            }

            override fun onFailure(call: Call<Show>?, t: Throwable?) {
                view.displayErrorToast("Failed to retrieve data: \n ${t?.localizedMessage.orEmpty()}")
            }
        }
    }

    override fun onDetach() {
        this.view = null
    }

    override fun searchForShow(name: String) {
        if (view?.isNetworkConnected() == true) {
            showRetriever.getSingleShow(name, callback)
        }
    }


    override fun displayShowInfo(show: Show) {
        view?.displayShowTitle(show.name)
        view?.displayShowPoster(show.image.originalImage)
        view?.displayDaysSincePremiere(getDaysCountToDisplay(show.premiered.orEmpty()))
    }

    override fun getDaysCountToDisplay(releaseDateString: String): String {
        return if (releaseDateString.isNotBlank()) {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
            val releaseDate: Date = dateFormatter.parse(releaseDateString)
            val diffBetweenDates = Date().time - releaseDate.time
            val daysBetween = TimeUnit.DAYS.convert(diffBetweenDates, TimeUnit.MILLISECONDS)
            "$daysBetween days since release"
        } else {
            "Release date unknown"
        }
    }

    private fun setupCallback(): Callback<Show> {
        return object : Callback<Show> {
            override fun onResponse(call: Call<Show>?, response: Response<Show>?) {
                response?.isSuccessful?.let {
                    response.body()?.let { show ->
                        displayShowInfo(show)
                    }
                }
            }

            override fun onFailure(call: Call<Show>?, t: Throwable?) {
                view?.displayErrorToast("Failed to retrieve data: \n ${t?.localizedMessage.orEmpty()}")
            }
        }
    }

}